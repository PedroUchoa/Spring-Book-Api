package com.example.book_api.entities;

import com.example.book_api.dtos.DataCreateUser;
import com.example.book_api.dtos.UpdateDataUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String login;
    private String password;
    private String name;

    @ManyToMany
    @JoinTable(name ="user_books",
            joinColumns = {@JoinColumn (name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    private Set<Book> books = new HashSet<>();

    public User(DataCreateUser data) {
        this.login = data.login();
        this.password = data.password();
        this.name = data.name();
    }


    public void updateUser(UpdateDataUser data) {

        if(data.login() != null && !data.login().isEmpty()){
            this.login = data.login();
        }
        if(data.password() != null && !data.password().isEmpty()){
            this.password = data.password();

        }
        if(data.name() != null && !data.name().isEmpty()){
            this.name = data.name();
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getPassword(){
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
