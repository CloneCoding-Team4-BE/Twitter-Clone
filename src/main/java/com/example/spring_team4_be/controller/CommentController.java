package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.dto.request.TwitRequestDto;
import com.example.spring_team4_be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/auth/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{id}")
    public ResponseDto<?> CommentCreate(@PathVariable Long id , @RequestPart TwitRequestDto requestDto, @RequestPart(required = false) MultipartFile multipartFile, HttpServletRequest request){
        return commentService.create(id,requestDto,multipartFile,request);
    }
}
