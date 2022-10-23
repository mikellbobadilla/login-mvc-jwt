package com.sting.loginmvcjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sting.loginmvcjwt.models.AppUser;
import com.sting.loginmvcjwt.repository.UserRepository;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService{

  @Autowired
  UserRepository userRepository;

  PasswordEncoder passwordEncoder;

  // Carga al usuario por su nombre de usuario
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    AppUser user = userRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(String.format("user %s not found", username))
    );

    return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
  }

  // Valida si el ususario existe
  public boolean validateUsername (String username){
    return userRepository.existsByUsername(username);
  }

  // Valida si el password es correcto
  public boolean validatePassword(String password, UserDetails user){
    return passwordEncoder.matches(password, user.getPassword());
  }

  // Crea un nuevo hash de password
  public String encodePassword(String password){
    return passwordEncoder.encode(password);
  }
}
