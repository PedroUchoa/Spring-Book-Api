package com.example.book_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNaoEncontradoException.class)
    public ResponseEntity<String> handleBookNaoEncontradoException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage() + "\n " +ex.getClass());
    }

    @ExceptionHandler(DuplicatedBookException.class)
    public ResponseEntity<String> handleDuplicatedBookException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage() + "\n" +ex.getClass());
    }

    @ExceptionHandler(UserNaoEncontradoException.class)
    public ResponseEntity<String> handleUserNaoEncontradoException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage() + "\n" +ex.getClass());
    }

    @ExceptionHandler(BookNaoVinculadoComUsuarioException.class)
    public ResponseEntity<String> handleBookNaoVinculadoComUsuarioException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage() + "\n" +ex.getClass());
    }

    @ExceptionHandler(DuplicatedLoginException.class)
    public ResponseEntity<String> handleDuplicatedLoginException(Exception ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage() + "\n" +ex.getClass());
    }


}

