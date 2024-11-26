package com.example.demo.services;

import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Create a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by ID
    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    // Update a book (only updates fields that are provided)
    public Book updateBook(int id, Book bookDetails) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            bookToUpdate.setTitle(bookDetails.getTitle());
            bookToUpdate.setAuthor(bookDetails.getAuthor());
            // Add other fields as necessary
            return bookRepository.save(bookToUpdate);
        } else {
            throw new RuntimeException("Book not found with id " + id);
        }
    }

    // Delete a book by ID
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

}
