package com.example.spring_team4_be.controller;


import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.service.ReTwitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ReTwitController {
    private final ReTwitService reTwitService;

    @PostMapping("/api/auth/retwit/{twit_id}")
    public ResponseDto<?> reTwitAndUnreTwit(@PathVariable Long twit_id, HttpServletRequest request){
        return reTwitService.reTwitAndUnreTwit(twit_id,request);
    }
}
