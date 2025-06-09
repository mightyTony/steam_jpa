package com.example.steam.domain.game;

import com.example.steam.domain.game.dto.*;
import com.example.steam.domain.game.genre.GameGenre;
import com.example.steam.domain.game.genre.GameGenreRepository;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.notification.event.GameSaleEvent;
import com.example.steam.domain.user.User;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import com.example.steam.infra.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final GameGenreRepository genreRepository;
    private final S3Util s3Util;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String S3_GAME_DIRNAME = "image/game";
    private final ApplicationEventPublisher eventPublisher;

    // 게임 등록
    @Transactional
    public Game createGame(GameCreateRequest request) {
        // 1. 이미 존재하는 게임인지 체크
        Optional<Game> isExistedGame = gameRepository.findByName(request.getName());
        if(isExistedGame.isPresent()) {
            throw new SteamException(ErrorCode.ALREADY_EXISTED_GAME);
        }

        // 2. 이미지 업로드
//        // 파일 검증
//        if(imageFile.isEmpty() || Objects.requireNonNull(imageFile.getOriginalFilename()).isEmpty()) {
//            throw new SteamException(ErrorCode.ILLEGAL_ARGUMENT_MULTIPARTFILE);
//        }
//        // 파일 업로드
//        String imageCloudFrontUrl = s3Util.upload(imageFile, S3_GAME_DIRNAME);

        // 3. 게임 저장
        Game game = Game.builder()
                .name(request.getName())
                .developer(request.getDeveloper())
                .publisher(request.getPublisher())
                .content(request.getContent())
                .price(request.getPrice())
                .totalPrice(request.getPrice())
                //.pictureUrl(imageCloudFrontUrl)
                .releaseDate(request.getReleaseDate())
                .onSale(false)
                .build();

        // 4. 장르 저장
        addGenresToGame(game, request.getGenres());

        return gameRepository.saveAndFlush(game);
    }

    private void addGenresToGame(Game game, List<String> genres) {
        List<GameGenre> genreList = new ArrayList<>();
        for(String genreName : genres) {
            GameGenre gameGenre = GameGenre.builder()
                    .game(game)
                    .genreName(genreName)
                    .build();
            genreList.add(gameGenre);
        }

        genreRepository.saveAll(genreList);
        game.getGenres().addAll(genreList);
    }

    // 특정 게임 상세 조회
    @Transactional(readOnly = true)
    public GameDetailResponse getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));
        return new GameDetailResponse(game);
    }

    // 게임 삭제
    @Transactional
    public void deleteGame(Long id, @AuthenticationPrincipal User user) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        gameRepository.deleteById(id);
        log.info("[LOG] [게임][어드민] 게임이 삭제 되었습니다. 게임 명 - {}, 삭제 한 유저 : {}", game.getName(), user.getUsername());
    }

    // 할인 적용
    @Transactional
    public GameDetailResponse applyDiscount(Long id, GameDiscountRequest discountDto, @AuthenticationPrincipal User user) {
        // 1. 조회
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        // 2. 최종가 설정
        game.discount(discountDto.getDiscount());

        // 3. 정보 업데이트
        Game saved = gameRepository.save(game);

        log.info("[LOG] [게임][어드민] 할인 적용 되었습니다. {} / by {}", game.getName(), user.getUsername());

        // 4. 할인 메시지 전송
        eventPublisher.publishEvent(new GameSaleEvent(game));

        return new GameDetailResponse(saved);
    }

    @Transactional
    public void publishGame(Long id, @AuthenticationPrincipal User user) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));
        game.publish();

        gameRepository.save(game);

        log.info("[LOG] [게임][어드민] 게임이 릴리즈 되었습니다. {} / by {}", game.getName(), user.getUsername());
    }

    // 게임 정보 수정
    @Transactional
    public GameUpdateResponse updateGame(Long gameId, GameUpdateRequest request) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        game.update(request.getName(),
                request.getDeveloper(),
                request.getPublisher(),
                request.getContent(),
                request.getPrice()
        );

        gameRepository.save(game);

        return new GameUpdateResponse(game);
    }

    @Transactional(readOnly = true)
    public Page<GameDetailResponse> getGamesByCategory(String category, String name, Integer minPrice, Integer maxPrice, int page, int size) {
        return gameRepository.findGamesByCategory(category,name,minPrice,maxPrice, page, size);
    }

    @Transactional
    public String editGamePicture(Long gameId, MultipartFile imageFile) throws IOException {
        // 게임 검증
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        // 파일 검증
        if(imageFile.isEmpty() || Objects.requireNonNull(imageFile.getOriginalFilename()).isEmpty()) {
            throw new SteamException(ErrorCode.ILLEGAL_ARGUMENT_MULTIPARTFILE);
        }
        // 파일 업로드
        String imageCloudFrontUrl = s3Util.uploadImageFile(imageFile, S3_GAME_DIRNAME);

        // 이미지 변경
        game.uploadImage(imageCloudFrontUrl);
        gameRepository.save(game);

        log.info("[LOG] [게임 이미지 변경] gameId : {}, uploadImageUrl : {}", game.getId(), game.getPictureUrl());

        return game.getPictureUrl();
    }

    public List<GameRankingResponse> getTopGameRanking(String redisKey, Duration redisTTL, LocalDateTime fromDate) {
        Object cachedData = redisTemplate.opsForValue().get(redisKey);

        if(cachedData != null) {
            log.info("[LOG] [캐시 히트] 게임 랭킹 데이터 key: {}", redisKey);
            return (List<GameRankingResponse>)cachedData;
        }

        log.warn("[LOG] [CACHE MISS] key: {} - DB 조회 후 캐싱 시도", redisKey);
        List<GameRankingResponse> result = gameRepository.findTopGamesBySales(fromDate);

        // 캐싱
        redisTemplate.opsForValue().set(redisKey, result, redisTTL);
        log.info("[LOG] [CACHE PUT] key: {}, TTL: {} days, size: {}", redisKey, redisTTL.toDays(), result.size());

        return result;
    }

    public List<GameRankingResponse> getTopRanking(String topRankingKey, Duration topTtl) {
        Object cachedData = redisTemplate.opsForValue().get(topRankingKey);

        if(cachedData != null) {
            log.info("[LOG] [캐시 히트] 탑 랭킹 게임 데이터 key: {}", topRankingKey);
            return (List<GameRankingResponse>)cachedData;
        }

        log.warn("[LOG] [CACHE MISS] key: {} - DB 조회 후 캐싱 시도", topRankingKey);
        List<GameRankingResponse> result = gameRepository.findTopGames();

        // 캐싱
        redisTemplate.opsForValue().set(topRankingKey, result, topTtl);
        log.info("[LOG] [CACHE PUT] key: {}, TTL: {} days, size: {}", topRankingKey, topTtl.toDays(), result.size());

        return result;
    }
}
