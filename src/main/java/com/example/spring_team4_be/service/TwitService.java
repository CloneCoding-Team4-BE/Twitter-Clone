package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.dto.request.TwitRequestDto;
import com.example.spring_team4_be.dto.response.TwitResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class TwitService {
    private final TwitRepository twitRepository;
    private final TokenProvider tokenProvider;
    private final HeartService heartService;

    @Transactional
    public ResponseDto<?> allTwit() {

        List<Twit> twitList = twitRepository.findAllByOrderByCreatedAtDesc();

        List<TwitResponseDto> twits = new ArrayList<>();

        for(Twit twit : twitList){
            twits.add(
                    TwitResponseDto.builder()
                            .id(twit.getId())
                            .userFrofileImage(twit.getMember().getImageUrl())
                            .nickname(twit.getMember().getNickname())
                            .userId(twit.getMember().getUserId())
                            .content(twit.getContent())
                            .fileUrl(twit.getUrl())
                            .createdAt(twit.getCreatedAt())
                            .commentCnt(heartService.commentcnt(twit.getId()))
                            .likeCnt(heartService.commentcnt(twit.getId()))
                            .build()
            );
        }



        return ResponseDto.success(twits);
    }


    @Transactional
    public ResponseDto<?> twitCreate(TwitRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        Twit twit = Twit.builder()
                .content(requestDto.getContent())
//                .url(requestDto.getUrl())
                .member(member)
                .build();
        twitRepository.save(twit);


        return ResponseDto.success(
                TwitResponseDto.builder()
                        .id(twit.getId())
                        .userFrofileImage(member.getImageUrl())
                        .nickname(member.getNickname())
                        .userId(member.getUserId())
                        .content(twit.getContent())
                        .fileUrl(twit.getUrl())
                        .createdAt(twit.getCreatedAt())
                        .commentCnt(heartService.commentcnt(twit.getId()))
                        .likeCnt(heartService.commentcnt(twit.getId()))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> twitDelete(Long twit_id, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Twit twit = isPresentTwit(twit_id);
        if(twit == null)
            return ResponseDto.fail("NOT_FOUND_REVIEW", "리뷰가 존재하지 않습니다.");

        if(twit.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST","작성자만 삭제할 수 있습니다.");
        }

        twitRepository.deleteById(twit_id);
        return ResponseDto.success("");

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Twit isPresentTwit(Long id) {
        Optional<Twit> optionalBook_review = twitRepository.findById(id);
        return optionalBook_review.orElse(null);
    }


}
