package com.example.book_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Usuario nao encontrado no banco de dados")
public class UserNaoEncontradoException extends Exception{

    public UserNaoEncontradoException(String id){
        super("Usuario com o id: " + id + " não encontrado no banco de dados");
    }

}
