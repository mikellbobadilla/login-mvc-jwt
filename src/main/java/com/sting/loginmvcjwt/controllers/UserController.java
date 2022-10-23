package com.sting.loginmvcjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sting.loginmvcjwt.models.AppUser;
import com.sting.loginmvcjwt.repository.UserRepository;
import com.sting.loginmvcjwt.service.UserService;

@Controller
public class UserController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  UserService userService;

  @GetMapping("/")
  public String getHello() {
    return "index";
  }

  @GetMapping("/login")
  public String getLogin(Model model) {
    AppUser user = new AppUser();

    model.addAttribute("user", user);

    return "login";
  }

  @GetMapping("/register")
  public String getRegister(Model model) {
    AppUser user = new AppUser();

    model.addAttribute("user", user);

    return "register";
  }

  @PostMapping("/register")
  public String postRegister(@ModelAttribute AppUser user, Model model) {
    boolean userExist = userRepository.existsByUsername(user.getUsername());
    if (userExist) {
      return "redirect:/register";
    }
    String encoded = passwordEncoder.encode(user.getPassword());
    AppUser newUser = new AppUser(null, user.getUsername(), encoded);
    userRepository.save(newUser);
    return "redirect:/users";
  }

  @GetMapping("/private")
  public String privado() {
    return "privado";
  }

}
