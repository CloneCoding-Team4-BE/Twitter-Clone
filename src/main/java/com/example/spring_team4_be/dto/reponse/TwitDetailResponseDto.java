package com.example.spring_team4_be.dto.reponse;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwitDetailResponseDto {
    private String userProfileImage;
    private String nickname;
    private Long userId;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int likeCnt;
    private List<TwitResponseDto> commentList;
}
