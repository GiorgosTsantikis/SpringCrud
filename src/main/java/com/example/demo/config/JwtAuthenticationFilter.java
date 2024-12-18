package com.example.demo.config;


import com.example.demo.controllers.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService=jwtService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,  HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");

        if(authHeader!=null&&authHeader.startsWith("Bearer ")){
            String token=authHeader.substring(7);
            String username=jwtService.extractUsername(token);
            System.out.println(jwtService.extractRole(token));
            if(jwtService.isTokenValid(token,username)){
                SecurityContextHolder.getContext().setAuthentication(
                        new JwtAuthenticationToken(username,token,jwtService.getAuthoritiesFromToken(token)));
            }else{
                System.out.println("token invalid");
            }

        }
        filterChain.doFilter(request,response);

        }

    }

