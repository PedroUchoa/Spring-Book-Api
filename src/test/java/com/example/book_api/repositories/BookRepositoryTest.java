package com.example.book_api.repositories;

import com.example.book_api.dtos.CreateBookDto;
import com.example.book_api.entities.Book;
import com.example.book_api.enums.Categories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve retornar o livro com sucesso do DB")
    void findByNameSuccess() {
        List<Categories> categoriesList = new ArrayList<>();
        categoriesList.add(Categories.ROMANCE);
        CreateBookDto bookDto = new CreateBookDto("teste","teste","testeste","teste",categoriesList);
        this.createBook(bookDto);

        Optional<Book> result = this.bookRepository.findByName("teste");
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não Deve retornar o livro com sucesso do DB quando o usuario não existe")
    void findByNameError() {
        Optional<Book> result = this.bookRepository.findByName("teste");
        assertThat(result.isEmpty()).isTrue();

    }

    private Book createBook(CreateBookDto dados){
        Book newBook = new Book(dados);
        this.entityManager.persist(newBook);
        return newBook;
    }



}