package com.example.demo.config;

import com.example.demo.entities.Listing;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.services.ListingService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
public class AppConfiguration {





    private final ListingService listingService;
    private final UserService userService;

    @Autowired
    public AppConfiguration(ListingService listingService,UserService userService){
        this.listingService=listingService;
        this.userService=userService;
    }

    @Bean
    public CommandLineRunner initialiseDatabase(){

        return args ->
        {




            List<Listing> theListings=new ArrayList<>();
            theListings.add(new Listing("House","Poli","state1","assets/house.jpg",true,true,5,200_000,2));
            theListings.add(new Listing("other house","Thessaloniki","state1","assets/house.jpg",true,true,5,1_000_000,3));
            theListings.add(new Listing("a third house","Katerini","state1","assets/house.jpg",true,true,5,500_000,1));
            theListings.add(new Listing("big house","Athens","state1","assets/house.jpg",true,true,5,150_000,2));
            theListings.add(new Listing("small house","Katsavraxa","state1","assets/house.jpg",true,true,5,250_000,3));
            theListings.add(new Listing("idk","Agrinio","Katerini","assets/house.jpg",true,true,6,300_000,3));
            theListings.add(new Listing("idc","Poli","Thessaloniki","assets/house.jpg",true,true,7,90_000,2));
            theListings.add(new Listing("final house","Athens","state1","assets/house.jpg",true,true,8,70_000,5));
            theListings.forEach(this.listingService::saveListing);

            User admin=userService.findByUsername("admin");
            admin.setRole(Role.ROLE_ADMIN);
            userService.updateUser(admin);



        };


    }

}
