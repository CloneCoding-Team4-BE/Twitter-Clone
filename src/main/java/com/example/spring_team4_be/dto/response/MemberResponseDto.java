package com.example.spring_team4_be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String userId;
    private String nickname;
    private String imageUrl;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM d, yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDate dateOfBirth;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
}

