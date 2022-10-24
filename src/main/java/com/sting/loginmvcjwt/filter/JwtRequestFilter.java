package com.sting.loginmvcjwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sting.loginmvcjwt.service.JwtToken;

public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  JwtToken jwtToken;

  @Autowired
  UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    Cookie[] cookies = request.getCookies();

    String token = null;
    String username = null;

    // Get token from cookie
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("jwt")) {
          token = cookie.getValue();
          break;
        }
      }
    }

    if(token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
      username = jwtToken.getUser(token);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      if(jwtToken.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }

      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
  
}
