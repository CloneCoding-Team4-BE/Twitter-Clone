package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.*;
import com.example.spring_team4_be.dto.request.CheckIdReqDto;
import com.example.spring_team4_be.dto.request.LoginReqDto;
import com.example.spring_team4_be.dto.request.MemberReqDto;
import com.example.spring_team4_be.dto.response.MemberResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    @Transactional
    public ResponseDto<?> createMember(MemberReqDto requestDto) {
        if (null != isPresentMember(requestDto.getUserId())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 아이디입니다.");
        }


        Member member = Member.builder()
                .userId(requestDto.getUserId())
                .nickname(requestDto.getNickname())
                .imageUrl(defaultImageAddress)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .dateOfBirth(requestDto.getDateOfBirth())
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .userId(member.getUserId())
                        .nickname(member.getNickname())
                        .imageUrl(member.getImageUrl())
                        .dateOfBirth(member.getDateOfBirth())
                        .createdAt(member.getCreatedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> login(@Valid LoginReqDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getUserId());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }


        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .userId(member.getUserId())
                        .nickname(member.getUserId())
                        .dateOfBirth(member.getDateOfBirth())
                        .createdAt(member.getCreatedAt())
                        .build()
        );
    }

//  @Transactional
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "사용자를 찾을 수 없습니다.");
//    }
//
//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access-Token"));
//    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
//
//    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//
//    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
//    refreshToken.updateValue(tokenDto.getRefreshToken());
//    tokenToHeaders(tokenDto, response);
//    return ResponseDto.success("success");
//  }

    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    public ResponseDto<?> checkId(CheckIdReqDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getUserId());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }else return ResponseDto.success(requestDto);

    }
}
