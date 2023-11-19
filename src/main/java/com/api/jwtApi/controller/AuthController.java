package com.api.jwtApi.controller;

import com.api.jwtApi.domain.request.JwtLoginRequest;
import com.api.jwtApi.domain.response.JwtTokenResponse;
import com.api.jwtApi.entity.DefaultResponse;
import com.api.jwtApi.service.AuthService;
import com.api.jwtApi.util.JwtParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;

    // 검증된 회원 token 생성 및 발급
    @PostMapping("/create")
    public ResponseEntity<DefaultResponse<JwtTokenResponse>> createAuthenticationToken(@RequestBody final JwtLoginRequest request, HttpServletResponse response) {
        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(request.toAuthentication());
        // 2. SecurityContextHolder에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final JwtTokenResponse token = authService.createJwtToken(authentication);
        createCookie(response, token.getRefreshToken());
        return new ResponseEntity<>(new DefaultResponse<>(token), settingHeader(token), HttpStatus.OK);
    }

    private void createCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // 30일
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }

    @PostMapping("/resource-check")
    public ResponseEntity<DefaultResponse<Void>> resourceCheck(HttpServletRequest request, @RequestHeader(name = "Authorization") String accessToken) {
        log.info("Bearer : {}", accessToken);
        String refreshToken = JwtParser.getRefreshToken(request);
        log.info("refresh Token : {}", refreshToken);
        if ("access".equals(authService.validateAccessToken(JwtParser.extractAccessToken(accessToken)))) {
            return new ResponseEntity<>(new DefaultResponse<>("Access Resource", 200), HttpStatus.OK);
        }
        log.info("만료 확인 후 다시 발급");
        // 만료된 경우 새로 발급
        String token = authService.createAccessTokenByRefreshToken(refreshToken);
        return new ResponseEntity<>(new DefaultResponse<>("Access Resource", 200),settingHeader(new JwtTokenResponse(token, null)),HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> createRefreshAuthenticationToken(@RequestBody final JwtLoginRequest request, HttpServletResponse response) throws URISyntaxException {
        final Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(request.toAuthentication());
        final JwtTokenResponse token = authService.createJwtToken(authenticate);
        createCookie(response, token.getRefreshToken());
        return new ResponseEntity<>(new DefaultResponse<>(token), settingHeader(token), HttpStatus.OK);
    }

    private HttpHeaders settingHeader(JwtTokenResponse token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.getAccessToken());
        return headers;
    }
}
