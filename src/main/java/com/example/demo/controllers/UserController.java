package com.example.demo.controllers;


import com.example.demo.entities.FriendList;
import com.example.demo.entities.UserDetails;
import com.example.demo.model.ProfileDTO;
import com.example.demo.services.FriendListService;
import com.example.demo.services.ImageService;
import com.example.demo.services.KeycloakUserService;
import com.example.demo.services.UserDetailsService;
import jakarta.ws.rs.PathParam;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

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

    @Autowired
    private FriendListService friendListService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);






    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication ) {
        // Extract the username from the JWT
        logger.debug("/profile called");
        var keycloakUser=keycloakUserService.getUserById(authentication.getName());
        var userDetails=userDetailsService.getUserDetailsById(authentication.getName());
        ProfileDTO profile=new ProfileDTO(keycloakUser.getUsername(),keycloakUser.getEmail(),userDetails);
        profile.setFriends(friendListService.getFriendListOfUser(authentication.getName()));
        logger.debug("response {}",profile);
        return ResponseEntity.ok(profile);

    }

    @PostMapping("/pic")
    public ResponseEntity<String> uploadPic(

        @RequestParam("img") MultipartFile img,Authentication authentication
    ){
        logger.debug("called /pic params {}, {}",img,authentication);
   String imgPath= imageService.saveImageToStorage(img);
   if(imgPath==null){
       logger.warn("imgPath==null returning 400");
       return ResponseEntity.status(400).body("error");
   }
   try {
       UserDetails userDetails=userDetailsService.getUserDetailsById(authentication.getName());
       userDetails.setProfilePic(imgPath);
       userDetailsService.updateUserDetails(userDetails, authentication.getName());
       logger.debug("/pic success response {}",imgPath);
       return ResponseEntity.ok("File upload success");
   }catch (Exception e){
       logger.warn("exception at /pic ",e);
       return ResponseEntity.status(400).body("Problem saving image");
   }
   //TODO Error checking
    }

    //TODO Include pic in GET profile
    @GetMapping("/picture")
    public ResponseEntity<String> getPic(Authentication authentication){
        UserRepresentation userRepresentation=keycloakUserService.getUserById(authentication.getName());
        logger.debug("/picture called credentials {}, name {}, principal {}, keycloak username {}",authentication.getCredentials(),authentication.getName(),authentication.getPrincipal(),userRepresentation.getUsername());
        try{
            UserDetails userDetails=userDetailsService.getUserDetailsById(userRepresentation.getId());
            String userImg=userDetails.getProfilePic();
            if(userImg!=null){
                String encodedImg= Base64.getEncoder().encodeToString(imageService.getImage(userImg));
                logger.debug("/picture success userDetails {}",userDetails);
                return ResponseEntity.ok(encodedImg);
            }else{
                logger.debug("user has no img returning 404");
                return ResponseEntity.status(404).body("no image");
            }

        } catch (Exception e) {
            logger.warn("exception in img encoding",e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/friendRequest/{usernameOrEmail}")
    public ResponseEntity<String> doesUserExist(@PathVariable String usernameOrEmail,Authentication authentication){
        UserRepresentation user=keycloakUserService.getUserByEmailOrUsername(usernameOrEmail);
        String senderId=authentication.getName();
        logger.debug("/friendRequest/{} called: user {}, senderId {}",user,senderId);
        if( user !=null ) {
            try {
                if(friendListService.friendRequest(senderId, user.getId())) {
                    logger.debug("success friend request added");
                    return ResponseEntity.ok("Friend added");
                }
            }catch(Exception e){
                logger.warn("Error with friend request ",e);
                return ResponseEntity.status(400).body("problems with friendship");
            }
        }
        logger.debug("user not found returning 404");
        return ResponseEntity.status(404).body(null);
    }

    @PostMapping("/acceptRequest/{senderId}")
    public ResponseEntity<String> acceptRequest(@PathVariable String senderId,Authentication authentication){
        try{
            friendListService.acceptFriendRequest(authentication.getName(), senderId);
            logger.debug("/acceptRequest/{} called",senderId);
            return ResponseEntity.ok("Request accepted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Friend request doesn't exist, or person isn't the receiver");
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
