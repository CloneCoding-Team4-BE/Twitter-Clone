package com.example.spring_team4_be.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class TwitRequestDto {
    private String content;
    private MultipartFile file;
}
