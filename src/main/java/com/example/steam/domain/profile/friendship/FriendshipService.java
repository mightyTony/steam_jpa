package com.example.steam.domain.profile.friendship;

import com.example.steam.domain.notification.event.FriendEventNotification;
import com.example.steam.domain.profile.friendship.dto.*;
import com.example.steam.domain.profile.friendship.query.FriendshipRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /*
    # 친구 신청
    - (대기 상태 생성), 보낸 이, 받는 이
    - 보낸 이 = 로그인 한 유저
    - 받는 이 있는 지 검증
     */
    @Transactional
    public Friendship requestFriendship(User user, FriendshipRequest request) {
        // 받는 이 유저 검증
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));
        // 이미 친구 인지 검증
        if(friendRepository.isFriend(user.getId(), receiver.getId())) {
            throw new SteamException(ErrorCode.ALREADY_FRIENDSHIP);
        }
        // 자기 자신한테 보내는 거 막기
        if(user.getId().equals(request.getReceiverId())) {
            throw new SteamException(ErrorCode.ILLEGAL_FRIENDSHIP);
        }

        // 친구 요청
        Friendship friendship = Friendship.builder()
                .status(FriendStatus.PENDING)
                .sender(user)
                .receiver(receiver)
                .build();

        eventPublisher.publishEvent(new FriendEventNotification(receiver, user));

        return friendRepository.save(friendship);
    }

    /*
    # 친구 요청 목록 조회
    - 응답 값 : 유저 id, 유저네임(실질적 아이디) 닉네임, 프로필 사진
    - 조건 : 대기 상태, 받는 이 = 나
 */
    @Transactional(readOnly = true)
    public List<FriendshipResponse> getFriendshipRequest(User user) {
        List<Friendship> friendships = friendRepository.findFriendshipsByReceiverAndStatus(user, FriendStatus.PENDING);

        return friendships.stream()
                .map(FriendshipResponse::new)
                .collect(Collectors.toList());
    }

    /*
        # 친구 요청 승락/거절
        - 승락 시 : 대기 상태 -> 수락 상태 변경
        - 거절 시 : 친구 관계 삭제
        - 이외의 FriendStatus 값 검증 예외 처리
     */
    @Transactional
    public FriendshipResponse updateFriendshipRequestStatus(Long requestId, FriendshipRequestUpdateDto requestDto) {
        // FriendStatus 값 검증 예외 처리 (승락,거절 이외 시 예외)
        if(!(requestDto.getFriendStatus().equals(FriendStatus.ACCEPTED)
                || requestDto.getFriendStatus().equals(FriendStatus.REJECTED))) {
            throw new SteamException(ErrorCode.ILLEGAL_FRIENDSHIP_REQUEST);
        }

        Friendship friendship = friendRepository.findById(requestId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_FRIENDSHIP));

        if(friendship.getStatus().equals(FriendStatus.PENDING)) {
            // 거절 시 찬구 관계 삭제
            if (requestDto.getFriendStatus().equals(FriendStatus.REJECTED)) {
                friendRepository.delete(friendship);
                return null;
            }
            // 승락 시 대기 -> 수락
            else if(requestDto.getFriendStatus().equals(FriendStatus.ACCEPTED)){
                friendship.accepted();
                Friendship saved = friendRepository.save(friendship);
                return FriendshipResponse.toDto(saved);
            }
        }
        return null;
    }


    /*
    # 내 친구 조회
    - 조회 조건 : 보낸이 id = 나 or 받은이 id = 나, 친구 상태(FriendStatus) = ACCEPTED
    - 응답 : 유저 id, 아이디, 닉네임, 프로필 사진
    */
    @Transactional(readOnly = true)
    public List<FriendshipReadResponse> getMyFriends(Long userId) {
//        userService.isExisted(user);

        return friendRepository.findFriendships(userId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_MY_FRIENDSHIP));
    }

    /*
    # 내가 보낸 친구 요청 (대기중 인것) 조회
    - 조건 : 보낸 이 = 나, 상태 = 대기 중
    */
    @Transactional(readOnly = true)
    public List<FriendshipSearchResponse> getMyFriendshipRequest(User user) {
//        userService.isExisted(user);

        userRepository.findById(user.getId())
                        .orElseThrow(()-> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));

        return friendRepository.getMyFriendshipRequest(user);
    }

    /*
    # 내가 보낸 친구 요청 (대기중 인것) 삭제
    - 친구 관계 삭제
    */
    @Transactional
    public void deleteMyFriendshipRequest(User user, Long requestId) {
        Friendship friendship = friendRepository.findById(requestId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_FRIENDSHIP));

        if (!friendship.getStatus().equals(FriendStatus.PENDING)){
            throw new SteamException(ErrorCode.NOT_PENDING_STATUS_FRIENDSHIP);
        }

        friendRepository.deleteById(requestId);
    }

    @Transactional(readOnly = true)
    public FriendshipShortViewResponse getMyFriendsList(Long userId) {
        return friendRepository.getMyFriendsListShort(userId);
    }


    @Transactional
    public void deleteMyFriendship(User user, Long friendshipId) {
        Friendship friendship = friendRepository.findById(friendshipId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_FRIENDSHIP));

        log.info("sender : {} , receiver : {}", friendship.getSender().getUsername(), friendship.getReceiver().getUsername());

        Boolean senderCheck = user.getUsername().equals(friendship.getSender().getUsername());
        Boolean receiverCheck = user.getUsername().equals(friendship.getReceiver().getUsername());

        if(!(senderCheck || receiverCheck)) {
            throw new SteamException(ErrorCode.UNAUTHORIZED);
        }

        friendRepository.deleteById(friendshipId);
    }
}
