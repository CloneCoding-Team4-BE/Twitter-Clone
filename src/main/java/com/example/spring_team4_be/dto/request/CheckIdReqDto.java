package com.example.spring_team4_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckIdReqDto {
    @NotBlank
    private String userId;
}
