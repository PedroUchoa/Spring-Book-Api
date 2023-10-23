package com.example.book_api.dtos;

import com.example.book_api.entities.Book;
import com.example.book_api.entities.User;


import java.util.Set;
import java.util.UUID;

public record DataDetailUser(String id, String login, String Name, Set<Book> books) {

    public DataDetailUser(User user){
        this(user.getId(),user.getLogin(),user.getName(),user.getBooks());
    }

}
