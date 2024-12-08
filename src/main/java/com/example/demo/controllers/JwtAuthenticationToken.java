package com.example.demo.controllers;

import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final String principal;

    public JwtAuthenticationToken(String principal, String token){
        super(null);
        this.principal=principal;
        this.token=token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials(){
        return token;
    }

    @Override
    public Object getPrincipal(){
        return principal;
    }
}
