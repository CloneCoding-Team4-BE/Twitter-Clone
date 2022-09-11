package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.ReTwit;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.ReTwitRepository;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReTwitService {
    private final TwitRepository twitRepository;
    private final ReTwitRepository reTwitRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public ResponseDto<?> reTwitAndUnreTwit(Long twit_id, HttpServletRequest request){
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        Twit twit = isPresentTwit(twit_id);

        List<ReTwit> reTwitList = reTwitRepository.findAllByMemberIdAndTwitId(member.getId(),twit_id);

        for(ReTwit reTwit : reTwitList){
            if(reTwit.getMember().equals(member)){
                reTwitRepository.delete(reTwit);
                log.info("리트윗 취소");
                return ResponseDto.success("리트윗이 취소 되었습니다.");
            }
        }

        ReTwit reTwit = ReTwit.builder()
                .member(member)
                .twit(twit)
                .build();
        reTwitRepository.save(reTwit);
        log.info("리트윗 성공");
        return ResponseDto.success("리트윗이 등록 되었습니다.");

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
