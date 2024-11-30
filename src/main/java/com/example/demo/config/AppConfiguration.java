package com.example.demo.config;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.services.BookService;
import com.example.demo.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class AppConfiguration {



	private final LibraryService libraryService;

	private final BookService bookService;

    @Autowired
    public AppConfiguration(LibraryService lib,BookService book){
        this.bookService=book;
        this.libraryService=lib;
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

        };


    }

}
