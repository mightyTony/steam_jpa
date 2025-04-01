package com.example.steam.domain.profile.friendship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum FriendStatus {
    @Schema(description = "친구 요청 보낸 상태") PENDING,
    @Schema(description = "친구 수락된 상태") ACCEPTED,
    @Schema(description = "거절 혹은 삭제된 상태") REJECTED
}
