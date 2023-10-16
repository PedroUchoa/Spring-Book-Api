package com.example.book_api.dtos;

import com.example.book_api.entities.User;

public record UpdateDataUser(
        String login,
        String password,
        String name
) {

    public UpdateDataUser(User user){
        this(user.getLogin(), user.getPassword(), user.getName());
    }

}
