package com.example.book_api.repositories;

import com.example.book_api.dtos.CreateUserDto;
import com.example.book_api.entities.User;
import com.example.book_api.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    @Test
    @DisplayName("Deve retornar o Usuario pelo Login do DB com sucesso")
    void findByLoginSucces() {
        CreateUserDto userDto = new CreateUserDto("teste","test","test", Role.USER);
        this.createUser(userDto);
        Optional<User> result = userRepository.findByLogin("teste");
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não Deve retornar o Usuario pelo Login do DB")
    void findByLoginError() {
        Optional<User> result = userRepository.findByLogin("teste");
        assertThat(result.isEmpty()).isTrue();

    }


    private User createUser(CreateUserDto userDto){
        User newUser = new User(userDto);
        entityManager.persist(newUser);
        return newUser;
    }

}