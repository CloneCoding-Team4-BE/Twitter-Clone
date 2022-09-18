package com.example.spring_team4_be.service;

import com.example.spring_team4_be.dto.response.ProfileResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.entity.Follow;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;

    private final ProfileService profileService;
    private final TokenProvider tokenProvider;

    // 사용자 팔로우
    @Transactional
    public ResponseDto<?> follow(Member follower, HttpServletRequest request){
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");


        List<Follow> followList = followRepository.findAllByFollowingAndFollower(member,follower);

        for(Follow follow : followList){
            if(follow.getFollowing().equals(member)){
                followRepository.delete(follow);
                log.info("팔로우 취소");
                return ResponseDto.success("팔로우가 취소 되었습니다.");
            }
        }
        Follow follow = Follow.builder()
                .follower(follower)
                .following(member)
                .build();
        followRepository.save(follow);
        log.info("팔로우 성공");
        return ResponseDto.success("팔로우가 등록 되었습니다.");

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public ResponseDto<?> getfollowings(Member memberId, HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        List<Follow> followList = followRepository.findAllByFollowing(memberId);

        List<ProfileResponseDto> results = new ArrayList<>();
        for(Follow follow:followList){
            Member result = follow.getFollower();
            ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                    .memberId(result.getId())
                    .imageUrl(result.getImageUrl())
                    .backgroundImageUrl(result.getBackgroundImageUrl())
                    .userId(result.getUserId())
                    .memberId(result.getId())
                    .nickname(result.getNickname())
                    .bio(result.getBio())
                    .followerCnt(followRepository.countAllByFollower(result))
                    .followingCnt(followRepository.countAllByFollowing(result))
                    .isFollowing(profileService.isFollowing(result, member))
                    .createdAt(result.getCreatedAt())
                    .dateOfBirth(result.getDateOfBirth())
                    .build();
            results.add(profileResponseDto);
        }
        return ResponseDto.success(results);
    }

    public ResponseDto<?> getfollowers(Member memberId, HttpServletRequest request) {
        Member member = validateMember(request);

        if(member == null)
            return ResponseDto.fail("INVALID_TOKEN","토큰이 유효하지 않습니다.");

        if(request.getHeader("Authorization") == null)
            return ResponseDto.fail("MEMBER_NOT_FOUND","로그인이 필요합니다.");

        List<Follow> followerList = followRepository.findAllByFollower(memberId);
        List<ProfileResponseDto> results = new ArrayList<>();
        for(Follow follow:followerList){
            Member result = follow.getFollowing();
            ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                    .memberId(result.getId())
                    .imageUrl(result.getImageUrl())
                    .backgroundImageUrl(result.getBackgroundImageUrl())
                    .userId(result.getUserId())
                    .memberId(result.getId())
                    .nickname(result.getNickname())
                    .bio(result.getBio())
                    .followerCnt(followRepository.countAllByFollower(result))
                    .followingCnt(followRepository.countAllByFollowing(result))
                    .isFollowing(profileService.isFollowing(result, member))
                    .createdAt(result.getCreatedAt())
                    .dateOfBirth(result.getDateOfBirth())
                    .build();
            results.add(profileResponseDto);
        }
        return ResponseDto.success(results);
    }
}
