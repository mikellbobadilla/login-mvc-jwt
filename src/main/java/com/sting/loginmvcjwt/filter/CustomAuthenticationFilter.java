package com.sting.loginmvcjwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sting.loginmvcjwt.service.JwtToken;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtToken jwtToken;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    User user = (User) authResult.getPrincipal();

    String token = jwtToken.createToken(user);

    Cookie cook = new javax.servlet.http.Cookie("jwt", String.format("Bearer ", token));

    response.addCookie(cook);
    response.sendRedirect("/users");
  }
}
