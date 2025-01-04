package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    public JwtService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }


    public boolean isTokenValid(String token){
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
            var response=restTemplate.exchange(introspectionUrl,HttpMethod.POST,request,Map.class);
            System.out.println(response+" response");
            Boolean active=(Boolean)response.getBody().get("active");
            System.out.println(active+" "+response.getBody().get("active"));
            if(active){
                return true;
            }else{
                return false;
            }
            }catch(Exception e){
            System.out.println("Error"+e);
            return false;

        }
    }

    public String getUserFromToken(String token){
        DecodedJWT decodedJWT= JWT.decode(token);
        String username= decodedJWT.getClaim("preferred_username").toString();
        System.out.println(username);
        return username;
    }

   public String getAttributeFromToken(String token,String attribute){
        DecodedJWT decodedJWT=JWT.decode(token);
        String result=decodedJWT.getClaim(attribute).toString();
        return result;
   }

   public Set<String> getClaims(String token){
        DecodedJWT decodedJWT=JWT.decode(token);
        var claims=decodedJWT.getClaims().keySet();
        return claims;
   }

   public boolean validateToken(String token){
       X509EncodedKeySpec keySpec=new X509EncodedKeySpec(Base64.getDecoder().decode(key));
       try{
           KeyFactory keyFactory=KeyFactory.getInstance("RSA");
           RSAPublicKey key= (RSAPublicKey) keyFactory.generatePublic(keySpec);
           JWTVerifier verifier=JWT.require(Algorithm.RSA256(key,null))
                   .withIssuer(issuer)
                   .build();
           verifier.verify(token);
           return true;
       } catch (Exception e) {
           System.out.println(e+" error in validation");
       }
       return false;
   }
}

