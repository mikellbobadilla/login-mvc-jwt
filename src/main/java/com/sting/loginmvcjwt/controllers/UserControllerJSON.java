package com.sting.loginmvcjwt.controllers;

import com.sting.loginmvcjwt.models.AppUser;
import com.sting.loginmvcjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserControllerJSON {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public List<AppUser> getUsers(){
        return userRepository.findAll();
    }
}
