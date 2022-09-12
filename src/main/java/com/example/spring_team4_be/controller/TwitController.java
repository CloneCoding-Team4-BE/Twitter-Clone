package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.reponse.ResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.service.TwitService;
import com.example.spring_team4_be.util.PublicMethod;
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

}
