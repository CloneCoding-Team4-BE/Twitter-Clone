package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.dto.TwitRequestDto;
import com.example.spring_team4_be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/auth/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{id}")
    public ResponseDto<?> CommentCreate(@PathVariable Long id , @RequestBody TwitRequestDto requestDto, HttpServletRequest request){
        return commentService.create(id,requestDto,request);
    }
}
