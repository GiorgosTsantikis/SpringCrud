package com.example.demo.controllers;

import com.example.demo.entities.Listing;
import com.example.demo.services.ListingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;

import static org.hibernate.internal.CoreLogging.logger;

@RestController
@CrossOrigin(origins="http://localhost:5173")
public class ListingController {

    private ListingService listingService;

    private static final Logger logger = LoggerFactory.getLogger(ListingController.class);


    @Autowired
    public ListingController(ListingService listingService)
    {
        this.listingService=listingService;
    }

    @GetMapping("/listings")
    public List<Listing> getListings(){
        logger.info("CALLING /listings");
        List<Listing> listings= this.listingService.getAllListings();
        logger.info("RESULT {}",listings);
        return listings;
    }

    @GetMapping("/listings/{id}")
    public Listing getListingWithId(@PathVariable int id){
        try{
           return  this.listingService.getListingById(id);
        } catch (Exception e) {
            System.out.println("Problem not found id:"+id);
        }
        return null;
    }

    @PostMapping("listings")
    public int createListing(@RequestBody Listing listing){
        return this.listingService.saveListing(new Listing(listing));
    }

    @PutMapping("/listings")
    public Listing updateListing(@RequestBody Listing listing){
        System.out.println("Trying to update id:"+listing.getId());
        try {
            return this.listingService.updateListing(listing);
        }catch (Exception e){
            System.out.println("problem");
            return null;
        }
    }

    @DeleteMapping("/listings/{id}")
    public String deleteListing(@PathVariable int id){
        System.out.println("Trying to delete id:"+id);
        try{
            this.listingService.deleteListing(id);
            return "Deleted successfully entry with id:"+id;
        } catch (Exception e) {
            return "ID not found "+id;
        }
    }
}
