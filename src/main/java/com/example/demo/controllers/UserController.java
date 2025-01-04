package com.example.demo.controllers;


import com.example.demo.entities.UserDetails;
import com.example.demo.model.ProfileDTO;
import com.example.demo.services.ImageService;
import com.example.demo.services.KeycloakUserService;
import com.example.demo.services.UserDetailsService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@CrossOrigin(origins="http://localhost:5173")
@RequestMapping("/users")
public class UserController {



    @Autowired
    private ImageService imageService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private KeycloakUserService keycloakUserService;





    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication ) {
        // Extract the username from the JWT
        var keycloakUser=keycloakUserService.getUserById(authentication.getName());
        var userDetails=userDetailsService.getUserDetailsById(authentication.getName());
        return ResponseEntity.ok(new ProfileDTO(keycloakUser.getUsername(),keycloakUser.getEmail(),userDetails));

    }

    @PostMapping("/pic")
    public ResponseEntity<String> uploadPic(

        @RequestParam("img") MultipartFile img,Authentication authentication
    ){
   String imgPath= imageService.saveImageToStorage(img);
   if(imgPath==null){return ResponseEntity.status(400).body("error");}
   try {
       UserDetails userDetails=userDetailsService.getUserDetailsById(authentication.getName());
       userDetails.setProfilePic(imgPath);
       userDetailsService.updateUserDetails(userDetails, authentication.getName());
       return ResponseEntity.ok("File upload success");
   }catch (Exception e){
       return ResponseEntity.status(400).body("Problem saving image");
   }
   //TODO Error checking
    }


    @GetMapping("/picture")
    public ResponseEntity<String> getPic(Authentication authentication){
        System.out.println(authentication.getCredentials()+"  "+ authentication.getName()+" "+authentication.getPrincipal());
        UserRepresentation userRepresentation=keycloakUserService.getUserById(authentication.getName());
        System.out.println(userRepresentation.getUsername());


        try{
            UserDetails userDetails=userDetailsService.getUserDetailsById(userRepresentation.getId());
            String encodedImg= Base64.getEncoder().encodeToString(imageService.getImage(userDetails.getProfilePic()));
            return ResponseEntity.ok(encodedImg);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }


    }




    /*
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

     */

    /*
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

     */







}
