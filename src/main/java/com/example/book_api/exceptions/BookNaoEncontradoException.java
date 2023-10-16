package com.example.book_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Livro não encontrado na base")
public class BookNaoEncontradoException extends Exception{

    public BookNaoEncontradoException(Long id){
        super("Livro com o id: " + id + " não encontrado na base de dados");
    }

}
