package com.example.demo.config;

import com.example.demo.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY="858DB36A9D3577E0F6ED6AC7EBEC1424455F3597BC87899B7E3F6A626F8515F13E61096D50D25DFC364B7F407E6D1D807768119ED97B3739487B47E6B5BBDAB9C901A923327D96026E51FC9CC4BA3DC3F3B634402BC3B939954D0090A4F7429D6B83A56DC117CEBB819B6707CF88C2AAF76C90CC0047FB4219ADA249C23E1239";

    //ok
    public String extractUsername(String token){
        try {
            return extractAllClaims(token).getSubject();
        }catch(Exception e){
            System.out.println("Invalid token");
        }
        return null;
    }

    public String extractRole(String token){
        Claims claims=extractAllClaims(token);
        return claims.get("role", String.class);
    }



    //ok
    public boolean isTokenValid(String token,String username){
         String extractUsername=extractUsername(token);
         if(username!=null) {
             return (username.equals(extractUsername)) && !isTokenExpired(token);
         }
         return false;
    }

    //ok
    private boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Collection<GrantedAuthority> getAuthoritiesFromToken(String token) {
        // Decode the token and extract roles
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Use your secret key
                .build()
                .parseClaimsJws(token)
                .getBody();

        String role = claims.get("role", String.class); // Assuming the role is stored as "role" in JWT
        return Collections.singletonList(new SimpleGrantedAuthority( role)); // Add "ROLE_" prefix to role
    }




    //ok
    public String generateToken(User user){
        Claims claims=Jwts.claims().setSubject(user.getUsername());
        claims.put("role",user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith( SignatureAlgorithm.HS256,JwtService.SECRET_KEY)
                .compact();
    }

    //ok
    private Claims extractAllClaims(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(JwtService.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


}
