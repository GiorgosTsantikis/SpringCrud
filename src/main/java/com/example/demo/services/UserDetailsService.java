package com.example.demo.services;

import com.example.demo.controllers.UserController;
import com.example.demo.entities.FriendRequestStatus;
import com.example.demo.entities.UserDetails;
import com.example.demo.repositories.UserDetailsRepository;
import org.keycloak.jose.jwk.JWK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);




    //TODO Make extensible
    public UserDetails updateUserDetails(UserDetails userDetails,String id){
        logger.debug("UserDetailsService.updateUserDetails( {}, {} )",userDetails,id);
        UserDetails userDetailsOld=userDetailsRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User details not found "+id));
        if(userDetails.getAddress()!=null){
            userDetailsOld.setAddress(userDetails.getAddress());
        }
        if(userDetails.getProfilePic()!=null){
            userDetailsOld.setProfilePic(userDetails.getProfilePic());
        }
        if(userDetails.getState()!=null){
            userDetailsOld.setState(userDetails.getState());
        }
        var result= userDetailsRepository.save(userDetailsOld);
        logger.debug("End UserDetailsService.updateUserDetails result {}",result);
        return result;
    }

    public UserDetails getUserDetailsById(String id){
       Optional<UserDetails> userDetails=userDetailsRepository.findById(id);
        logger.debug("UserDetailsService.getUserDetailsById( {} ) isPresent? {}",id,userDetails.isPresent());
       if(userDetails.isPresent()){
           return userDetails.get();
       }else{
           return createUserDetails(id);
       }
    }



    public UserDetails createUserDetails(String id){
        logger.debug("UserDetailsService.createUserDetails( {} )",id);
        return userDetailsRepository.save(new UserDetails(id));
    }





    public boolean saveProfileImg(String imgPath,String id) {
        logger.debug("UserDetailsService.saveProfileImg( {}, {} )",imgPath,id);
        Optional<UserDetails>userDetails=userDetailsRepository.findById(id);
        if(userDetails.isPresent()) {
            userDetails.get().setProfilePic(imgPath);
            userDetailsRepository.save(userDetails.get());
            logger.debug("UserDetailsService.saveProfileImg user found img saved");
            return true;
        }
        logger.warn("UserDetailsService.saveProfileImg user NOT found img saved");
        return false;
    }

}
