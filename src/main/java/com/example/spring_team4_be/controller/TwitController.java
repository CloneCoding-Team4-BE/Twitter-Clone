package com.example.spring_team4_be.controller;


import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.service.TwitService;
import com.example.spring_team4_be.util.PublicMethod;
import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.dto.TwitRequestDto;
import com.example.spring_team4_be.service.TwitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class TwitController {

    private final TwitService twitService;
    private final PublicMethod publicMethod;

    //프로필 내 트윗 목록  조회
    @GetMapping("/mytwit")
    public ResponseDto<?> readMyTwit(HttpServletRequest request){
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();
        return twitService.readMyTwit(member);
    }

    //다른 사용자 프로필 트위 목록 조회
    @GetMapping("/mytwit/{user_id}")
    public ResponseDto<?> readMemberTwit(@PathVariable Long user_id, HttpServletRequest request){
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        return twitService.readMemberTwit(user_id);
    }

    //트윗 상세 조회
    @GetMapping("/twit/{twit_id}")
    public ResponseDto<?> readTwitDetail(@PathVariable Long twit_id, HttpServletRequest request){
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        return twitService.readTwitDetail(twit_id);
    }

    @GetMapping("/twit")
    public ResponseDto<?> allTwit(){
        return twitService.allTwit();
    }


    @PostMapping("/twit")
    public ResponseDto<?> twitCreate(@RequestBody TwitRequestDto requestDto, HttpServletRequest request){
        return twitService.twitCreate(requestDto, request);
    }

    @DeleteMapping("/twit/{twit_id}")
    public ResponseDto<?> twitDelete(@PathVariable Long twit_id, HttpServletRequest request){
        return twitService.twitDelete(twit_id, request);
    }
}
