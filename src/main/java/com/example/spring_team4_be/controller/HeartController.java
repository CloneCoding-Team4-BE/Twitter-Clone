package com.example.spring_team4_be.controller;


import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class HeartController {
    private final HeartService heartService;

    @GetMapping("/likepage/{member_id}")
    public ResponseDto<?> getLikeTwit(@PathVariable Long member_id, HttpServletRequest request){
        return heartService.getLikeTwit(member_id, request);
    }

    @PostMapping("/twitlike/{twit_id}")
    public ResponseDto<?> likeAndUnlike(@PathVariable Long twit_id, HttpServletRequest request) {
        return heartService.likeAndUnlike(request, twit_id);

    }
}
