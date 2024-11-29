package com.example.demo.services;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.LibraryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Book book;
    private Library library;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
         library=new Library("the library");
        library.setId(1);
         book=new Book("title","author",10);
        book.setId(1);
    }

    @Test
    public void addBookToLibrary_ShouldReturnLibrary(){

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(libraryRepository.findById(anyInt())).thenReturn(Optional.of(library));
        when(libraryRepository.save(any(Library.class))).thenReturn(library);

        //Correct insertion
        libraryService.addBookToLibrary(1,1);
        Assertions.assertEquals(library.getBooks().get(0).getId(),book.getId(),"First book of library should be book with id of 1");

        //Re-inserting the same book to reach nested else condition
        Assertions.assertThrows(RuntimeException.class,()->libraryService.addBookToLibrary(1,1),"We already added book with id of 1 ");

        //Providing an empty optional to reach outer else condition
        when(libraryRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,()->libraryService.addBookToLibrary(1,1),"Library should be null");
    }

    @Test
    public void removeBookFromLibrary_ShouldReturnLibrary(){

        when(libraryRepository.findById(anyInt())).thenReturn(Optional.of(library));
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(libraryRepository.save(any(Library.class))).thenReturn(library);

        library.getBooks().add(book);
        libraryService.removeBookFromLibrary(1,1);

        //Checking library is empty after delete
        Assertions.assertTrue(library.getBooks().isEmpty(),"Library should be empty");

        //Deleting a book that isn't contained in the library, to reach inner else condition
        Assertions.assertThrows(RuntimeException.class,()->libraryService.removeBookFromLibrary(1,1),"Book should not be in library");

        //Providing an empty optional to reach outer else condition
        when(libraryRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,()->libraryService.removeBookFromLibrary(1,1));

    }
}
