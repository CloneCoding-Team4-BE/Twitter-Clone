package com.example.spring_team4_be.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class MemberReqDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    private LocalDate dateOfBirth;
}

