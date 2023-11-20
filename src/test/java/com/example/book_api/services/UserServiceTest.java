package com.example.book_api.services;

import com.example.book_api.dtos.CreateUserDto;
import com.example.book_api.dtos.DetailUserDto;
import com.example.book_api.dtos.UpdateUserDto;
import com.example.book_api.entities.Book;
import com.example.book_api.entities.User;
import com.example.book_api.enums.Categories;
import com.example.book_api.enums.Role;
import com.example.book_api.exceptions.BookNaoEncontradoException;
import com.example.book_api.exceptions.BookNaoVinculadoComUsuarioException;
import com.example.book_api.exceptions.DuplicatedLoginException;
import com.example.book_api.exceptions.UserNaoEncontradoException;
import com.example.book_api.repositories.BookRepository;
import com.example.book_api.repositories.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("Deve salvar um estudante na base com sucesso")
    void createUserSuccess() throws DuplicatedLoginException {
        CreateUserDto user = new CreateUserDto(this.buildMockUser());

        this.userService.createUser(user);

        verify(userRepository,times(1)).save(new User(user));
    }

    @Test
    @DisplayName("Deve jogar uma exceção quando o usuario é invalido")
    void createUserError() throws DuplicatedLoginException {
       User user = this.buildMockUser();

       when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

       DuplicatedLoginException excep = assertThrows(DuplicatedLoginException.class,()->userService.createUser(new CreateUserDto(user)));

       assertEquals("Já existe um usuario com o login: " + user.getLogin() +". Pfv escolha outro login",excep.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um usuario por Id com sucesso")
    void listUsersByIdSuccess() throws UserNaoEncontradoException {
        User user = this.buildMockUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        DetailUserDto findUser = userService.listUsersById(user.getId());

        assertEquals(new DetailUserDto(user),findUser);
        verify(userRepository,times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Deve jogar um exceção ao buscar por id inexistente")
    void listUsersByIdError(){
        User user = this.buildMockUser();

        UserNaoEncontradoException excep = assertThrows(UserNaoEncontradoException.class, ()-> userService.listUsersById(user.getId()));

        assertEquals("Usuario com o id: " + user.getId() + " não encontrado no banco de dados",excep.getMessage());
    }


    @Test
    @DisplayName("Deve atualizar um usuario com sucsso")
    void updateUserSuccess() throws UserNaoEncontradoException {
        User user = this.buildMockUser();
        given(userRepository.findById("1")).willReturn(Optional.of(user));

        UpdateUserDto updateUser = new UpdateUserDto("teste2", passwordEncoder.encode("teste2"),"teste2");
        UpdateUserDto result = userService.updateUser(user.getId(),updateUser);

        assertThat(result.login()).isEqualTo(updateUser.login());
        assertThat(result.password()).isEqualTo(updateUser.password());
        assertThat(result.name()).isEqualTo(updateUser.name());
    }
    @Test
    @DisplayName("Deve jogar uma exceção ao buscar por id inexistente para atualizar")
    void updateUserError(){
        User user = this.buildMockUser();

        UserNaoEncontradoException excep = assertThrows(UserNaoEncontradoException.class, ()-> userService.updateUser(user.getId(),new UpdateUserDto(user)));

        assertEquals("Usuario com o id: " + user.getId() + " não encontrado no banco de dados",excep.getMessage());
    }

    @Test
    @DisplayName("Deve deletar um usuario com sucesso")
    void deleteAnUserSuccess() throws UserNaoEncontradoException {
        User user = this.buildMockUser();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        userService.deleteAnUser(user.getId());

        verify(userRepository,times(1)).deleteById(user.getId());
    }

    @Test
    @DisplayName("Deve jogar uma exceção ao buscar por id inexistente para deletar")
    void deleteAnUserError(){
        User user = this.buildMockUser();

        UserNaoEncontradoException excep = assertThrows(UserNaoEncontradoException.class, ()-> userService.deleteAnUser(user.getId()));

        assertEquals("Usuario com o id: " + user.getId() + " não encontrado no banco de dados",excep.getMessage());
    }

    @Test
    @DisplayName("Deve adicionar um livro ao usuario com sucesso")
    void addBookToUserSuccess() throws UserNaoEncontradoException, BookNaoEncontradoException {
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        userService.addBookToUser(user.getId(),book.getId());
        DetailUserDto result = userService.listUsersById(user.getId());

        assertEquals(result.books().size(), 1);
    }

    @Test
    @DisplayName("Deve jogar uma exceção ao buscar por id de um livro inexistente")
    void addBookToUserErrorCaseOne(){
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        BookNaoEncontradoException excep = assertThrows(BookNaoEncontradoException.class, () -> userService.addBookToUser(user.getId(),book.getId()));

        assertEquals("Livro com o id: " + user.getId() + " não encontrado na base de dados",excep.getMessage());
        verify(bookRepository,times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Deve jogar uma exceção ao buscar por id de um usuario inexistente")
    void addBookToUserErrorCaseTwo(){
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        UserNaoEncontradoException excep = assertThrows(UserNaoEncontradoException.class, () -> userService.addBookToUser(user.getId(),book.getId()));

        assertEquals("Usuario com o id: " + user.getId() + " não encontrado no banco de dados",excep.getMessage());
        verify(userRepository,times(1)).findById(user.getId());
    }


    @Test
    @DisplayName("Deve remover um livro do usuario com sucesso")
    void removeBookFromUserSuccess() throws UserNaoEncontradoException, BookNaoEncontradoException, BookNaoVinculadoComUsuarioException {
        User user = this.buildMockUser();
        Book book = this.buildMockBook();
        user.getBooks().add(book);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        DetailUserDto after = userService.listUsersById(user.getId());
        assertEquals(after.books().size(),1);

        userService.removeBookFromUser(user.getId(),book.getId());
        DetailUserDto result = userService.listUsersById(user.getId());

        assertEquals(result.books().size(),0);
    }

    @Test
    @DisplayName("Deve jogar um exceção quando buscar um livro para remoção pelo id invalido")
    void removeBookFromUserErrorCaseOne(){
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        BookNaoEncontradoException excep = assertThrows(BookNaoEncontradoException.class, () -> userService.removeBookFromUser(user.getId(),book.getId()));

        assertEquals("Livro com o id: " + user.getId() + " não encontrado na base de dados",excep.getMessage());
        verify(bookRepository,times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Deve jogar um exceção quando buscar um usuario para remoção pelo id invalido")
    void removeBookFromUserErrorCaseTwo(){
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        UserNaoEncontradoException excep = assertThrows(UserNaoEncontradoException.class, () -> userService.removeBookFromUser(user.getId(),book.getId()));

        assertEquals("Usuario com o id: " + user.getId() + " não encontrado no banco de dados",excep.getMessage());
        verify(userRepository,times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Deve jogar um exceção quando buscar um usuario para remoção pelo id invalido")
    void removeBookFromUserErrorCaseThree(){
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        BookNaoVinculadoComUsuarioException excep = assertThrows(BookNaoVinculadoComUsuarioException.class, () -> userService.removeBookFromUser(user.getId(),book.getId()));

        assertEquals("O Livro " + book.getName() + " não está vinculado com o usuario " + user.getName(),excep.getMessage());
        verify(userRepository,times(1)).findById(user.getId());
        verify(bookRepository,times(1)).findById(book.getId());

    }


    private User buildMockUser(){
        User user= new User();
        user.setId("1");
        user.setLogin("teste");
        user.setPassword("teste");
        user.setName("teste");
        user.setRole(Role.USER);
        return user;
    }

    private Book buildMockBook(){
        Book book = new Book();
        List<Categories> categories = new ArrayList<>();
        categories.add(Categories.ROMANCE);
        book.setName("teste");
        book.setId("1");
        book.setAuthor("teste");
        book.setImage("teste");
        book.setDescription("teste");
        book.setCategories(categories);
        return book;
    }


}