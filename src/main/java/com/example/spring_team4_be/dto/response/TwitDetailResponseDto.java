package com.example.spring_team4_be.dto.response;


import com.example.spring_team4_be.entity.Twit;
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
    private Long twitId;
    private String userProfileImage;
    private String nickname;
    private String userId;
    private Long memberId;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="h:mm a · MMM dd, YYYY", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    private String content;
    private String fileUrl;
    private int commentCnt;
    private int retwitCnt;
    private int likeCnt;

    private boolean isLike;
    private boolean isRetweet;
    private List<TwitResponseDto> commentList;

    public TwitDetailResponseDto(Long memberId,String userId, String nickname, String userProfileImage, Long twitId, LocalDateTime createdAt, String content, String fileUrl){
        this.memberId = memberId;
        this.userId = userId;
        this.nickname = nickname;
        this.userProfileImage = userProfileImage;
        this.twitId = twitId;
        this.createdAt = createdAt;
        this.content = content;
        this.fileUrl = fileUrl;
    }

    public TwitDetailResponseDto(Twit twit,List<TwitResponseDto> comments, boolean isLike , boolean isRetweet){
        this.memberId = twit.getMember().getId();
        this.userId = twit.getMember().getUserId();
        this.nickname = twit.getMember().getNickname();
        this.userProfileImage = twit.getMember().getImageUrl();
        this.twitId = twit.getId();
        this.createdAt = twit.getCreatedAt();
        this.content = twit.getContent();
        this.fileUrl = twit.getUrl();
        this.commentList= comments;
        this.commentCnt = comments.size();
        this.retwitCnt = twit.getReTwits().size();
        this.likeCnt = twit.getHearts().size();
        this.isLike  = isLike;
        this.isRetweet  = isRetweet;
    }
}
