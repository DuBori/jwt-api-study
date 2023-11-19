package com.api.jwtApi;

import com.api.jwtApi.enums.JwtUserExceptionType;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final int code;

    public JwtException(final JwtUserExceptionType jwtUserExceptionType) {
        super(jwtUserExceptionType.getDesc());
        this.code = jwtUserExceptionType.getCode();
    }

}
