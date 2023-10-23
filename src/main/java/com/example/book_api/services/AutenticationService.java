package com.example.book_api.services;

import com.example.book_api.entities.User;
import com.example.book_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AutenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).get();
    }
}
