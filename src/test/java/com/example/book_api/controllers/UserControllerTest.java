package com.example.book_api.controllers;


import com.example.book_api.dtos.CreateUserDto;
import com.example.book_api.dtos.DetailUserDto;
import com.example.book_api.dtos.DetailsBookDto;
import com.example.book_api.dtos.UpdateUserDto;
import com.example.book_api.entities.Book;
import com.example.book_api.entities.User;

import com.example.book_api.enums.Categories;
import com.example.book_api.enums.Role;

import com.example.book_api.exceptions.*;
import com.example.book_api.repositories.UserRepository;
import com.example.book_api.services.BookService;
import com.example.book_api.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("deve criar um usuario com sucesso")
    void createUserSuccess() throws Exception {
        User user = this.buildMockUser();

        when(userService.createUser(new CreateUserDto(user))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateUserDto> userCaptor = ArgumentCaptor.forClass(CreateUserDto.class);
        verify(userService,times(1)).createUser(userCaptor.capture());
        assertEquals(userCaptor.getValue().login(),user.getLogin());
    }

    @Test
    @DisplayName("deve devolver um erro ao criar um usuario duplicado")
    void createUserError() throws Exception {
        User user = this.buildMockUser();

        when(userService.createUser(new CreateUserDto(user))).thenThrow(new DuplicatedLoginException(user.getLogin()));

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        ArgumentCaptor<CreateUserDto> userCaptor = ArgumentCaptor.forClass(CreateUserDto.class);
        verify(userService,times(1)).createUser(userCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar OK ao buscar todos os usuaros com sucesso")
    void getAllUsersSuccess() throws Exception {
        User user = this.buildMockUser();
        List<DetailUserDto> list =Arrays.asList(new DetailUserDto(user));
        Page<DetailUserDto> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());


        when(userService.getAllUsers(PageRequest.of(0,10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("page","0")
                        .param("size","10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService).getAllUsers(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar OK ao buscar por um usuario com id correto")
    void listUserByIdSuccess() throws Exception {
        DetailUserDto user = new DetailUserDto(this.buildMockUser());

        when(userService.listUsersById(user.id())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}",user.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao buscar um usuario pelo id errado")
    void listUserByIdError() throws Exception {
        User user = this.buildMockUser();

        when(userService.listUsersById(user.getId())).thenThrow(new UserNaoEncontradoException(user.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

   @Test
   @DisplayName("Deve retornar NO CONTENT ao deletar um usuario com sucesso")
   void deleteUserSuccess() throws Exception {
        User user = this.buildMockUser();
        doNothing().when(userService).deleteAnUser(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{1}",user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
   }

    @Test
    @DisplayName("Deve retornar NO FOUND ao deletar um usuario com sucesso")
    void deleteUserError() throws Exception {
        User user = this.buildMockUser();

        doThrow(new UserNaoEncontradoException(user.getId())).when(userService).deleteAnUser(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{1}",user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService,times(1)).deleteAnUser(user.getId());
    }

    @Test
    @DisplayName("Deve retornar OK atualizar um usuario com sucesso")
    void updateUserSuccess() throws Exception {
        User user = this.buildMockUser();
        UpdateUserDto updateUserData = new UpdateUserDto("pedro","pedro","pedro");


        when(userService.updateUser(user.getId(),updateUserData)).thenReturn(updateUserData);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}",user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserData)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("login").value("pedro"))
                .andExpect(MockMvcResultMatchers.jsonPath("password").value("pedro"))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("pedro"));
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND atualizar um usuario com id errado")
    void updateUserError() throws Exception{
        User user = this.buildMockUser();
        UpdateUserDto update = new UpdateUserDto("novo","novo","Novo");

        doThrow(new UserNaoEncontradoException(user.getId())).when(userService).updateUser(user.getId(),update);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}",user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService).updateUser(user.getId(),update);

    }

    @Test
    @DisplayName("Deve retornar NO CONTENT ao adicionar um livro ao usuario com sucesso")
    void addBookToUserSuccess() throws Exception {
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        when(userService.listUsersById(user.getId())).thenReturn(new DetailUserDto(user));
        when(bookService.listBooksById(user.getId())).thenReturn(new DetailsBookDto(book));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/add/{id}/{book}",user.getId(),book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(userService).addBookToUser(user.getId(), book.getId());
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao tentar adicionar com id do livro invalido")
    void addBookToUserErrorCaseOne() throws Exception{
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        doThrow(new BookNaoEncontradoException(book.getId())).when(userService).addBookToUser(user.getId(),book.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/add/{id}/{book}",user.getId(),book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Livro com o id: " + book.getId() + " não encontrado na base de dados\n" +" " + BookNaoEncontradoException.class.toString()));

        verify(userService).addBookToUser(user.getId(), book.getId());
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao tentar adicionar livro com id do usuario invalido")
    void addBookToUserErrorCaseTwo() throws Exception{
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        doThrow(new UserNaoEncontradoException(book.getId())).when(userService).addBookToUser(user.getId(),book.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/add/{id}/{book}",user.getId(),book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Usuario com o id: " + user.getId() + " não encontrado no banco de dados\n" + UserNaoEncontradoException.class.toString()));

        verify(userService).addBookToUser(user.getId(), book.getId());

    }

    @Test
    @DisplayName("Deve retornar um NO CONTENT ao remover um livro de um usuario com sucesso")
    void removeBookFromUserSucces() throws Exception {
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        when(userService.listUsersById(user.getId())).thenReturn(new DetailUserDto(user));
        when(bookService.listBooksById(user.getId())).thenReturn(new DetailsBookDto(book));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{id}/{book}",user.getId(),book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(userService).removeBookFromUser(user.getId(), book.getId());
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao tentar remover livro com id do livro invalido")
    void removeBookFromUserErrorCaseOne() throws Exception{
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        doThrow(new BookNaoEncontradoException(book.getId())).when(userService).removeBookFromUser(user.getId(),book.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{id}/{book}",user.getId(),book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Livro com o id: " + book.getId() + " não encontrado na base de dados\n" +" " + BookNaoEncontradoException.class.toString()));

        verify(userService).removeBookFromUser(user.getId(), book.getId());
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao tentar remover livro com id do usuario invalido")
    void removeBookFromUserErrorCaseTwo() throws Exception{
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        doThrow(new UserNaoEncontradoException(book.getId())).when(userService).removeBookFromUser(user.getId(),book.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{id}/{book}",user.getId(),book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Usuario com o id: " + user.getId() + " não encontrado no banco de dados\n" + UserNaoEncontradoException.class.toString()));

        verify(userService).removeBookFromUser(user.getId(), book.getId());
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao tentar remover um livro que não esta vinculado com o user")
    void removeBookFromUserErrorCaseThree() throws Exception{
        User user = this.buildMockUser();
        Book book = this.buildMockBook();

        doThrow(new BookNaoVinculadoComUsuarioException(book.getId(),user.getId())).when(userService).removeBookFromUser(user.getId(),book.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{id}/{book}",user.getId(),book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("O Livro " + book.getId() + " não está vinculado com o usuario " + user.getId()+"\n" + BookNaoVinculadoComUsuarioException.class.toString()));

        verify(userService).removeBookFromUser(user.getId(), book.getId());
    }

    private User buildMockUser(){
        User user= new User();
        user.setId("1");
        user.setLogin("testetesteteste");
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