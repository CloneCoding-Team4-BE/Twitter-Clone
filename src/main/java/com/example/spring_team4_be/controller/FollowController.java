package com.example.spring_team4_be.controller;


import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class FollowController {
    private final FollowService followService;

    @PostMapping("/api/auth/follow/{user_id}")
    public ResponseDto<?> follow(@PathVariable Member user_id, HttpServletRequest request){
        return followService.follow(user_id,request);
    }
    @GetMapping("/api/auth/member/profile/following/{memberId}")
    public ResponseDto<?> getFollowingList(@PathVariable Member memberId, HttpServletRequest request ){
        return followService.getfollowings(memberId,request);
    }

    @GetMapping("/api/auth/member/profile/follower/{memberId}")
    public ResponseDto<?> getFollowerList(@PathVariable Member memberId, HttpServletRequest request ){
        return followService.getfollowers(memberId,request);
    }
}
