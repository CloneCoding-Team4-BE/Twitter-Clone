package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.dto.TwitRequestDto;
import com.example.spring_team4_be.dto.TwitResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Service
public class TwitService {
    private final TwitRepository twitRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> twitCreate(TwitRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        Twit twit = Twit.builder()
                .content(requestDto.getContent())
                .url(requestDto.getUrl())
                .member(member)
                .build();
        twitRepository.save(twit);


        return ResponseDto.success(
                TwitResponseDto.builder()
                        .id(twit.getId())
                        .userFrofileImage(member.getImageUrl())
                        .nickname(member.getNickname())
                        .userId(member.getUsername())
                        .content(twit.getContent())
                        .fileUrl(twit.getUrl())
                        .createdAt(twit.getCreatedAt())
                        .build()
        );
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
