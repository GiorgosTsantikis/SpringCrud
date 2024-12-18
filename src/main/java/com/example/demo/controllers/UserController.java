package com.example.demo.controllers;


import com.example.demo.config.JwtService;
import com.example.demo.entities.User;
import com.example.demo.entities.UserDetails;
import com.example.demo.services.ImageService;
import com.example.demo.services.UserDetailsService;
import com.example.demo.services.UserService;
import jakarta.persistence.Tuple;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins="http://localhost:5173")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;



    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(JwtAuthenticationToken authenticationToken) {
        // Extract the username from the JWT
        try {
            User user = userService.getUserFromToken((String) authenticationToken.getCredentials());
            System.out.println("you are looking for"+jwtService.extractRole((String)authenticationToken.getCredentials())+" "+user);
            return ResponseEntity.ok(user);// Fetch user details from DB
        } catch (RuntimeException e) {
            System.out.println(e+" error");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/pic")
    public ResponseEntity<String> uploadPic(
            JwtAuthenticationToken token,
        @RequestParam("img") MultipartFile img
    ){
   String imgPath= imageService.saveImageToStorage(img);
   if(imgPath==null){return ResponseEntity.status(400).body("error");}
   try {
       User user = userService.getUserFromToken((String) token.getCredentials());
       UserDetails userDetails=userDetailsService.getUserDetailsById(user.getId());
       userDetails.setProfilePic(imgPath);
       userDetailsService.updateUserDetails(userDetails,user.getId());
       return ResponseEntity.ok("File upload success");
   }catch (Exception e){
       return ResponseEntity.status(400).body("Problem saving image");
   }
   //TODO Error checking
    }

    @GetMapping("/picture")
    public ResponseEntity<String> getPic(JwtAuthenticationToken jwtAuthenticationToken){
        System.out.println(jwtAuthenticationToken);
        try{
            UserDetails userDetails=userDetailsService.getUserDetailsById(userService.getUserFromToken((String)jwtAuthenticationToken.getCredentials()).getId());
            String encodedImg= Base64.getEncoder().encodeToString(imageService.getImage(userDetails.getProfilePic()));
            return ResponseEntity.ok(encodedImg);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }


    @PutMapping("/updateUsername")
    public ResponseEntity<?> updateUser(JwtAuthenticationToken token,@RequestBody Map<String,String> request){
        try{
            String username=request.get("username");
            User old=userService.getUserFromToken((String)token.getCredentials());
            if(old!=null && username!=null){
                old.setUsername(username);
                String newTokenString=jwtService.generateToken(old);
                JwtAuthenticationToken newToken=new JwtAuthenticationToken(username,newTokenString,jwtService.getAuthoritiesFromToken(newTokenString));
                userService.updateUser(old);

                return ResponseEntity.ok(Map.of("token",newToken.getCredentials()));
            }
        }catch(Exception e){
            System.out.println("Error updating user given ");

        }
        return ResponseEntity.status(403).body("Error updating user");
    }

    @PutMapping("updateUserDetails")
    public ResponseEntity<?> updateUserDetails(JwtAuthenticationToken token,@RequestBody UserDetails userDetails){
        try{
            User user=userService.getUserFromToken((String) token.getCredentials());
            if(user!=null){
                userDetailsService.updateUserDetails(userDetails,user.getId());
                return ResponseEntity.ok("User update success");
            }
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        return ResponseEntity.status(400).body("Problem updating user details");
    }







}
