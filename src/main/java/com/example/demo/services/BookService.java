package com.example.demo.services;

import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Update a book
    public Book updateBook(int id, Book bookDetails) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            bookToUpdate.setTitle(bookDetails.getTitle());
            bookToUpdate.setAuthor(bookDetails.getAuthor());
            return bookRepository.save(bookToUpdate);
        } else {
            throw new RuntimeException("Book not found with id " + id);
        }
    }

    // Delete a book by ID
    public void deleteBook(int id) {

        bookRepository.deleteFromJoinTable(id);
        bookRepository.deleteBook(id);
    }

}
