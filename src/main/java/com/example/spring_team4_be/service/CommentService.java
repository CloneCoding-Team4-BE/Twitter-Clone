package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.dto.TwitRequestDto;
import com.example.spring_team4_be.dto.TwitResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final TwitRepository twitRepository;
    private final ReTwitService reTwitService;
    private final TwitService twitService;
    private final HeartService heartService;
    @Transactional
    public ResponseDto<?> create(Long twit_id, TwitRequestDto requestDto, HttpServletRequest request){
        Member member = reTwitService.validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN", "토큰이 유효하지 않습니다.");

        if(twitService.isPresentTwit(twit_id) == null) {
            return ResponseDto.fail("NOT_FOUNT_TWIT", "트윗을 찾을 수 없습니다.");
        }
        Twit twit = Twit.builder()
                .content(requestDto.getContent())
                .url(requestDto.getUrl())
                .reTwit(twit_id)
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
                        .commentCnt(heartService.commentcnt(twit.getId()))
                        .likeCnt(heartService.commentcnt(twit.getId()))
                        .build()
        );
    }


}
