package com.sting.loginmvcjwt.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sting.loginmvcjwt.service.JwtService;
import com.sting.loginmvcjwt.service.UserService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
      
  @Autowired
  UserService userService;

  @Autowired
  JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String jwt = null;
    String username = null;
    Cookie[] cookies = request.getCookies();
    if(cookies != null){
      for(Cookie cookie : cookies){
        if(cookie.getName().equals("jwt")){
          jwt = cookie.getValue();
        }
      }
    }
    
    if(jwt != null && jwt.startsWith("Bearer ")){
      jwt = jwt.substring(7);
      username = jwtService.getUsername(jwt);
    }

    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
      UserDetails userDetails = userService.loadUserByUsername(username);

      if(jwtService.validateToken(username, userDetails)){
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }
  
}
