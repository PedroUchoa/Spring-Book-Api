package com.example.book_api.controllers;

import com.example.book_api.dtos.*;
import com.example.book_api.entities.Book;
import com.example.book_api.entities.User;
import com.example.book_api.enums.Categories;
import com.example.book_api.exceptions.BookNaoEncontradoException;
import com.example.book_api.exceptions.DuplicatedBookException;
import com.example.book_api.exceptions.DuplicatedLoginException;
import com.example.book_api.exceptions.UserNaoEncontradoException;
import com.example.book_api.repositories.BookRepository;
import com.example.book_api.services.BookService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar Created ao criar um livro com sucesso")
    void addBookSuccess() throws Exception {
        Book book = this.buildMockBook();

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateBookDto> userCaptor = ArgumentCaptor.forClass(CreateBookDto.class);
        verify(bookService,times(1)).addBook(userCaptor.capture());
        assertEquals(userCaptor.getValue().name(),book.getName());
    }

    @Test
    @DisplayName("Deve retornar CONFLICT ao tentar criar um livro com o mesmo nome")
    void addBookError() throws Exception{
        Book book = this.buildMockBook();

        doThrow(new DuplicatedBookException(book.getName())).when(bookService).addBook(new CreateBookDto(book));

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar um OK ao buscar todos os livros com sucesso")
    void listBooksSuccess() throws Exception {
        Book book = this.buildMockBook();
        List<DetailsBookDto> list = Arrays.asList(new DetailsBookDto(book));
        Page<DetailsBookDto> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());


        when(bookService.listBooks(PageRequest.of(0,10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                        .param("page","0")
                        .param("size","10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bookService).listBooks(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar OK ao buscar um livro com id no banco de dados")
    void listBooksByIdSuccess() throws Exception{
        DetailsBookDto book = new DetailsBookDto(this.buildMockBook());

        when(bookService.listBooksById(book.id())).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}",book.id())
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));

        verify(bookService).listBooksById("1");
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao buscar um livro com id invalido no banco de dados")
    void listBooksByIdError() throws Exception{
        Book book = this.buildMockBook();

        when(bookService.listBooksById(book.getId())).thenThrow(new BookNaoEncontradoException(book.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", book.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

    @Test
    @DisplayName("deve retornar OK ao atualizar um livro com sucesso")
    void updateBookSuccess() throws Exception{
        Book book = this.buildMockBook();
        List<Categories> list = new ArrayList<>();
        UpdateBookDto updateBookDto = new UpdateBookDto("JONAS","JONAS","JONAS","JONAS",list);

        when(bookService.updateBook(book.getId(),updateBookDto)).thenReturn(updateBookDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}",book.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("JONAS"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("JONAS"))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("JONAS"))
                .andExpect(MockMvcResultMatchers.jsonPath("categories").isEmpty());

        verify(bookService).updateBook(book.getId(),updateBookDto);
    }

    @Test
    @DisplayName("deve retornar NOT FOUND ao tentar atualizar um livro com id invalido")
    void updateBookError() throws Exception{
        Book book = this.buildMockBook();
        List<Categories> list = new ArrayList<>();
        UpdateBookDto updateBookDto = new UpdateBookDto("JONAS","JONAS","JONAS","JONAS",list);

        doThrow(new BookNaoEncontradoException(book.getId())).when(bookService).updateBook(book.getId(),updateBookDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}",book.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService).updateBook(book.getId(),updateBookDto);
    }


    @Test
    @DisplayName("Deve retornar OK ao deletar um livro com sucesso")
    void deleteBookSuccess() throws Exception {
        Book book = this.buildMockBook();
        doNothing().when(bookService).deleteBook(book.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{1}",book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("teste").roles("ADMIN", "USER")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(bookService).deleteBook(book.getId());
    }


    @Test
    @DisplayName("Deve retornar Not Found ao tentar deletar um livro com id invalido")
    void deleteBookError() throws Exception {
        Book book = this.buildMockBook();

        doThrow(new BookNaoEncontradoException(book.getId())).when(bookService).deleteBook(book.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{1}",book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService,times(1)).deleteBook(book.getId());

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