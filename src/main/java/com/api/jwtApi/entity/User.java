package com.api.jwtApi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
//@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //@Id
    //@Column(length = 64)
    private String id;
    private String name;
    //@Transient
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
}
