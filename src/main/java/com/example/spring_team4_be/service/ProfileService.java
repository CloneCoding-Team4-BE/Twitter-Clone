package com.example.spring_team4_be.service;

import com.example.spring_team4_be.dto.request.ProfileReqDto;
import com.example.spring_team4_be.dto.response.*;
import com.example.spring_team4_be.entity.Follow;
import com.example.spring_team4_be.entity.Heart;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.FollowRepository;
import com.example.spring_team4_be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final S3UploaderService s3UploaderService;
    private final FollowRepository followRepository;

    @Transactional
    public ResponseDto<ProfileResponseDto> updateProfile(ProfileReqDto profileReqDto, MultipartFile profileFile, MultipartFile backgroundFile, HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //AWS
        String profileFileName = null;
        ImageResponseDto imageResponseDto = null;
        if(profileFile == null) {
            imageResponseDto = new ImageResponseDto(member.getImageUrl());
        } else {
            try {
                profileFileName = (String) s3UploaderService.uploadFile(profileFile).getData();
                imageResponseDto = new ImageResponseDto(profileFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String backgroundFileName = null;
        BackImageResponseDto backImageResponseDto = null;
        if(backgroundFile == null) {
            backImageResponseDto = new BackImageResponseDto(member.getBackgroundImageUrl());
        } else {
            try {
                backgroundFileName = (String) s3UploaderService.uploadFile(backgroundFile).getData();
                backImageResponseDto = new BackImageResponseDto(backgroundFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        member.update(profileReqDto, imageResponseDto, backImageResponseDto);
        memberRepository.save(member);

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .memberId(member.getId())
                .imageUrl(member.getImageUrl())
                .backgroundImageUrl(member.getBackgroundImageUrl())
                .userId(member.getUserId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .followerCnt(followRepository.countAllByFollower(member))
                .followingCnt(followRepository.countAllByFollowing(member))
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
                .memberId(member.getId())
                .imageUrl(member.getImageUrl())
                .backgroundImageUrl(member.getBackgroundImageUrl())
                .userId(member.getUserId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .followerCnt(followRepository.countAllByFollower(member))
                .followingCnt(followRepository.countAllByFollowing(member))
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

        Member memberMe = isPresentMember(request.getUserPrincipal().getName());

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .memberId(member.getId())
                .imageUrl(member.getImageUrl())
                .backgroundImageUrl(member.getBackgroundImageUrl())
                .userId(member.getUserId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .followerCnt(followRepository.countAllByFollower(member))
                .followingCnt(followRepository.countAllByFollowing(member))
                .isFollowing(isFollowing(member,memberMe))
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

    @Transactional
    public boolean isFollowing(Member following, Member follower){
        boolean isFollowing = false;
        int followingCnt = followRepository.countAllByFollowerAndFollowing(following, follower);
        System.out.println(followingCnt);
        if(followingCnt == 1)
            isFollowing = true;
        return isFollowing;
    }


}