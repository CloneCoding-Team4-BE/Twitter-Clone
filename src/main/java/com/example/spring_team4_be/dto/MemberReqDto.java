package com.example.spring_team4_be.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberReqDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
