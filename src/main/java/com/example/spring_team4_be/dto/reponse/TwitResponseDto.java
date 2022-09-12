package com.example.spring_team4_be.dto.reponse;

import com.example.spring_team4_be.entity.Twit;
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
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM dd", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int retwitCnt;
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

    public TwitResponseDto(Twit twit, int commentCnt){
        this.twitId = twit.getId();
        this.content = twit.getContent();
        this.fileUrl = twit.getUrl();
        this.userId = twit.getMember().getId();
        this.nickname = twit.getMember().getNickname();
        this.createdAt = twit.getCreatedAt();
        this.userProfileImage = twit.getUrl();
        this.retwitCnt = twit.getReTwits().size();
        this.likeCnt = twit.getHearts().size();
        this.commentCnt = commentCnt;
    }
}
