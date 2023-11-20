package com.example.book_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Não se pode adicionar dois livros iguais")
public class DuplicatedBookException extends Exception{

    public DuplicatedBookException(String name){
        super("Já existe um livro com esse nome: " + name+ " adicionado ao sistema");
    }

}
