package com.example.spring_team4_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private String imageUrl;

    public ImageResponseDto(String imageUrl){
        this.imageUrl = imageUrl;
    }

}