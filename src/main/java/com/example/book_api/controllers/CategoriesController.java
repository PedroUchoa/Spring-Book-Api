package com.example.book_api.controllers;

import com.example.book_api.enums.Categories;
import com.example.book_api.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<Categories>> bookCategories(){
        List<Categories> categories = categoriesService.bookCategories();
        return ResponseEntity.ok().body(categories);
    }

}
