package com.example.book_api.services;

import com.example.book_api.enums.Categories;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoriesService {

    public List<Categories> bookCategories(){
        return Arrays.asList(Categories.values());
    }


}
