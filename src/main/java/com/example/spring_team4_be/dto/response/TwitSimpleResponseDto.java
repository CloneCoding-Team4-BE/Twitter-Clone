package com.example.spring_team4_be.dto.response;

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
public class TwitSimpleResponseDto {
    private Twit twit;
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MMMM yyyy", timezone = "Asia/Seoul",  locale = "en")
    private LocalDateTime createdAt;
}
