package com.example.demo.repositories;

import com.example.demo.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library,Integer> {
    @Query(value = "SELECT * FROM library " +
            "JOIN library_book ON library.id = library_book.library_id " +
            "WHERE library_book.book_id = :bookId",
            nativeQuery = true)
    List<Library> findLibrariesByBookId(@Param("bookId") int bookId);
}
