package com.example.book_api.controllers;

import com.example.book_api.dtos.DadosCadastroBook;
import com.example.book_api.dtos.DataDetailBooks;
import com.example.book_api.dtos.UpdatedDataBooks;
import com.example.book_api.services.BookService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> addBook(@RequestBody @Valid DadosCadastroBook dados, UriComponentsBuilder uriBuilder) throws Exception {
        bookService.addBook(dados);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<DataDetailBooks>> listBooks(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao){
        Page<DataDetailBooks> data = bookService.listBooks(paginacao);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataDetailBooks> listBooksById(@PathVariable Long id) throws Exception{
        DataDetailBooks book = bookService.listBooksById(id);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UpdatedDataBooks> updateBook(@PathVariable Long id,@RequestBody @Valid UpdatedDataBooks data) throws Exception {
        UpdatedDataBooks book = bookService.updateBook(id,data);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws Exception{
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
