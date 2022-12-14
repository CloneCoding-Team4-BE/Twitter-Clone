package com.example.spring_team4_be.security;

import com.example.spring_team4_be.jwt.AccessDeniedHandlerException;
import com.example.spring_team4_be.jwt.AuthenticationEntryPointException;
import com.example.spring_team4_be.jwt.TokenProvider;
import com.example.spring_team4_be.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console ????????? ?????? ?????? (CSRF, FrameOptions ??????)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }


    // ?????? ????????? ????????? ?????? / ???????????? ?????? ??????
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // allowedOrigins??? * ??? ?????? ????????? ????????????.
                registry.addMapping("/**").allowedOrigins("http://localhost:8080", "http://localhost:3000", "http://13.125.55.110:8080", "http://13.125.55.110/", "http://twitter-mini-clone.s3-website.ap-northeast-2.amazonaws.com")
                        .allowCredentials(true)
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException)
                .accessDeniedHandler(accessDeniedHandlerException)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/google/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //????????? url ??????

        configuration.addAllowedOrigin("http://localhost:3000 http://twitter-mini-clone.s3-website.ap-northeast-2.amazonaws.com");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://twitter-mini-clone.s3-website.ap-northeast-2.amazonaws.com");

        //????????? ?????? ??????
        configuration.addAllowedHeader("*");
        //????????? http method
        configuration.addAllowedMethod("*");
        //?????????????????? ?????? ??? ??? ?????? ?????? ?????? ??????
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Refresh-Token");
        //????????? ?????? ????????? ??????????????? ??????
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}