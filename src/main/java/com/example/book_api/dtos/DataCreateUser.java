package com.example.book_api.dtos;


import jakarta.validation.constraints.NotBlank;


public record DataCreateUser(

        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotBlank
        String name

) {
}
