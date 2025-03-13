package com.example.steam.domain.cart.dto;

import com.example.steam.domain.cart.Cart;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CartViewResponse {
    private final Long id;
    private final Long gameId;
    private final String name;
    private final int price;
    private final int discount;
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
