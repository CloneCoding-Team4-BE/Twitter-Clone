package com.example.spring_team4_be.service;


import com.example.spring_team4_be.dto.response.ImageResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.dto.request.TwitRequestDto;
import com.example.spring_team4_be.dto.response.TwitResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.entity.Twit;
import com.example.spring_team4_be.repository.TwitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final TwitRepository twitRepository;
    private final ReTwitService reTwitService;
    private final TwitService twitService;
    private final HeartService heartService;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public ResponseDto<?> create(Long twit_id, TwitRequestDto requestDto, MultipartFile file, HttpServletRequest request){
        Member member = reTwitService.validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN", "토큰이 유효하지 않습니다.");

        if(twitService.isPresentTwit(twit_id) == null) {
            return ResponseDto.fail("NOT_FOUNT_TWIT", "트윗을 찾을 수 없습니다.");
        }

        //AWS
        String FileName = null;
        ImageResponseDto imageResponseDto = null;
        if(file == null) {
            imageResponseDto = new ImageResponseDto(FileName);
        } else {
            try {
                FileName = s3UploaderService.uploadFile(file, "image");
                imageResponseDto = new ImageResponseDto(FileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Twit twit = Twit.builder()
                .content(requestDto.getContent())
                .url(imageResponseDto.getImageUrl())
                .reTwit(twit_id)
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


}
