package com.example.demo.services;

import com.example.demo.config.JwtService;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private JwtService jwtService;
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository,JwtService jwtService){
        this.jwtService=jwtService;
        this.userRepository=userRepository;
    }


    public void registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
    }

    public List<User> getAllUsers(){
         return userRepository.findAll();
    }

    public User getUserFromToken(String token){
        String username=jwtService.extractUsername(token);
        return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found with username:"+username));
    }


}
