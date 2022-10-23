package com.sting.loginmvcjwt.controllers;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sting.loginmvcjwt.models.AppUser;
import com.sting.loginmvcjwt.repository.UserRepository;
import com.sting.loginmvcjwt.service.JwtService;
import com.sting.loginmvcjwt.service.UserService;


@Controller
public class UserController {
  
  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;
  
  @Autowired
  JwtService jwtService;

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

  @PostMapping("/validate")
  public String validate(@ModelAttribute AppUser user, HttpServletResponse response) {
    // Optional<AppUser> userFound = userRepository.findByUsername(user.getUsername());

    UserDetails userFound = userService.loadUserByUsername(user.getUsername());

    System.out.println(user.getUsername() + " " + user.getPassword());

    if(userFound == null){
      return "redirect:/login";
    }

    if(userService.validatePassword(user.getPassword(), userFound)){
      Cookie cookie = new Cookie("jwt", String.format("Bearer ", jwtService.createToken(userFound)));
      response.addCookie(cookie);
      return "redirect:/";
    }

    return "redirect:/login";
  }


  @GetMapping("/register")
  public String getRegister(Model model) {
    AppUser user = new AppUser();

    model.addAttribute("user", user);
  
    return "register";
  }

  @PostMapping("/register")
  public String postRegister(@ModelAttribute AppUser user, Model model, HttpServletResponse response) {
    boolean userExists = userService.validateUsername(user.getUsername());
    if(userExists) {
      model.addAttribute("user", user);
      return "register";
    }
    user.setPassword(userService.encodePassword(user.getPassword()));
    userRepository.save(user);
    return "redirect:/users";
  }
}
