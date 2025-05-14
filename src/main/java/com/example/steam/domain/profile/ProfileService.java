package com.example.steam.domain.profile;

import com.example.steam.domain.notification.Notification;
import com.example.steam.domain.notification.event.CommentWriteEvent;
import com.example.steam.domain.notification.model.NotiType;
import com.example.steam.domain.notification.query.NotificationRepository;
import com.example.steam.domain.profile.comment.Comment;
import com.example.steam.domain.profile.dto.*;
import com.example.steam.domain.profile.query.MyGameRepository;
import com.example.steam.domain.profile.query.CommentRepository;
import com.example.steam.domain.profile.query.ProfileRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.domain.user.UserService;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import com.example.steam.infra.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final S3Util s3Util;
    private final MyGameRepository myGameRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final String S3_USER_DIRNAME = "image/user";

    @Transactional(readOnly = true)
    public ProfileResponse getProfileInfo(Long userId) {
        // 유저 검증
        userService.isExistedBoolean(userId);
        log.info("[프로필 정보 쿼리]");
        // 프로필 정보
        return profileRepository.findByUserId(userId);


    }

    @Transactional
    public String editProfileImage(MultipartFile imageFile, Long userId) throws IOException {
        // 유저 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));

        // 유저 본인 검증
        if(!user.getId().equals(userId)) {
            throw new SteamException(ErrorCode.UNAUTHORIZED);
        }

        // 파일 검증
        if(imageFile.isEmpty() || Objects.requireNonNull(imageFile.getOriginalFilename()).isEmpty()) {
            throw new SteamException(ErrorCode.ILLEGAL_ARGUMENT_MULTIPARTFILE);
        }
        // 파일 업로드
        String imageCloudFrontUrl = s3Util.uploadImageFile(imageFile, S3_USER_DIRNAME);

        // 이미지 변경
        user.updateImage(imageCloudFrontUrl);
        userRepository.save(user);

        log.info("[유저 이미지 변경] userId : {}, uploadImageUrl : {}", user.getId(), user.getProfileImageUrl());

        return user.getProfileImageUrl();
    }

    @Transactional
    public ProfileResponse editProfileInfo(User user, Long userId, ProfileUpdateRequest request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));

        // 유저 본인 검증
        log.info("findUser.getId : {}, user.getId() : {}, equals : {},", findUser.getId(), user.getId(), findUser.getId().equals(user.getId()));
        if(!findUser.getId().equals(user.getId())) {
            throw new SteamException(ErrorCode.UNAUTHORIZED);
        }

        // user-profile
        Profile profile = profileRepository.findProfileByUser(user);
        profile.update(request.getContent());
        // save
        profileRepository.save(profile);

        return ProfileResponse.update(profile);
    }

    /*
        프로필 댓글 작성
        POST
        req : content, user
        res : content, writerId, writerName, createdTime, updatedTime
     */
    @Transactional
    public CommentResponse postComment(User writer, Long profileId, CommentPostRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_PROFILE));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .profile(profile)
                .writer(writer)
                .build();

        commentRepository.save(comment);

        // 알림 이벤트 생성
//        postCommentNotification(writer, profile);
        eventPublisher.publishEvent(new CommentWriteEvent(writer, profile));



        return CommentResponse.toDto(comment);
    }

    private void postCommentNotification(User writer, Profile profile){
        if(!profile.getUser().equals(writer)) {
//            Notification notification = Notification.builder()
//                    .user(profile.getUser())
//                    .title("내 프로필에 댓글이 달렸어요")
//                    .message(writer.getNickname() + "님이 프로필에 댓글을 남겼습니다. ")
//                    .type(NotiType.PROFILE_COMMENT)
//                    .build();
            Notification notification = Notification.notify(
                    writer.getId(),
                    NotiType.PROFILE_COMMENT,
                    "내 프로필에 댓글이 달렸어요",
                    writer.getNickname() + "님이 프로필에 댓글을 남겼습니다.",
                    false,
                    LocalDateTime.now().toString()
            );

            notificationRepository.save(notification);
        }
    }

    // 프로필 내 댓글 삭제
    @Transactional
    public void deleteComment(User user, Long profileId, Long commentId) {
        // 코멘트 작성자 혹은 프로필 유저만 삭제 가능함, 검증
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_PROFILE));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_COMMENT));

        log.info("user : {}", user);
        log.info("profile.getUser() : {}", profile.getUser());
        log.info("comment.getWriter() :  {}", comment.getWriter());

        if(user.equals(profile.getUser())  || user.equals(comment.getWriter())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new SteamException(ErrorCode.UNAUTHORIZED);
        }

    }

    public Page<CommentResponse> getComments(Long profileId, int page, int size, Pageable pageable) {
        Page<CommentResponse> result = commentRepository.findCommentsWithPaging(page,size,pageable, profileId);

        if (result.isEmpty()) {
            throw new SteamException(ErrorCode.NOT_FOUND_COMMENT);
        }
        return result;
    }

    public List<MyGameResponse> getMyGames(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));

        List<MyGameResponse> result = myGameRepository.getMyGamesByUser(user);

        return result;
    }
}
