
package com.example.book_api.controllers;

import com.example.book_api.dtos.CreateUserDto;
import com.example.book_api.dtos.DetailsBookDto;
import com.example.book_api.dtos.DetailUserDto;
import com.example.book_api.dtos.UpdateUserDto;
import com.example.book_api.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping
    @Transactional
    public ResponseEntity<CreateUserDto> createUser(@RequestBody @Valid CreateUserDto data)throws Exception{
        userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<DetailUserDto>> getAllUsers(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao){
        Page<DetailUserDto> users = userService.getAllUsers(paginacao);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailUserDto> listUserById(@PathVariable String id)throws Exception {
        DetailUserDto user = userService.listUsersById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAnUser(@PathVariable String id)throws Exception{
        userService.deleteAnUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable String id, @RequestBody UpdateUserDto data)throws Exception{
        UpdateUserDto user =  userService.updateUser(id,data);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add/{id}")
    @Transactional
    public ResponseEntity<Void>  addBookToUser(@PathVariable String id, @RequestBody DetailsBookDto book)throws Exception{
        userService.addBookToUser(id,book);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void>  removeBookFromUser(@PathVariable String id, @RequestBody DetailsBookDto book)throws Exception{
        userService.removeBookFromUser(id,book);
        return ResponseEntity.noContent().build();
    }


}
