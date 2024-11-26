package com.example.demo.services;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LibraryService(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    // Create a new library
    public Library createLibrary(Library library) {
        return libraryRepository.save(library);
    }

    // Get all libraries
    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }

    // Get a library by ID
    public Optional<Library> getLibraryById(int id) {
        return libraryRepository.findById(id);
    }

    // Add a book to a library
    public Library addBookToLibrary(int libraryId, int bookId) {
        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (libraryOptional.isPresent() && bookOptional.isPresent()) {
            Library library = libraryOptional.get();
            Book book = bookOptional.get();

            // Check if the book is already in the library
            if (!library.getBooks().contains(book)) {
                library.getBooks().add(book);
                libraryRepository.save(library);
            } else {
                throw new RuntimeException("Book already exists in this library.");
            }
            return library;
        } else {
            throw new RuntimeException("Library or Book not found");
        }
    }

    // Remove a book from a library
    public Library removeBookFromLibrary(int libraryId, int bookId) {
        Optional<Library> libraryOptional = libraryRepository.findById(libraryId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (libraryOptional.isPresent() && bookOptional.isPresent()) {
            Library library = libraryOptional.get();
            Book book = bookOptional.get();

            // Check if the book exists in the library
            if (library.getBooks().contains(book)) {
                library.getBooks().remove(book);
                libraryRepository.save(library);
            } else {
                throw new RuntimeException("Book does not exist in this library.");
            }
            return library;
        } else {
            throw new RuntimeException("Library or Book not found");
        }
    }

    // Update library details (for example, changing the library's name)


    // Delete a library by ID
    public void deleteLibrary(int id) {
        libraryRepository.deleteById(id);
    }
}
