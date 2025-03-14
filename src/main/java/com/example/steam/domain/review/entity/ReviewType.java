package com.example.steam.domain.review.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReviewType {
    POSITIVE("추천"), NEGATIVE("비추천");
    private final String displayKoreanName;

    @JsonValue
    public String getDisplayKoreanName() {
        return displayKoreanName;
    }

    ReviewType(String displayKoreanName) {
        this.displayKoreanName = displayKoreanName;
    }
}
