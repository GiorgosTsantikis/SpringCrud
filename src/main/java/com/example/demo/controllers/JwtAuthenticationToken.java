package com.example.demo.controllers;

import com.example.demo.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final String principal;
    private final Collection<GrantedAuthority> authorities;



    public JwtAuthenticationToken(String principal, String token,Collection<GrantedAuthority>authorities){
        super(authorities);
        this.principal=principal;
        this.token=token;
        this.authorities=authorities;
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

    @Override
    public Collection<GrantedAuthority> getAuthorities(){
        return authorities;
    }





}
