package com.api.jwtApi.config;

import com.api.jwtApi.JwtException;
import com.api.jwtApi.enums.JwtUserExceptionType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // db 있을떄 여기서 매칭
        if (ObjectUtils.isEmpty(authentication)) {
            throw new JwtException(JwtUserExceptionType.AUTHENTICATION_EMPTY);
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
