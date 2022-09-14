package com.example.spring_team4_be.service;

import com.example.spring_team4_be.dto.response.HeartResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.entity.Heart;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.HeartRepository;
import com.example.spring_team4_be.repository.ReTwitRepository;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class HeartService {
    private final HeartRepository heartRepository;
    private final TwitRepository twitRepository;
    private final TokenProvider tokenProvider;
    private final ReTwitRepository reTwitRepository;


    @Transactional
    public int heartcnt(Long id) {
        return heartRepository.countAllByTwitId(id);
    }

    @Transactional
    public int commentcnt(Long twit_id){return twitRepository.countAllByReTwit(twit_id);}
    @Transactional
    public int reTwitcnt(Long twit_id){return reTwitRepository.countAllByTwitId(twit_id);}


    // 트윗 좋아요
    @Transactional
    public ResponseDto<?> likeAndUnlike(HttpServletRequest request, Long twit_id) {
        Member member = validateMember(request);

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        if(member == null){
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다");
        }

        Twit twit = isPresentTwit(twit_id);

        List<Heart> heartList = heartRepository.findByMemberIdAndTwitId(member.getId(), twit_id);


        for(Heart heart : heartList){
            if(heart.getMember().equals(member)){
                heartRepository.delete(heart);
                log.info("좋아요 취소");
                return ResponseDto.success("좋아요가 취소 되었습니다.");
            }
        }

        Heart heart = Heart.builder()
                        .member(member)
                        .twit(twit)
                        .build();
        heartRepository.save(heart);
        log.info("좋아요 등록.");
        return ResponseDto.success("좋아요가 등록 되었습니다.");

    }

    // 사용자가 좋아요한 트윗 전체 조회
    @Transactional
    public ResponseDto<?> getLikeTwit(Long member_id,HttpServletRequest request){
        Member member = validateMember(request);

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        if(member == null){
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다");
        }

        List<Heart> heartList = heartRepository.findByMemberId(member_id);


        List<HeartResponseDto> heartResponseDtoList = new ArrayList<>();
        for(Heart heart : heartList){

            heartResponseDtoList.add(
                    HeartResponseDto.builder()
                    .id(heart.getTwit().getId())
                    .userProfileImage(heart.getMember().getImageUrl())
                    .nickname(heart.getMember().getNickname())
                    .userId(heart.getMember().getUserId())
                    .memberId(heart.getMember().getId())
                    .content(heart.getTwit().getContent())
                    .fileUrl(heart.getTwit().getUrl())
                    .commentCnt(commentcnt(heart.getTwit().getId()))
                    .createdAt(heart.getTwit().getCreatedAt())
                    .likeCnt(heartcnt(heart.getTwit().getId()))
                    .reTwitCnt(reTwitcnt(heart.getTwit().getId()))
                    .isLike(isLike(member,heart.getTwit().getId()))
                    .isRetweet(isRetweet(member,heart.getTwit().getId()))
                    .build()
            );
        }


        return ResponseDto.success(heartResponseDtoList);

    }

    @Transactional
    public boolean isLike(Member member,Long twit_id){
        boolean isLike = false;
        int LikeCnt = heartRepository.countByMemberAndTwitId(member,twit_id);
        if(LikeCnt == 1)
            isLike = true;
        return isLike;
    }

    @Transactional
    public boolean isRetweet(Member member,Long twit_id){
        boolean isRetweet = false;
        int RetweetCnt = reTwitRepository.countByMemberAndTwitId(member,twit_id);
        if(RetweetCnt == 1)
            isRetweet = true;
        return isRetweet;
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
