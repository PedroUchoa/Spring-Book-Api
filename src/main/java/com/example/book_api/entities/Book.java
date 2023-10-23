package com.example.book_api.entities;

import com.example.book_api.dtos.DadosCadastroBook;
import com.example.book_api.dtos.UpdatedDataBooks;
import com.example.book_api.enums.Categories;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Table(name = "books")
@Entity(name = "Book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String author;
    private String description;
    private String image;
    @ElementCollection(targetClass = Categories.class)
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Categories> categories = new ArrayList<>();

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<User> userList = new HashSet<>();

    public Book(DadosCadastroBook dados) {
        this.name= dados.name();
        this.author = dados.author();
        this.description =dados.description();
        this.image = dados.image();
        this.categories = dados.categories();
    }

    public void updateInfos(UpdatedDataBooks data) {
        if(data.name() != null && !data.name().isEmpty()){
            this.name = data.name();
        }
        if(data.author() != null && !data.author().isEmpty()){
            this.author = data.author();
        }
        if(data.description() != null && !data.description().isEmpty()){
            this.description = data.description();
        }
        if(data.image() != null && !data.image().isEmpty()){
            this.image = data.image();
        }
        if(data.categories() != null && !data.categories().isEmpty()){
            this.categories = data.categories();
        }

    }
}
