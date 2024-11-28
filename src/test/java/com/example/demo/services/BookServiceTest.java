package com.example.demo.services;


import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookServiceTest {


    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllBooks_ShouldReturnBooks(){
        Book book1 = new Book("Book 1", "Author 1", 10);
        book1.setId(1);
        Book book2 = new Book("Book 2", "Author 2", 15);
        book2.setId(2);

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);  // Mock the behavior of the repository


        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size(), "There should be 2 books in the list");
        assertEquals("Book 1", result.get(0).getTitle(), "The title of the first book should be 'Book 1'");
    }

    @Test
    public void deleteBook_ShouldDeleteBook() {
        int bookId = 1;

        // Act
        bookService.deleteBook(bookId);

        // Assert: Verify that the delete method was called with the correct argument
        verify(bookRepository, times(1)).deleteFromJoinTable(bookId);
        verify(bookRepository, times(1)).deleteBook(bookId);
    }

}
