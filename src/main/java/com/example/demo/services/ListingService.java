package com.example.demo.services;

import com.example.demo.controllers.ListingController;
import com.example.demo.entities.Listing;
import com.example.demo.repositories.ListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ListingService {

    private ListingRepository listingRepository;

    private static final Logger logger = LoggerFactory.getLogger(ListingService.class);


    @Autowired
    public ListingService(ListingRepository listingRepository){
        this.listingRepository=listingRepository;
    }

    public List<Listing> getAllListings(){
        List<Listing> result=this.listingRepository.findAll();
        logger.debug("ListingService.getAllListings() result {}",result);
        return result;
    }

    public Listing getListingById(int id){
        Listing listing= this.listingRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Listing with id:"+id+" doesn't exist"));
        logger.debug("ListingService.getListingById( {} ) result {}",id,listing);
        return listing;
    }

    public int saveListing(Listing listing){
        var result= this.listingRepository.save(listing).getId();
        logger.debug("ListingService.saveListing( {} ) result {}",listing,result);
        return result;
    }

    public Listing updateListing( Listing listing) {
        Optional<Listing> existingListing = listingRepository.findById(listing.getId());
        logger.debug("ListingService.updateListing( {} )",listing);
        //TODO:CHECK
        if (existingListing.isPresent()) {
            var result= listingRepository.save(listing);
            logger.debug("ListingService.updateListing listing is present updated: {}",result);
            return result;
        } else {
            logger.warn("ListingService.updateListing listing not found throwing exception");
            throw new RuntimeException("Listing not found  id:" + listing.getId());
        }
    }

    public void deleteListing(int id){
        Optional<Listing> listing=this.listingRepository.findById(id);
        logger.debug("ListingService.deleteListing( {} ) isPresent? {}",id,listing.isPresent());
        if(listing.isPresent())
            this.listingRepository.deleteById(id);
        else
            throw new RuntimeException("Listing not found id:"+id);
    }
}
