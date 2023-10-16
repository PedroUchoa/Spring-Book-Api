
package com.example.book_api.controllers;

import com.example.book_api.dtos.DataCreateUser;
import com.example.book_api.dtos.DataDetailBooks;
import com.example.book_api.dtos.DataDetailUser;
import com.example.book_api.dtos.UpdateDataUser;
import com.example.book_api.entities.User;
import com.example.book_api.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity<DataCreateUser> createUser(@RequestBody @Valid DataCreateUser data)throws Exception{
        userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<DataDetailUser>> getAllUsers(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao){
        Page<DataDetailUser> users = userService.getAllUsers(paginacao);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataDetailUser> listUserById(@PathVariable Long id)throws Exception {
        DataDetailUser user = userService.listUsersById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAnUser(@PathVariable Long id)throws Exception{
        userService.deleteAnUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UpdateDataUser> updateUser(@PathVariable Long id, @RequestBody UpdateDataUser data)throws Exception{
        UpdateDataUser user =  userService.updateUser(id,data);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add/{id}")
    @Transactional
    public ResponseEntity<Void>  addBookToUser(@PathVariable Long id, @RequestBody DataDetailBooks book)throws Exception{
        userService.addBookToUser(id,book);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void>  removeBookFromUser(@PathVariable Long id, @RequestBody DataDetailBooks book)throws Exception{
        userService.removeBookFromUser(id,book);
        return ResponseEntity.noContent().build();
    }


}
