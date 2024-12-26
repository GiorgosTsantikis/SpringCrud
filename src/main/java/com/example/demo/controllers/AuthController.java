package com.example.demo.controllers;

import com.example.demo.entities.UserDTO;
import com.example.demo.services.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:5173")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder,  KeycloakUserService keycloakUserService){

        this.passwordEncoder=passwordEncoder;
        this.keycloakUserService=keycloakUserService;
    }

    /*
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> user){
        String username=user.get("username");
        String email=user.get("email");
        String password= passwordEncoder.encode(user.get("password"));
        var newUser=new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRole(Role.ROLE_USER);

        try {
            userService.registerUser(newUser);
        } catch (RuntimeException e) {
            System.out.println(e);
            return ResponseEntity.status(401).body("Username taken");
        }
        return ResponseEntity.ok("User registered successfully");

    }

     */

    /*
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String>credentials){
        String username=credentials.get("username");
        String password=credentials.get("password");
        try {
            User user = userService.findByUsername(username);
            if (passwordEncoder.matches(password, user.getPassword())) {

                return ResponseEntity.ok(Map.of("token", "token"));
            }
        }catch(RuntimeException e) {
            return ResponseEntity.status(401).body("Invalid username");
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

     */

/*
    @GetMapping("/hello")
    public ResponseEntity<String> hello(OAuth2AuthenticationToken token){
        System.out.println(token.getCredentials()+" "+"<-cred princ->"+" "+token.getPrincipal());
        var principal=token.getPrincipal();
        System.out.println(principal.getAuthorities()+" "+principal.getName());
        return ResponseEntity.ok("hello");
    }

 */



    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserDTO request) {
        String response = keycloakUserService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }





}
