package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.controllers.ListingController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Service
public class JwtService {

    @Value("${public-RS256-key}")
    private  String key;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${client-id}")
    private String clientId;

    @Value("${client-secret}")
    private String clientSecret;

    @Value("${issuer}")
    private String issuer;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


    @Autowired
    public JwtService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }


    public boolean isTokenValid(String token){
        logger.debug("JwtService.isTokenValid( {} )",token);
        String introspectionUrl = String.format("%s/realms/%s/protocol/openid-connect/token/introspect", keycloakUrl, realm);
        if(token==null){return false;}
        // Request body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("token", token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Make the POST request
        try{
            logger.debug("CALLING POST  {} request {} ",introspectionUrl,request);
            var response=restTemplate.exchange(introspectionUrl,HttpMethod.POST,request,Map.class);

            Boolean active=(Boolean)response.getBody().get("active");
            logger.debug("RESPONSE {} active? {}",response,active);
            if(active){
                return true;
            }else{
                return false;
            }
            }catch(Exception e){
           logger.warn("exception in http request",e);
            return false;

        }
    }

    public String getUserFromToken(String token){
        DecodedJWT decodedJWT= JWT.decode(token);
        String username= decodedJWT.getClaim("preferred_username").toString();
        logger.debug("JwtService.getUserFromToken( {} ) username {}",token,username);
        return username;
    }

   public String getAttributeFromToken(String token,String attribute){
        DecodedJWT decodedJWT=JWT.decode(token);
        String result=decodedJWT.getClaim(attribute).toString();
        logger.debug("JwtService.getAttributeFromToken( {}, {} ) result {}",token,attribute,result);
        return result;
   }

   public Set<String> getClaims(String token){
        DecodedJWT decodedJWT=JWT.decode(token);
        var claims=decodedJWT.getClaims().keySet();
        logger.debug("JwtService.getClaims( {} ) claims {}",token,claims);
        return claims;
   }

   public boolean validateToken(String token){
       X509EncodedKeySpec keySpec=new X509EncodedKeySpec(Base64.getDecoder().decode(key));
       logger.debug("JwtService.validateToken( {} )",token);
       try{
           KeyFactory keyFactory=KeyFactory.getInstance("RSA");
           RSAPublicKey key= (RSAPublicKey) keyFactory.generatePublic(keySpec);
           JWTVerifier verifier=JWT.require(Algorithm.RSA256(key,null))
                   .withIssuer(issuer)
                   .build();
           verifier.verify(token);
           logger.debug("Verified");
           return true;
       } catch (Exception e) {
           logger.warn("Exception in JwtService.validateToken ",e);
       }
       return false;
   }
}

