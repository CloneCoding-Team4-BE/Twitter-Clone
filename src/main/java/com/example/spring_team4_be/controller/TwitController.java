package com.example.spring_team4_be.controller;


import com.example.spring_team4_be.dto.request.TwitRequestDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.service.TwitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/auth/twit")
@RestController
public class TwitController {

    private final TwitService twitService;


    @GetMapping
    public ResponseDto<?> allTwit(){
        return twitService.allTwit();
    }


    @PostMapping
    public ResponseDto<?> twitCreate(@RequestPart(required = false) MultipartFile multipartFile, @RequestPart TwitRequestDto requestDto, HttpServletRequest request){
        return twitService.twitCreate(multipartFile,requestDto, request);
    }

    @DeleteMapping("/{twit_id}")
    public ResponseDto<?> twitDelete(@PathVariable Long twit_id, HttpServletRequest request){
        return twitService.twitDelete(twit_id, request);
    }
}
