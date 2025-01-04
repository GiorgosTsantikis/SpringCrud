package com.example.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomJwtAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Extract roles from realm_access and resource_access
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        System.out.println(jwt.getClaim("realm_access").toString()+" jwt claim realm access");

        // Add roles from realm_access
        if (realmAccess != null) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles != null) {
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            }
        }

        // Add roles from resource_access (for specific client roles)
        if (resourceAccess != null) {
            Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("spring-boot-app");
            if (clientRoles != null) {
                List<String> clientRolesList = (List<String>) clientRoles.get("roles");
                if (clientRolesList != null) {
                    clientRolesList.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                }
            }
        }
        System.out.println(authorities+" authorities");

        return authorities;
    }
}
