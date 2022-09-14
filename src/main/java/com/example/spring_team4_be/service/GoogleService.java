package com.example.spring_team4_be.service;

import com.example.spring_team4_be.dto.GoogleLoginDto;
import com.example.spring_team4_be.dto.TokenDto;
import com.example.spring_team4_be.dto.request.GoogleLoginRequestDto;
import com.example.spring_team4_be.dto.response.GoogleLoginResponseDto;
import com.example.spring_team4_be.entity.Google;
import com.example.spring_team4_be.entity.Member;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.repository.GoogleRepository;
import com.example.spring_team4_be.repository.MemberRepository;
import com.example.spring_team4_be.util.ConfigUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;


@RequiredArgsConstructor
@Service
public class GoogleService {
    private final ConfigUtils configUtils;
    private final GoogleRepository googleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    public ResponseEntity<Object> moveGoogleInitUrl() {


        String authUrl = configUtils.googleInitUrl();
        URI redirectUri = null;

        try {
                redirectUri = new URI(authUrl);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(redirectUri);
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<GoogleLoginDto> redirectGoogleLogin(String authCode, HttpServletResponse response) {
        // HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        GoogleLoginRequestDto requestparams = GoogleLoginRequestDto.builder()
                .clientId(configUtils.getGoogleClientId())
                .clientSecret(configUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(configUtils.getGoogleRedirectUrl())
                .grantType("authorization_code")
                .build();

        try{
            // Http header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleLoginRequestDto> httpRequestEntity = new HttpEntity<>(requestparams,headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getGoogleAuthUrl()
                    + "/token" , httpRequestEntity, String.class);

            //ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponseDto googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponseDto>(){});

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getGoogleAuthUrl()+ "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if(resultJson != null) {
                GoogleLoginDto userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {});
                Google google = new Google(userInfoDto.getEmail());
                String[] userEmail = userInfoDto.getEmail().split("@");
                String userId = userEmail[0];
                if(googleRepository.findByEmail(google.getEmail()).isEmpty()) {
                    googleRepository.save(google);
                    Member member = Member.builder()
                            .userId(userId)
                            .nickname(userInfoDto.getName())
                            .imageUrl(userInfoDto.getPicture())
                            .password(passwordEncoder.encode(""))
                            .dateOfBirth(LocalDate.ofEpochDay(1998-03-20))
                            .build();
                    memberRepository.save(member);
                }

                Member googlemember = memberService.isPresentMember(userId);

                TokenDto tokenDto = tokenProvider.generateTokenDto(googlemember);
                memberService.tokenToHeaders(tokenDto, response);

                return ResponseEntity.ok().body(userInfoDto);
            }
            else {
                throw new Exception("Google OAuth failed!");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }
}
