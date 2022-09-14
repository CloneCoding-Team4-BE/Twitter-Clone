package com.example.spring_team4_be.controller;

import com.example.spring_team4_be.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/google")
public class GoogleController {
    private final GoogleService googleService;


    @GetMapping("/login")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        return googleService.moveGoogleInitUrl();
    }

    @PostMapping("/login/redirect")
    public void redirectGoogleLogin(@RequestParam(value = "code")String authCode, HttpServletResponse response) throws IOException {
        googleService.redirectGoogleLogin(authCode,response);
        response.sendRedirect("http://localhost:3000/oauth");
    }
}
