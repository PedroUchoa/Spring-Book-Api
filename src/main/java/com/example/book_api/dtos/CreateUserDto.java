package com.example.book_api.dtos;


import com.example.book_api.entities.User;
import com.example.book_api.enums.Role;
import jakarta.validation.constraints.NotBlank;


public record CreateUserDto(

        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotBlank
        String name,
        Role role

) {

        public CreateUserDto(User user){
                this(user.getLogin(),user.getPassword(),user.getName(),user.getRole());
        }


}
