package com.example.demo.services;

import com.example.demo.config.JwtService;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private JwtService jwtService;
    private UserRepository userRepository;
    private ImageService imageService;

    @Autowired
    public UserService(UserRepository userRepository,JwtService jwtService,ImageService imageService){
        this.jwtService=jwtService;
        this.userRepository=userRepository;
        this.imageService=imageService;
    }


    public void registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        userRepository.save(user);
    }

    public void updateUser(User user){
        Optional<User> myUser=userRepository.findById(user.getId());
        if(myUser.isPresent()){
            userRepository.save(user);
        }else {
            throw new RuntimeException("User not present");
        }
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


    public void saveProfileImg(String imgPath,User user) {
        user.setProfilePic(imgPath);
        this.updateUser(user);
    }
}
