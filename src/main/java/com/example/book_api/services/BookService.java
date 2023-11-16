package com.example.book_api.services;

import com.example.book_api.dtos.CreateBookDto;
import com.example.book_api.dtos.DetailsBookDto;
import com.example.book_api.dtos.UpdateBookDto;
import com.example.book_api.entities.Book;
import com.example.book_api.exceptions.BookNaoEncontradoException;
import com.example.book_api.exceptions.DuplicatedBookException;
import com.example.book_api.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void addBook(CreateBookDto dados) throws DuplicatedBookException {
        Book book = new Book(dados);
        if(!bookRepository.findByName(dados.name()).isEmpty()){
            throw new DuplicatedBookException(dados.name());
        }
        bookRepository.save(book);
    }

    public Page<DetailsBookDto> listBooks(Pageable paginacao){
        Page<DetailsBookDto> data = bookRepository.findAll(paginacao).map(DetailsBookDto::new);
        return data;
    }

    public DetailsBookDto listBooksById(String id) throws BookNaoEncontradoException {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNaoEncontradoException(id));
        return new DetailsBookDto(book);
    }

    public UpdateBookDto updateBook(String id, UpdateBookDto data) throws BookNaoEncontradoException {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNaoEncontradoException(id));
        book.updateInfos(data);
        return new UpdateBookDto(book);
    }

    public void deleteBook(String id) throws BookNaoEncontradoException {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNaoEncontradoException(id));
        book.getUserList().forEach(user -> {
            user.getBooks().remove(book);
        });
        bookRepository.deleteById(id);
    }

}
