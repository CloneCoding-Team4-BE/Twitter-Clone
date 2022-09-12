package com.example.spring_team4_be.service;

import com.example.spring_team4_be.dto.reponse.ResponseDto;
import com.example.spring_team4_be.dto.reponse.TwitDetailResponseDto;
import com.example.spring_team4_be.dto.reponse.TwitResponseDto;
import com.example.spring_team4_be.dto.reponse.TwitSimpleResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TwitService {
    private final TwitRepository twitRepository;

    //TODO: 마이페이지 내 트윗 조회
    @Transactional
    public ResponseDto<?> readMyTwit(Member member){
        List<TwitSimpleResponseDto> twitList;
       twitList= twitRepository.findAllTwit(member.getId());
       twitList.addAll(twitRepository.findAllTwitWithReTwit(member.getId()));
        twitList.sort(new Comparator<TwitSimpleResponseDto>() {
            @Override
            public int compare(TwitSimpleResponseDto t0, TwitSimpleResponseDto t1) {
                return t1.getCreatedAt().compareTo(t0.getCreatedAt());
            }
        });
       List<TwitResponseDto> twitResponseDtos = new ArrayList<>();

       for(TwitSimpleResponseDto twit:twitList){
           int commentCont = twitRepository.findAllByReTwit(twit.getTwit().getId()).size();
           twitResponseDtos.add(new TwitResponseDto(twit.getTwit(),commentCont));
       }
        return ResponseDto.success(twitResponseDtos);
    }


    //TODO: 다른 사용자 트윗 조회
    @Transactional
    public ResponseDto<?> readMemberTwit(Long user_id){
        List<TwitSimpleResponseDto> twitList;
        twitList= twitRepository.findAllTwit(user_id);
        twitList.addAll(twitRepository.findAllTwitWithReTwit(user_id));
        twitList.sort(new Comparator<TwitSimpleResponseDto>() {
            @Override
            public int compare(TwitSimpleResponseDto t0, TwitSimpleResponseDto t1) {
                return t1.getCreatedAt().compareTo(t0.getCreatedAt());
            }
        });
        List<TwitResponseDto> twitResponseDtos = new ArrayList<>();

        for(TwitSimpleResponseDto twit:twitList){
            int commentCont = twitRepository.findAllByReTwit(twit.getTwit().getId()).size();
            twitResponseDtos.add(new TwitResponseDto(twit.getTwit(),commentCont));
        }
        return ResponseDto.success(twitResponseDtos);
    }

    //TODO: 트윗 상세조회
    @Transactional
    public ResponseDto<?> readTwitDetail(Long twit_id){
        Optional<Twit> twitOptional = twitRepository.findById(twit_id);
        if(twitOptional.isEmpty()) return ResponseDto.fail("","");
        else {
            Twit twit = twitOptional.get();
            List<Twit> comments = twitRepository.findAllByReTwit(twit.getId());
            List<TwitResponseDto> commentlist = new ArrayList<>();
            for(Twit comment: comments){
                commentlist.add(twitTotwitResponseDto(comment));
            }
            TwitDetailResponseDto twitDetailResponseDto = new TwitDetailResponseDto(twit,commentlist);
            return ResponseDto.success(twitDetailResponseDto);
        }
    }

    public TwitResponseDto twitTotwitResponseDto(Twit twit){
        int commentCont = twitRepository.findAllByReTwit(twit.getId()).size();
        return new TwitResponseDto(twit,commentCont);
    }


}
