package com.example.book_api.dtos;

import com.example.book_api.entities.Book;
import com.example.book_api.enums.Categories;

import java.util.List;

public record UpdatedDataBooks(
        String name,
        String author,
        String description,
        String image,
        List<Categories> categories
) {
        public UpdatedDataBooks(Book book) {
                this(book.getName(), book.getAuthor(), book.getDescription(), book.getImage(), book.getCategories());
        }
}
