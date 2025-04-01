package com.example.steam.domain.game;

import com.example.steam.domain.game.dto.*;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.AdminAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game")
@Tag(name = "게임", description = "게임 관련 API")
public class GameController {

    private final GameService gameService;

    @Operation(summary = "게임 등록", description = "관리자가 게임을 등록합니다. JSON 데이터 + 이미지 파일 업로드 필요")
    @AdminAuthorize
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
    public Response<GameDetailResponse> createGame(
            @RequestPart("data") GameCreateRequest requestDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal User user) throws IOException {
        Game game = gameService.createGame(requestDto,imageFile);

        GameDetailResponse response = new GameDetailResponse(game);

        log.info("[게임][어드민] 게임이 등록 되었습니다 : {}, 등록 한 유저 :{}", response.getName(), user.getUsername());
        return  Response.success(response);
    }

    @Operation(summary = "게임 상세 조회", description = "게임 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public Response<GameDetailResponse> getGameInfoDetail(@PathVariable("id") Long id) {
        GameDetailResponse response = gameService.getGameById(id);
        return Response.success(response);
    }

    @Operation(summary = "게임 삭제", description = "관리자가 게임을 삭제합니다.")
    @AdminAuthorize
    @DeleteMapping("/{id}")
    public Response<String> deleteGame(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        gameService.deleteGame(id, user);
        return Response.success("게임이 삭제 되었습니다.");
    }

    @Operation(summary = "게임 할인 적용", description = "특정 게임에 할인율을 적용합니다.")
    @AdminAuthorize
    @PatchMapping("/{id}/discount")
    public Response<GameDetailResponse> applyDiscount(@PathVariable("id") Long id, @RequestBody GameDiscountRequest request, @AuthenticationPrincipal User user) {
        GameDetailResponse response = gameService.applyDiscount(id, request, user);

        return Response.success(response);
    }

    @Operation(summary = "게임 오픈", description = "관리자가 게임을 오픈 상태로 변경합니다.")
    @AdminAuthorize
    @PatchMapping("/{id}/open")
    public Response<Void> publishGame(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        gameService.publishGame(id, user);

        return Response.success();
    }

    @Operation(summary = "게임 정보 수정", description = "관리자가 게임 정보를 수정합니다.")
    @AdminAuthorize
    @PatchMapping("/{id}/info")
    public Response<GameUpdateResponse> updateGameInfo(@PathVariable("id") Long id, @RequestBody GameUpdateRequest request, @AuthenticationPrincipal User user) {
        GameUpdateResponse response = gameService.updateGame(id, request);
        log.info("[게임][어드민] 게임 정보 수정 - {} / by {}", response.getName(), user.getUsername());
        return Response.success(response);
    }

    // 게임 목록 조회 (검색 + 페이징) 카테고리 : 인기상품, 신규게임, 가격
    @Operation(summary = "게임 목록 조회", description = "카테고리, 이름, 가격으로 검색하고 페이징 처리합니다.")
    @GetMapping("/category")
    public Response<Page<GameDetailResponse>> getGamesByCategory(
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "카테고리 (top: 인기순, new: 최신순, hotsale: 할인순)",
                    schema = @Schema(type = "string", allowableValues = {"top", "new", "hotsale"})
            )
            @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "게임 이름 검색 키워드") @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "최소 가격") @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @Parameter(description = "최대 가격") @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log.info("[게임 목록 조회]");
        Page<GameDetailResponse> result = gameService.getGamesByCategory(
                category,
                name,
                minPrice,
                maxPrice,
                page,
                size);

        return Response.success(result);
    }

    @Operation(summary = "게임 이미지 수정", description = "게임 이미지 파일을 업로드하여 수정합니다.")
    @PutMapping("{gameId}/edit/image")
    @AdminAuthorize
    public Response<String> editGamePicture(@PathVariable("gameId") Long gameId,
                                            @NotNull @RequestParam("image") MultipartFile imageFile,
                                            @AuthenticationPrincipal User user) throws IOException {
        String imageUrl = gameService.editGamePicture(gameId, imageFile);

        return Response.success(imageUrl);
    }
}
