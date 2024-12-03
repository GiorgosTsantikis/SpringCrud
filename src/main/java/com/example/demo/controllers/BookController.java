package com.example.demo.controllers;

import com.example.demo.entities.Book;
import com.example.demo.entities.Listing;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService=bookService;
    }

    @PostMapping("/books")
    public int addBook(@RequestBody Book book){
        bookService.createBook(book);
        return book.getId();
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id")int id){
        bookService.deleteBook(id);
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable("id")int id,@RequestBody Book book){
        return bookService.updateBook(id,book);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/")
    public String homeRoute(){
        return "Hello, World";
    }




}
