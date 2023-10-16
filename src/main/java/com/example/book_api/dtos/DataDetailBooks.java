package com.example.book_api.dtos;

import com.example.book_api.entities.Book;
import com.example.book_api.enums.Categories;

import java.util.List;

public record DataDetailBooks(Long id, String name, String author, String description, String image, List<Categories> categories) {

    public DataDetailBooks(Book book){
        this(book.getId(), book.getName(), book.getAuthor(), book.getDescription(), book.getImage(), book.getCategories());
    }

}
