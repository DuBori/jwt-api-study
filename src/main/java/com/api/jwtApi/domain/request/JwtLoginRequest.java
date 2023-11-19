package com.api.jwtApi.domain.request;

import com.api.jwtApi.JwtException;
import com.api.jwtApi.enums.JwtUserExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

@Getter
@RequiredArgsConstructor
public class JwtLoginRequest {
    private final String name;
    private final String pwd;

    public JwtLoginRequest() {
        name = null;
        pwd = null;
    }


    public Authentication toAuthentication() {
        if (!StringUtils.hasText(name)) {
            throw new JwtException(JwtUserExceptionType.REQUEST_EMPTY_NAME);
        }
        if (!StringUtils.hasText(pwd)) {
            throw new JwtException(JwtUserExceptionType.REQUEST_EMPTY_PWD);
        }
        return new UsernamePasswordAuthenticationToken(this, null);
    }
}
