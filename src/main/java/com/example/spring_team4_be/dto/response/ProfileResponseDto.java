package com.example.spring_team4_be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponseDto {
//    @NotBlank
    private String imageUrl;
    private String backgroundImageUrl;
    @NotBlank
    private String userId;
    @NotBlank
    private String nickname;
//    @NotBlank
    private String bio;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
    @NotBlank
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM d, yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDate dateOfBirth;

    //팔로워 팔로잉
    private int followerCnt;
    private int followingCnt;
}
