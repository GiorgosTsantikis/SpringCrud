package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY="858DB36A9D3577E0F6ED6AC7EBEC1424455F3597BC87899B7E3F6A626F8515F13E61096D50D25DFC364B7F407E6D1D807768119ED97B3739487B47E6B5BBDAB9C901A923327D96026E51FC9CC4BA3DC3F3B634402BC3B939954D0090A4F7429D6B83A56DC117CEBB819B6707CF88C2AAF76C90CC0047FB4219ADA249C23E1239";

    //ok
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }



    //ok
    public boolean isTokenValid(String token,String username){
         String extractUsername=extractUsername(token);
        return (username.equals(extractUsername))&& !isTokenExpired(token);
    }

    //ok
    private boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }




    //ok
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith( SignatureAlgorithm.HS256,JwtService.SECRET_KEY)
                .compact();
    }

    //ok
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(JwtService.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }


}
