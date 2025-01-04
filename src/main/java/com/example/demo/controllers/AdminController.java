package com.example.demo.controllers;

import com.example.demo.services.KeycloakUserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins="http://localhost:5173")
public class AdminController {


    @Autowired
    private KeycloakUserService keycloakUserService;


    @GetMapping("/hello")
    public ResponseEntity<String> sayHello( ){
        return ResponseEntity.ok("hello");
    }

    /*
    @PostMapping("/makeAdmin/{username}")
    public ResponseEntity<User> makeUserAdmin(JwtAuthenticationToken token, @PathVariable String username){
        System.out.println(username);
        try{
            User user=userService.findByUsername(username);
            if(user!=null){
                user.setRole(Role.ROLE_ADMIN);
                userService.updateUser(user);
                return ResponseEntity.ok(user);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return ResponseEntity.status(403).body(null);
    }

     */



    @GetMapping("/allUsers")
    public List<UserRepresentation> getUsers(){
        return keycloakUserService.getUsers();
    }


}
