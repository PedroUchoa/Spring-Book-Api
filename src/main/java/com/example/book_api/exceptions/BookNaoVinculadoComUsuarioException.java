package com.example.book_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Livro não está vinculado com o usuario")
public class BookNaoVinculadoComUsuarioException extends Exception {
    public BookNaoVinculadoComUsuarioException(String book, String user) {
        super("O Livro " + book + " não está vinculado com o usuario " + user );
    }
}
