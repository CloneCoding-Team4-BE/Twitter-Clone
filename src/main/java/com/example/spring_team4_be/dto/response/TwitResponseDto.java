package com.example.spring_team4_be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import com.example.spring_team4_be.entity.Twit;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwitResponseDto {
    private Long id;
    private String userProfileImage;
    private String nickname;
    private String userId;
    private Long memberId;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMM d", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int retwitCnt;
    private int likeCnt;

    public TwitResponseDto(Long memberId, String userId, String nickname, String userProfileImage, Long twitId, LocalDateTime createdAt, String content, String fileUrl){
        this.memberId = memberId;
        this.userId = userId;
        this.nickname = nickname;
        this.userProfileImage = userProfileImage;
        this.id = twitId;
        this.createdAt = createdAt;
        this.content = content;
        this.fileUrl = fileUrl;
    }

    public TwitResponseDto(Twit twit, int commentCnt){
        this.id = twit.getId();
        this.content = twit.getContent();
        this.fileUrl = twit.getUrl();
        this.memberId = twit.getMember().getId();
        this.userId = twit.getMember().getUserId();
        this.nickname = twit.getMember().getNickname();
        this.createdAt = twit.getCreatedAt();
        this.userProfileImage = twit.getUrl();
        this.retwitCnt = twit.getReTwits().size();
        this.likeCnt = twit.getHearts().size();
        this.commentCnt = commentCnt;
    }
}
