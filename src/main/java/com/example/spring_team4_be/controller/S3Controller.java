package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.response.ImageResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3UploaderService s3Uploader;

    //@RequestParam("image") MultipartFile multipartFile
    //@RequestPart(value = "file") MultipartFile multipartFile
    @PostMapping("/api/auth/image")
    public ResponseDto<?> imageUpload(@RequestPart(value = "file") MultipartFile multipartFile){

        if(multipartFile.isEmpty()){
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }
        try{
            return ResponseDto.success(new ImageResponseDto(s3Uploader.uploadFile(multipartFile,"image")) );
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }

    }
}