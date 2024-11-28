package com.example.demo;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.services.BookService;
import com.example.demo.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

/*
	@Autowired
	private LibraryService libraryService;
	@Autowired
	private BookService bookService;

 */



	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		/*
		Library lib1=new Library("lib1");
		Library lib2=new Library("lib2");
		Library lib3=new Library("lib3");
		Book book1=new Book("book1","1",10);
		Book book2=new Book("book2","2",20);
		Book book3=new Book("book1","3",30);
		bookService.createBook(book1);
		bookService.createBook(book2);
		bookService.createBook(book3);

		libraryService.createLibrary(lib1);
		libraryService.createLibrary(lib2);
		libraryService.createLibrary(lib3);
		libraryService.addBookToLibrary(lib1.getId(),book1.getId());
		libraryService.addBookToLibrary(lib2.getId(),book1.getId());
		libraryService.addBookToLibrary(lib3.getId(),book1.getId());

		 */








	}
}
