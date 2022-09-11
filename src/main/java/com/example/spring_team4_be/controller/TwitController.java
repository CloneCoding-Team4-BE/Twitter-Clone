package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.dto.TwitRequestDto;
import com.example.spring_team4_be.service.TwitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ResponseDto<?> twitCreate(@RequestBody TwitRequestDto requestDto, HttpServletRequest request){
        return twitService.twitCreate(requestDto, request);
    }

    @DeleteMapping("/{twit_id}")
    public ResponseDto<?> twitDelete(@PathVariable Long twit_id, HttpServletRequest request){
        return twitService.twitDelete(twit_id, request);
    }
}
