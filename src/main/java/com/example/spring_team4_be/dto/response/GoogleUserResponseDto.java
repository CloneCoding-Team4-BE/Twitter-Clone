package com.example.spring_team4_be.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleUserResponseDto {
    private String accessToken;
    private String refreshToken;
}
