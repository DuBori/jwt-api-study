package com.api.jwtApi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final CustomAuthenticationProvider customAuthenticationManger;
  @Bean
  public SecurityFilterChain webSecurityCustomizer(HttpSecurity http) throws  Exception {
    return http
            .csrf(it -> it.disable()) // 크로스 사이팅 관련 설정 필요없음
            .cors(it -> it.disable())
            /*.authorizeHttpRequests(it -> it.requestMatchers("/api/**")
                    *//*.hasRole("ADMIN")*//*
                    .authenticated()
                    .anyRequest().permitAll()) */// url 인가 설정완료
            .httpBasic(Customizer.withDefaults())
            .build();
  }
  @Bean
  public AuthenticationManager authManger(HttpSecurity httpSecurity) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(customAuthenticationManger);
    return authenticationManagerBuilder.build();
  }
}