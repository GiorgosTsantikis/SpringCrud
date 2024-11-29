package com.example.demo.repositories;

import com.example.demo.entities.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM library_book WHERE book_id = :bookId", nativeQuery = true)
    void deleteFromJoinTable(@Param("bookId") int bookId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM book WHERE book_id = :bookId", nativeQuery = true)
    void deleteBook(@Param("bookId") int bookId);
}
