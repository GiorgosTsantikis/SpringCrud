package com.example.demo.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "library_book", // The name of the join table
            joinColumns = @JoinColumn(name = "library_id"), // Foreign key column for Library
            inverseJoinColumns = @JoinColumn(name = "book_id") // Foreign key column for Book
    )
    private List<Book> books = new ArrayList<>();

    public Library() {}

    public Library(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
