package com.example.book_api.dtos;


import com.example.book_api.enums.Role;
import jakarta.validation.constraints.NotBlank;


public record DataCreateUser(

        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotBlank
        String name,
        Role role

) {
}
