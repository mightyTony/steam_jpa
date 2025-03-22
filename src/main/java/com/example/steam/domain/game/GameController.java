package com.example.steam.domain.game;

import com.example.steam.domain.game.dto.*;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.AdminAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game")
public class GameController {

    private final GameService gameService;

//    @AdminAuthorize
//    @PostMapping("")
//    public Response<Game> createGame(@RequestPart("data") GameCreateRequest requestDto, @RequestPart("image") MultipartFile imageFile) {
//        Game game = gameService.createGame(requestDto, imageFile);
//
//        return  Response.success();
//    }

    // 게임 등록
    @AdminAuthorize
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
    public Response<GameDetailResponse> createGame(
            @RequestPart("data") GameCreateRequest requestDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile, @AuthenticationPrincipal User user) {
        Game game = gameService.createGame(requestDto);

        GameDetailResponse response = new GameDetailResponse(game);

        log.info("[게임][어드민] 게임이 등록 되었습니다 : {}, 등록 한 유저 :{}", response.getName(), user.getUsername());
        return  Response.success(response);
    }

    // 특정 게임 상세 조회
    @GetMapping("/{id}")
    public Response<GameDetailResponse> getGameInfoDetail(@PathVariable("id") Long id) {
        GameDetailResponse response = gameService.getGameById(id);
        return Response.success(response);
    }

    // 게임 삭제
    @AdminAuthorize
    @DeleteMapping("/{id}")
    public Response<String> deleteGame(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        gameService.deleteGame(id, user);
        return Response.success("게임이 삭제 되었습니다.");
    }

    // 할인 적용
    @AdminAuthorize
    @PatchMapping("/{id}/discount")
    public Response<GameDetailResponse> applyDiscount(@PathVariable("id") Long id, @RequestBody GameDiscountRequest request, @AuthenticationPrincipal User user) {
        GameDetailResponse response = gameService.applyDiscount(id, request, user);

        return Response.success(response);
    }

    // 게임 오픈
    @AdminAuthorize
    @PatchMapping("/{id}/open")
    public Response<Void> publishGame(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        gameService.publishGame(id, user);

        return Response.success();
    }

    // 게임 정보 수정
    @AdminAuthorize
    @PatchMapping("/{id}/info")
    public Response<GameUpdateResponse> updateGameInfo(@PathVariable("id") Long id, @RequestBody GameUpdateRequest request, @AuthenticationPrincipal User user) {
        GameUpdateResponse response = gameService.updateGame(id, request);
        log.info("[게임][어드민] 게임 정보 수정 - {} / by {}", response.getName(), user.getUsername());
        return Response.success(response);
    }

    // 게임 목록 조회 (검색 + 페이징) 카테고리 : 인기상품, 신규게임, 가격
    @GetMapping("/category")
    public Response<Page<GameDetailResponse>> getGamesByCategory(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            Pageable pageable) {
        log.info("[게임 목록 조회]");
        Page<GameDetailResponse> result = gameService.getGamesByCategory(category, name, minPrice, maxPrice, pageable);

        return Response.success(result);
    }
}
