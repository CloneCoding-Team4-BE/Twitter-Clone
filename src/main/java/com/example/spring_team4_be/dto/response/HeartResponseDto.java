package com.example.spring_team4_be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HeartResponseDto {
    private String userProfileImage;
    private String nickname;
    private String userId;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int likeCnt;
    private int reTwitCnt;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMM d", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
}
