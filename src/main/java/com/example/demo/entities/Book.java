package com.example.demo.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="BOOK")
public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 6909884030121410992L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private int id;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="price")
    private int price;

    @ManyToMany(mappedBy = "books") // This is the inverse side of the relationship
    private List<Library> libraries = new ArrayList<>();

    // Constructors
    public Book() {}

    public Book(String title, String author,int price) {
        this.title = title;
        this.author = author;
        this.price=price;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }
}
