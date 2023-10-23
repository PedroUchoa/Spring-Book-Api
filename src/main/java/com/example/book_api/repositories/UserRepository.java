package com.example.book_api.repositories;
import com.example.book_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
    
}
