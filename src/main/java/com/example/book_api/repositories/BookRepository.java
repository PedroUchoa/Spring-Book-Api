package com.example.book_api.repositories;

import com.example.book_api.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByName(String name);
}
