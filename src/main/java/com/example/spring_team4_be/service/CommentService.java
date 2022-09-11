package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.ResponseDto;
import com.example.spring_team4_be.dto.TwitRequestDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final TwitRepository twitRepository;
    private final ReTwitService reTwitService;
    @Transactional
    public ResponseDto<?> create(Long id, TwitRequestDto requestDto, HttpServletRequest request){
        Member member = reTwitService.validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN", "토큰이 유효하지 않습니다.");

        Optional<Twit> parentTwit = twitRepository.findById(id);





        return ResponseDto.success("");
    }
}
