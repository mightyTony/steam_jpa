package com.example.steam.domain.cart.dto;

import com.example.steam.domain.cart.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(description = "장바구니 항목 응답 DTO")
public class CartViewResponse {
    @Schema(description = "장바구니 항목 ID", example = "1001")
    private final Long id;

    @Schema(description = "게임 ID", example = "1")
    private final Long gameId;

    @Schema(description = "게임 이름", example = "StarCraft")
    private final String name;

    @Schema(description = "정가", example = "19900")
    private final int price;

    @Schema(description = "할인율 (%)", example = "10")
    private final int discount;

    @Schema(description = "최종 가격", example = "17910")
    private final int totalPrice;

    public CartViewResponse (Cart cart) {
        this.id = cart.getId();
        this.gameId = cart.getGame().getId();
        this.name = cart.getGame().getName();
        this.price = cart.getGame().getPrice();
        this.discount = cart.getGame().getDiscount();
        this.totalPrice = cart.getGame().getTotalPrice();
    }
}
