package com.example.steam.domain.game;

import com.example.steam.domain.game.dto.*;
import com.example.steam.domain.game.genre.GameGenre;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.AdminAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        Game game = gameService.createGame(requestDto);
        List<GameGenre> genres = gameService.addGenresToGame(game, requestDto.getGenres());
        log.info("genres = {} ", genres.toString());
        GameDetailResponse response = new GameDetailResponse(game);
        log.info("response = {}",response);
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
    public Response<String> deleteGame(@PathVariable("id") Long id) {
        gameService.deleteGame(id);
        return Response.success("게임이 삭제 되었습니다.");
    }

    // 할인 적용
    @AdminAuthorize
    @PatchMapping("/{id}/discount")
    public Response<GameDetailResponse> applyDiscount(@PathVariable("id") Long id, @RequestBody GameDiscountRequest request) {
        GameDetailResponse response = gameService.applyDiscount(id, request);
        return Response.success(response);
    }

    // 게임 오픈
    @AdminAuthorize
    @PatchMapping("/{id}/open")
    public Response<Void> publishGame(@PathVariable("id") Long id) {
        gameService.publishGame(id);

        return Response.success();
    }

    // 게임 정보 수정
    @AdminAuthorize
    @PatchMapping("/{id}/info")
    public Response<GameUpdateResponse> updateGameInfo(@PathVariable("id") Long id, @RequestBody GameUpdateRequest request) {
        GameUpdateResponse response = gameService.updateGame(id, request);

        return Response.success(response);
    }

    // 게임 목록 조회 (검색 + 페이징)
    @GetMapping
    public Response<Page<Game>> getAllGames(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String genre,
                                            Pageable pageable) {

        Page<Game> allGames = gameService.getAllGames(keyword, genre, pageable);

        return Response.success(allGames);
    }

    // 인기 게임 조회
//    @GetMapping("/pop")
//    public Response<>
}
