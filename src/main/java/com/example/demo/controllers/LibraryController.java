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

    //Get libraries with bookId
    @GetMapping("/libraries/{id}")
    public List<String> getLibrariesFromBook(@PathVariable("id")int id){
        return libraryService.findLibrariesByBookId(id);
    }

    @DeleteMapping("/library/{id}")
    public void deleteLibrary(@PathVariable("id")int id){
        libraryService.deleteLibrary(id);
    }

    @DeleteMapping("/{lib}/{id}")
    public void deleteBookFromLibrary(@PathVariable("lib")int libId,@PathVariable("id")int bookId){
        libraryService.removeBookFromLibrary(libId,bookId);
    }



    @PutMapping("/library/{id}")
    public void updateLibrary(@PathVariable("id")int id,@RequestBody Library lib){
        libraryService.updateLibrary(id,lib);
    }

    @PutMapping("/{library}/{book}")
    public void addBookToLibrary(@PathVariable("library") int libId,@PathVariable("book") int bookId){
       libraryService.addBookToLibrary(libId,bookId);
    }

}
