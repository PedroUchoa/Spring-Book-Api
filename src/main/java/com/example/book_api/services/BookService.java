package com.example.book_api.services;

import com.example.book_api.dtos.DadosCadastroBook;
import com.example.book_api.dtos.DataDetailBooks;
import com.example.book_api.dtos.UpdatedDataBooks;
import com.example.book_api.entities.Book;
import com.example.book_api.exceptions.BookNaoEncontradoException;
import com.example.book_api.exceptions.DuplicatedBookException;
import com.example.book_api.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void addBook(DadosCadastroBook dados) throws DuplicatedBookException {
        Book book = new Book(dados);
        if(!bookRepository.findByName(dados.name()).isEmpty()){
            throw new DuplicatedBookException(dados.name());
        }
        bookRepository.save(book);
    }

    public Page<DataDetailBooks> listBooks(Pageable paginacao){
        Page<DataDetailBooks> data = bookRepository.findAll(paginacao).map(DataDetailBooks::new);
        return data;
    }

    public DataDetailBooks listBooksById(String id) throws BookNaoEncontradoException {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNaoEncontradoException(id));
        return new DataDetailBooks(book);
    }

    public UpdatedDataBooks updateBook(String id, UpdatedDataBooks data) throws BookNaoEncontradoException {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNaoEncontradoException(id));
        book.updateInfos(data);
        return new UpdatedDataBooks(book);
    }

    public void deleteBook(String id) throws BookNaoEncontradoException {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNaoEncontradoException(id));
        book.getUserList().forEach(user -> {
            user.getBooks().remove(book);
        });
        bookRepository.deleteById(id);
    }

}
