package com.example.steam.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderSelectedGamesRequest {
    List<Long> selectedGameIds;
}
