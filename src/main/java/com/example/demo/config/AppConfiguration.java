package com.example.demo.config;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.entities.Listing;
import com.example.demo.services.BookService;
import com.example.demo.services.LibraryService;
import com.example.demo.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
public class AppConfiguration {



	private final LibraryService libraryService;

	private final BookService bookService;

    private final ListingService listingService;

    @Autowired
    public AppConfiguration(LibraryService lib,BookService book,ListingService listingService){
        this.bookService=book;
        this.libraryService=lib;
        this.listingService=listingService;
    }

    @Bean
    public CommandLineRunner initialiseDatabase(){

        return args ->
        {

            Library lib1 = new Library("lib1");
            Library lib2 = new Library("lib2");
            Library lib3 = new Library("lib3");
            Book book1 = new Book("book1", "1", 10);
            Book book2 = new Book("book2", "2", 20);
            Book book3 = new Book("book1", "3", 30);
            bookService.createBook(book1);
            bookService.createBook(book2);
            bookService.createBook(book3);

            libraryService.createLibrary(lib1);
            libraryService.createLibrary(lib2);
            libraryService.createLibrary(lib3);
            libraryService.addBookToLibrary(lib1.getId(), book1.getId());
            libraryService.addBookToLibrary(lib2.getId(), book1.getId());
            libraryService.addBookToLibrary(lib3.getId(), book1.getId());


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

        };


    }

}