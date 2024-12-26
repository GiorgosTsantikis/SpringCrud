package com.example.demo.services;

import com.example.demo.entities.UserDetails;
import com.example.demo.repositories.UserDetailsRepository;
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



    //TODO Make extensible
    public UserDetails updateUserDetails(UserDetails userDetails,String id){
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
        return userDetailsRepository.save(userDetailsOld);
    }

    public UserDetails getUserDetailsById(String id){
       Optional<UserDetails> userDetails=userDetailsRepository.findById(id);
       if(userDetails.isPresent()){
           return userDetails.get();
       }else{
           return createUserDetails(id);
       }
    }

    public UserDetails createUserDetails(String id){
        return userDetailsRepository.save(new UserDetails(id));
    }





    public boolean saveProfileImg(String imgPath,String id) {
        Optional<UserDetails>userDetails=userDetailsRepository.findById(id);
        if(userDetails.isPresent()) {
            userDetails.get().setProfilePic(imgPath);
            userDetailsRepository.save(userDetails.get());
            return true;
        }
        return false;
    }

}
