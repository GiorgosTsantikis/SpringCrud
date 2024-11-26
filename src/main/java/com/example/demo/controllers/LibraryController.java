package com.example.demo.controllers;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.services.BookService;
import com.example.demo.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LibraryController {

    private LibraryService libraryService;
    private BookService bookService;

    @Autowired
    public LibraryController(LibraryService lib,BookService book){
        this.bookService=book;
        this.libraryService=lib;
    }

    @PostMapping("/library")
    public void createLibrary(@RequestBody Library library){
        libraryService.createLibrary(library);
    }

    @GetMapping("/library/{id}")
    public java.util.List<String> getLibraryById(@PathVariable("id") int id){
        var books=libraryService.getLibraryById(id);
        if(books.isPresent()) {
            System.out.println("\n\n"+books.get().getBooks()+"\n\n");
            var ret=new ArrayList<String>();
            books.get().getBooks().forEach(x->ret.add(x.getTitle()));
            return ret;
        }
        return null;


    }



    @PutMapping("/{library}/{book}")
    public void addBookToLibrary(@PathVariable("library") int libId,@PathVariable("book") int bookId){
       libraryService.addBookToLibrary(libId,bookId);
    }

}
