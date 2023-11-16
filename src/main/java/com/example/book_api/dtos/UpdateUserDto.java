package com.example.book_api.dtos;

import com.example.book_api.entities.User;

public record UpdateUserDto(
        String login,
        String password,
        String name
) {

    public UpdateUserDto(User user){
        this(user.getLogin(), user.getPassword(), user.getName());
    }

}
