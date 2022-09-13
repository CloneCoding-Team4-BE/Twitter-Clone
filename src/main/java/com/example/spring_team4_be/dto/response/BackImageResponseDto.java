package com.example.spring_team4_be.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BackImageResponseDto {
    private String backgroundImageUrl;

    public BackImageResponseDto(String backgroundImageUrl){
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
