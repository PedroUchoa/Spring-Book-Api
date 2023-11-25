package com.example.book_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Já existe um usuario com esse login")
public class DuplicatedLoginException extends RuntimeException{

    public DuplicatedLoginException(String login){
        super("Já existe um usuario com o login: " + login +". Pfv escolha outro login");
    }

}
