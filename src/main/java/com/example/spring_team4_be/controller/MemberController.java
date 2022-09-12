package com.example.spring_team4_be.controller;



import com.example.spring_team4_be.dto.request.LoginReqDto;
import com.example.spring_team4_be.dto.request.MemberReqDto;
import com.example.spring_team4_be.service.MemberService;
import com.example.spring_team4_be.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/member/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestBody @Valid MemberReqDto requestDto) {
        return memberService.createMember(requestDto);
    }


    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto requestDto,
                                HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    @RequestMapping(value = "/member/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }


//    @GetMapping("/issue/token")
//    public GlobalResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
//        //refresh토큰을 주면서 access토큰을 발급하는 API
//        response.addHeader(JwtFilter.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getMember().getUsername(), "Access"));
//        return new GlobalResDto("issuedToken Success", HttpStatus.OK.value());
//    }

}