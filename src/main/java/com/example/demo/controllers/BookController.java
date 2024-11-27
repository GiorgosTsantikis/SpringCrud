package com.example.demo.controllers;

import com.example.demo.entities.Book;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/books")
    public int addBook(@RequestBody Book book){
        bookService.createBook(book);
        return book.getId();
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id")int id){
        bookService.deleteBook(id);
    }


}
