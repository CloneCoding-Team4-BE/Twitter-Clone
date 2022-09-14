package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.dto.GoogleLoginDto;
import com.example.spring_team4_be.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/google")
public class GoogleController {
    private final GoogleService googleService;


    @GetMapping("/login")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        return googleService.moveGoogleInitUrl();
    }

    @GetMapping("/login/redirect")
    public ResponseEntity<GoogleLoginDto> redirectGoogleLogin(@RequestParam(value = "code")String authCode){
        return googleService.redirectGoogleLogin(authCode);
    }
}
