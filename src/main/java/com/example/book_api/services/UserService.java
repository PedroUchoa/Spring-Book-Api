
package com.example.book_api.services;

import com.example.book_api.dtos.*;
import com.example.book_api.entities.Book;
import com.example.book_api.entities.User;
import com.example.book_api.exceptions.BookNaoEncontradoException;
import com.example.book_api.exceptions.BookNaoVinculadoComUsuarioException;
import com.example.book_api.exceptions.DuplicatedLoginException;
import com.example.book_api.exceptions.UserNaoEncontradoException;
import com.example.book_api.repositories.BookRepository;
import com.example.book_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createUser(CreateUserDto data) throws DuplicatedLoginException {
        User user = new User(data);
        if(!userRepository.findByLogin(user.getLogin()).isEmpty()){
           throw new DuplicatedLoginException(data.login());
        }
        user.setPassword(passwordEncoder.encode(data.password()));
        return userRepository.save(user);

    }

    public Page<DetailUserDto> getAllUsers(Pageable paginacao){
       Page<DetailUserDto> users = userRepository.findAll(paginacao).map(DetailUserDto::new);
       return users;
    }

    public DetailUserDto listUsersById(String id) throws UserNaoEncontradoException {
        User user = userRepository.findById(id).orElseThrow(()->new UserNaoEncontradoException(id));
        return new DetailUserDto(user);
    }

    public UpdateUserDto updateUser(String id, UpdateUserDto data) throws UserNaoEncontradoException {
        User user = userRepository.findById(id).orElseThrow(()->new UserNaoEncontradoException(id));
        user.updateUser(data);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new UpdateUserDto(user);
    }

    public void deleteAnUser(String id) throws UserNaoEncontradoException {
        userRepository.findById(id).orElseThrow(()->new UserNaoEncontradoException(id));
        userRepository.deleteById(id);

    }

    public void addBookToUser(String userId, String bookId) throws BookNaoEncontradoException, UserNaoEncontradoException {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookNaoEncontradoException(bookId));
        User user = userRepository.findById(userId). orElseThrow(()-> new UserNaoEncontradoException(userId));
        user.getBooks().add(book);

    }

    public void removeBookFromUser(String userId, String bookId) throws BookNaoEncontradoException, UserNaoEncontradoException, BookNaoVinculadoComUsuarioException {
        Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNaoEncontradoException(bookId));
        User user = userRepository.findById(userId).orElseThrow(()->new UserNaoEncontradoException(userId));
        if(!user.getBooks().contains(book)){
            throw new BookNaoVinculadoComUsuarioException(book.getName(), user.getName());
        }
        user.getBooks().remove(book);
    }



}
