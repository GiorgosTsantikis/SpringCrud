package com.example.demo.controllers;


import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.services.BookService;
import com.example.demo.services.LibraryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibraryService libraryService;

    @MockitoBean
    private BookService bookService;

    @Test
    public void getLibraryWithId_ShouldReturnLibraryBookNames() throws Exception{
        //call /library/{id}
        Library library=new Library("libname");
        library.setId(1);
        Book book1=new Book("title","author",10);
        Book book2=new Book("2title","2author",20);
        library.getBooks().add(book1);
        library.getBooks().add(book2);
        //libraryService.getLibraryById(id)
        when(libraryService.getLibraryById(anyInt())).thenReturn(Optional.of(library));

        //GET Request too /library/1, where library with id of 1 contains 2 books
        MvcResult result=this.mockMvc.perform(get("/library/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultString=result.getResponse().getContentAsString();
        ObjectMapper objectMapper=new ObjectMapper();
        //Map response body to a list of strings
        List<String> books= objectMapper.readValue(resultString, new TypeReference<List<String>>() {});
        //Assert both book titles
        Assertions.assertEquals(books.get(0),book1.getTitle());
        Assertions.assertEquals(books.get(1),book2.getTitle());

        //GET Request for non-existent library, returns ["null"]
        when(libraryService.getLibraryById(anyInt())).thenReturn(Optional.empty());

        MvcResult result2=this.mockMvc.perform(get("/library/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        resultString=result2.getResponse().getContentAsString();
        books= objectMapper.readValue(resultString, new TypeReference<List<String>>() {});
        //check for ["null"]
        Assertions.assertEquals(books.get(0),"null");



    }
}
