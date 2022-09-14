package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.request.TwitRequestDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.dto.response.TwitDetailResponseDto;
import com.example.spring_team4_be.dto.response.TwitResponseDto;
import com.example.spring_team4_be.dto.response.TwitSimpleResponseDto;
import com.example.spring_team4_be.dto.response.*;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TwitService {
    private final TwitRepository twitRepository;
    private final TokenProvider tokenProvider;
    private final HeartService heartService;
    private final S3UploaderService awsS3Service;
    //마이페이지 내 트윗 조회
    @Transactional
    public ResponseDto<?> readMyTwit(Member member, Pageable pageable){
        List<TwitSimpleResponseDto> twitList;
       twitList= twitRepository.findAllTwit(member.getId());
       twitList.addAll(twitRepository.findAllTwitWithReTwit(member.getId()));

       pageable.getPageSize();
       pageable.getPageNumber();
        twitList.sort(new Comparator<TwitSimpleResponseDto>() {
            @Override
            public int compare(TwitSimpleResponseDto t0, TwitSimpleResponseDto t1) {
                return t1.getCreatedAt().compareTo(t0.getCreatedAt());
            }
        });
       List<TwitResponseDto> twitResponseDtos = new ArrayList<>();

       int count = 0;
       for(TwitSimpleResponseDto twit:twitList){
           int commentCont = twitRepository.findAllByReTwit(twit.getTwit().getId()).size();
           twitResponseDtos.add(new TwitResponseDto(twit.getTwit(),commentCont));
           if(twitResponseDtos.size()==pageable.getPageSize()){
               if(count==pageable.getPageNumber()){
                   break;
               }else{
                   count++;
                   twitResponseDtos.clear();
               }
           }
       }
       if(count!=pageable.getPageNumber()) twitResponseDtos.clear();

        return ResponseDto.success(twitResponseDtos);
    }



    //다른 사용자 트윗 조회
    @Transactional
    public ResponseDto<?> readMemberTwit(Long user_id, Pageable pageable){
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

        int count =0;
        for(TwitSimpleResponseDto twit:twitList){
            int commentCont = twitRepository.findAllByReTwit(twit.getTwit().getId()).size();
            twitResponseDtos.add(new TwitResponseDto(twit.getTwit(),commentCont));
            if(twitResponseDtos.size()==pageable.getPageSize()){
                if(count==pageable.getPageNumber()){
                    break;
                }else{
                    count++;
                    twitResponseDtos.clear();
                }
            }
        }
        if(count!=pageable.getPageNumber()) twitResponseDtos.clear();
        return ResponseDto.success(twitResponseDtos);
    }

    //트윗 상세조회
    @Transactional
    public ResponseDto<?> readTwitDetail(Long twit_id){
        Optional<Twit> twitOptional = twitRepository.findById(twit_id);
        if(twitOptional.isEmpty()) return ResponseDto.fail("NOT_FOUND_TWEET","트윗이 존재하지 않습니다.");
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

    //트윗 상위객체 목록 조회
    @Transactional
    public ResponseDto<?> readParentTwit(Long twit_id){
        Optional<Twit> twitOptional = twitRepository.findById(twit_id);
        if(twitOptional.isEmpty()) return ResponseDto.fail("NOT_FOUND_TWEET","트윗이 존재하지 않습니다.");
        else {
            List<TwitResponseDto> parentList = new ArrayList<>();
            Twit twit = twitOptional.get();
            Long tid = twit.getReTwit();
            while (tid!=null){
                Optional<Twit> tempOptional = twitRepository.findById(twit.getReTwit());

                if(tempOptional.isEmpty()) break;
                else {
                    Twit temp = tempOptional.get();
                    parentList.add(twitTotwitResponseDto(temp));
                    tid = temp.getReTwit();
                }

            }
            if(parentList.size()==0) return ResponseDto.fail("NOT_FOUND_PARENT","상위 트윗이 없습니다.");
            return ResponseDto.success(parentList);
        }
    }


    public TwitResponseDto twitTotwitResponseDto(Twit twit) {
        int commentCont = twitRepository.findAllByReTwit(twit.getId()).size();
        return new TwitResponseDto(twit, commentCont);
    }


    @Transactional
    public ResponseDto<?> allTwit(Pageable pageable) {

        List<Twit> twitList = twitRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<TwitResponseDto> twits = new ArrayList<>();

        for(Twit twit : twitList){
            twits.add(
                    TwitResponseDto.builder()
                            .id(twit.getId())
                            .userProfileImage(twit.getMember().getImageUrl())
                            .nickname(twit.getMember().getNickname())
                            .userId(twit.getMember().getUserId())
                            .memberId(twit.getMember().getId())
                            .content(twit.getContent())
                            .fileUrl(twit.getUrl())
                            .createdAt(twit.getCreatedAt())
                            .commentCnt(heartService.commentcnt(twit.getId()))
                            .likeCnt(heartService.heartcnt(twit.getId()))
                            .retwitCnt(heartService.reTwitcnt(twit.getId()))
                            .build()
            );
        }



        return ResponseDto.success(twits);
    }


    @Transactional
    public ResponseDto<?> twitCreate(MultipartFile multipartFile, TwitRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");


        //AWS
        String FileName = null;
        ImageResponseDto imageResponseDto = null;
        if(multipartFile == null) {
            imageResponseDto = new ImageResponseDto(FileName);
        } else {
            try {
                FileName = (String) awsS3Service.uploadFile(multipartFile).getData();
                imageResponseDto = new ImageResponseDto(FileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String content = requestDto==null? null : requestDto.getContent();
        Twit twit = Twit.builder()
                .content(content)
                .url(imageResponseDto.getImageUrl())
                .member(member)
                .build();
        twitRepository.save(twit);


        return ResponseDto.success(
                TwitResponseDto.builder()
                        .id(twit.getId())
                        .userProfileImage(member.getImageUrl())
                        .nickname(member.getNickname())
                        .memberId(member.getId())
                        .userId(member.getUserId())
                        .content(twit.getContent())
                        .fileUrl(twit.getUrl())
                        .createdAt(twit.getCreatedAt())
                        .commentCnt(heartService.commentcnt(twit.getId()))
                        .likeCnt(heartService.heartcnt(twit.getId()))
                        .retwitCnt(heartService.reTwitcnt(twit.getId()))
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
