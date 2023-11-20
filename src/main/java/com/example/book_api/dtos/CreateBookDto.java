package com.example.book_api.dtos;
import com.example.book_api.entities.Book;
import com.example.book_api.enums.Categories;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateBookDto(
        @NotBlank
        String name,
        @NotBlank
        String author,
        @NotBlank
        String description,
        String image,
        @NotNull
        @Enumerated
        List<Categories> categories
) {

    public CreateBookDto(Book book){
            this(book.getName(), book.getAuthor(), book.getDescription(), book.getImage(), book.getCategories());
    }


}
