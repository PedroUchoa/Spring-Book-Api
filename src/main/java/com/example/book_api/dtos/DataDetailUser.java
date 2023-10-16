package com.example.book_api.dtos;

import com.example.book_api.entities.Book;
import com.example.book_api.entities.User;

import java.util.List;
import java.util.Set;

public record DataDetailUser(Long id, String login, String Name, Set<Book> books) {

    public DataDetailUser(User user){
        this(user.getId(),user.getLogin(),user.getName(),user.getBooks());
    }

}
