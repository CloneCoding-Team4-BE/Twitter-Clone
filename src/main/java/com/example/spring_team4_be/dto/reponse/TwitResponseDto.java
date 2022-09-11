package com.example.spring_team4_be.dto.reponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwitResponseDto {
    private Long twitId;
    private String userProfileImage;
    private String nickname;
    private Long userId;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int likeCnt;

    public TwitResponseDto(Long userId, String nickname, String userProfileImage, Long twitId, LocalDateTime createdAt, String content, String fileUrl){
        this.userId = userId;
        this.nickname = nickname;
        this.userProfileImage = userProfileImage;
        this.twitId = twitId;
        this.createdAt = createdAt;
        this.content = content;
        this.fileUrl = fileUrl;
    }
}
