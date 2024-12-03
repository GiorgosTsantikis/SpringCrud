package com.example.demo.controllers;

import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.LibraryRepository;
import com.example.demo.services.BookService;
import com.example.demo.services.LibraryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("test")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;









    @Test
    public void getHello_ShouldReturnHelloWorld() throws Exception {
        // Perform GET request to the root endpoint
        this.mockMvc.perform(get("/"))
                .andDo(print())
                // Assert that the HTTP status is 200 OK
                .andExpect(status().isOk())
                // Assert that the response body contains "Hello, World"
                .andExpect(content().string(containsString("Hello, World")));
    }

    @Test
    public void getAllBooks_ShouldReturnBookList() throws Exception{

        ObjectMapper objectMapper=new ObjectMapper();

        ArrayList<Book>books=new ArrayList<>();
        Book book1=new Book("title","author",10);
        book1.setId(1);
        Book book2=new Book("title22","author2",10);
        book1.setId(2);
        books.add(book1);
        books.add(book2);
        when(bookService.getAllBooks()).thenReturn(books);

        MvcResult result=this.mockMvc.perform(get("/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String json=result.getResponse().getContentAsString();
        ArrayList<Book> resBooks=objectMapper.readValue(json,new TypeReference<>(){});
        assertNotNull(resBooks);
       for(int i=0;i<books.size();i++){
           assertEquals(books.get(i).getId(),resBooks.get(i).getId());
       }


    }

    @Test
    public void UpdateBookById_ShouldReturnNewBook() throws Exception{

        Book book=new Book("title","author",10);
        book.setId(1);
        Book newBook=new Book("new title","author",10);
        when(bookService.updateBook(anyInt(), any(Book.class))).thenReturn(newBook);


        ObjectMapper objectMapper=new ObjectMapper();
        String json="{"+
                "\"title\": \"new title\","+
                "\"author\": \"Joshua Bloch\","+
                "\"price\": 670"  +
"}";
        JSONObject jsonObject=new JSONObject(json);


        MvcResult result=this.mockMvc.perform(put("/books/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(content().string(containsString("new title")))

                .andReturn();
        String res=result.getResponse().getContentAsString();
        Book responseBook=objectMapper.readValue(res,Book.class);
        Assertions.assertEquals(responseBook.getTitle(), newBook.getTitle());

        System.out.println("\n\n++++"+result.getResponse().getContentAsString()+"\n\n+++++++++++");
    }



}

