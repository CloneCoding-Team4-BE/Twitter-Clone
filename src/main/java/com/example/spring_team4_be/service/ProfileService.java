package com.example.spring_team4_be.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.spring_team4_be.dto.request.ProfileReqDto;
import com.example.spring_team4_be.dto.response.ImageResponseDto;
import com.example.spring_team4_be.dto.response.ProfileResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public ResponseDto<ProfileResponseDto> updateProfile(ProfileReqDto profileReqDto, MultipartFile multipartFile, HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //AWS
        String FileName = null;
        ImageResponseDto imageResponseDto = null;
        if(multipartFile == null) {
            imageResponseDto = new ImageResponseDto(member.getImageUrl());
        } else {
            try {
                FileName = s3UploaderService.uploadFile(multipartFile, "image");
                imageResponseDto = new ImageResponseDto(FileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        member.update(profileReqDto, imageResponseDto);
        memberRepository.save(member);

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .imageUrl(member.getImageUrl())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .createdAt(member.getCreatedAt())
                .dateOfBirth(member.getDateOfBirth())
                .build();

        return ResponseDto.success(profileResponseDto);
    }

    @Transactional(readOnly = true)
    public ResponseDto<ProfileResponseDto> getMyProfile(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = isPresentMember(request.getUserPrincipal().getName());

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .imageUrl(member.getImageUrl())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .createdAt(member.getCreatedAt())
                .dateOfBirth(member.getDateOfBirth())
                .build();

        return ResponseDto.success(profileResponseDto);
    }

    @Transactional(readOnly = true)
    public ResponseDto<ProfileResponseDto> getProfile(String userId, HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = isPresentMember(userId);
        if (member == null) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 사용자입니다.");
        }

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .imageUrl(member.getImageUrl())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .createdAt(member.getCreatedAt())
                .dateOfBirth(member.getDateOfBirth())
                .build();

        return ResponseDto.success(profileResponseDto);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String userId){
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}