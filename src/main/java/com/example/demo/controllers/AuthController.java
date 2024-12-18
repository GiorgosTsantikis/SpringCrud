package com.example.demo.controllers;

import com.example.demo.config.JwtService;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:5173")
public class AuthController {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AuthController(JwtService jwtService,PasswordEncoder passwordEncoder,UserService userService ){
        this.jwtService=jwtService;
        this.passwordEncoder=passwordEncoder;
        this.userService=userService;
    }

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String>credentials){
        String username=credentials.get("username");
        String password=credentials.get("password");
        try {
            User user = userService.findByUsername(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(Map.of("token", token));
            }
        }catch(RuntimeException e) {
            return ResponseEntity.status(401).body("Invalid username");
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }








}
