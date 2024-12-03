package com.example.demo.repositories;

import com.example.demo.entities.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing,Integer> {
}
