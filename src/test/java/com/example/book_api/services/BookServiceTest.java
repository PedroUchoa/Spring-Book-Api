package com.example.book_api.services;

import com.example.book_api.dtos.CreateBookDto;
import com.example.book_api.dtos.DetailsBookDto;
import com.example.book_api.dtos.UpdateBookDto;
import com.example.book_api.entities.Book;
import com.example.book_api.enums.Categories;
import com.example.book_api.exceptions.BookNaoEncontradoException;
import com.example.book_api.exceptions.DuplicatedBookException;
import com.example.book_api.repositories.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Deve adicionar um livro ao banco de dados com sucesso")
    void addBookSuccess() throws DuplicatedBookException {
        CreateBookDto book = new CreateBookDto(this.buildMockBook());

        bookService.addBook(book);

        verify(bookRepository,times(1)).save(new Book(book));
    }

    @Test
    @DisplayName("Deve jogar uma exceção quando salvar um livro invalido")
    void addBookError() throws DuplicatedBookException {
        Book book = this.buildMockBook();

        when(bookRepository.findByName(book.getName())).thenReturn(Optional.of(book));

        DuplicatedBookException excep = assertThrows(DuplicatedBookException.class,()->bookService.addBook(new CreateBookDto(book)));

        assertEquals("Já existe um livro com esse nome: " + book.getName()+ " adicionado ao sistema",excep.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um livro por Id com sucesso")
    void listBooksById() throws BookNaoEncontradoException {
        Book book = this.buildMockBook();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        DetailsBookDto result = bookService.listBooksById(book.getId());

        assertEquals(new DetailsBookDto(book), result);
        verify(bookRepository,times(1)).findById(book.getId());
    }

    @Test
    @DisplayName("Deve jogar um exceção quando buscar por um id invalido")
    void listBooksByIdError() throws BookNaoEncontradoException {
        Book book = this.buildMockBook();

        BookNaoEncontradoException excep = assertThrows(BookNaoEncontradoException.class,()-> bookService.listBooksById(book.getId()));

        assertEquals("Livro com o id: " + book.getId() + " não encontrado na base de dados",excep.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um livro com sucesso")
    void updateBookSuccess() throws BookNaoEncontradoException {
        Book book = this.buildMockBook();
        List<Categories> categoriesList = new ArrayList<>();

        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));

        UpdateBookDto updateBookDto = new UpdateBookDto("teste2","teste2","teste2","teste2",categoriesList);
        UpdateBookDto result = bookService.updateBook(book.getId(),updateBookDto);

        assertEquals(result.name(),updateBookDto.name());
        assertEquals(result.author(),updateBookDto.author());
        assertEquals(result.description(),updateBookDto.description());
        assertEquals(result.image(),updateBookDto.image());
        assertEquals(result.categories(),updateBookDto.categories());

        verify(bookRepository).findById(book.getId());
    }

    @Test
    @DisplayName("Deve jogar um exceção ao buscar um id invalido para atualizar")
    void updateBookError() {
        Book book = this.buildMockBook();

        BookNaoEncontradoException excep = assertThrows(BookNaoEncontradoException.class,()->bookService.deleteBook(book.getId()));

        assertEquals("Livro com o id: " + book.getId() + " não encontrado na base de dados",excep.getMessage());
        verify(bookRepository).findById(book.getId());
    }

    @Test
    @DisplayName("Deve deletar um livro com sucesso")
    void deleteBookSuccess() throws BookNaoEncontradoException {
        Book book = this.buildMockBook();

        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));

        bookService.deleteBook(book.getId());

        verify(bookRepository).deleteById(book.getId());
    }

    @Test
    @DisplayName("Deve jogar um exceção ao buscar um livro pelo id invalido para deletar")
    void deleteBookError() {
        Book book = this.buildMockBook();

        BookNaoEncontradoException excep = assertThrows(BookNaoEncontradoException.class,()->bookService.deleteBook(book.getId()));

        assertEquals("Livro com o id: " + book.getId() + " não encontrado na base de dados",excep.getMessage());
    }

    private Book buildMockBook(){
        Book book = new Book();
        List<Categories> categories = new ArrayList<>();
        book.setName("teste");
        book.setId("1");
        book.setAuthor("teste");
        book.setImage("teste");
        book.setDescription("teste");
        book.setCategories(categories);
        return book;
    }


}