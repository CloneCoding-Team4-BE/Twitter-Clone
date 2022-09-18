package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.request.ProfileReqDto;
import com.example.spring_team4_be.dto.response.ProfileResponseDto;
import com.example.spring_team4_be.dto.response.ResponseDto;
import com.example.spring_team4_be.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @RequestMapping(value = "/member/profile", method = RequestMethod.PUT)
    public ResponseDto<ProfileResponseDto> updateProfile(@RequestPart ProfileReqDto profileReqDto, @RequestPart(required = false) MultipartFile profileFile, @RequestPart(required = false) MultipartFile backgroundFile, HttpServletRequest request) {
        return profileService.updateProfile(profileReqDto, profileFile, backgroundFile, request);
    }

    @RequestMapping(value = "/member/profile", method = RequestMethod.GET)
    public ResponseDto<ProfileResponseDto> getMyProfile(HttpServletRequest request) {
        return profileService.getMyProfile(request);
    }

    @RequestMapping(value = "/member/profile/{userId}", method = RequestMethod.GET)
    public ResponseDto<ProfileResponseDto> getProfile(@PathVariable String userId, HttpServletRequest request) {
        return profileService.getProfile(userId, request);
    }



}
