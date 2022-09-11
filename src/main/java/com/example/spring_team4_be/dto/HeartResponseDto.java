package com.example.spring_team4_be.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HeartResponseDto {
    private String userFrofileImage;
    private String nickname;
    private String userId;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int likeCnt;
    private LocalDateTime createdAt;
}
