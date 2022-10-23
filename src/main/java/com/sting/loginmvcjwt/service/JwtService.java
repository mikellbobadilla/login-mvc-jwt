package com.sting.loginmvcjwt.service;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
  
  private static final String SECRET_KEY = "clave_secreta";

  public static final long EXPIRATION_TIME = 864_000_000; // 10 days

  public static final String TOKEN_PREFIX = "Bearer ";

  // Genera el token JWT
  public String createToken(UserDetails userDetails){
    return Jwts.builder()
      .setSubject(userDetails.getUsername())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
      .compact();
  }

  // Valida si el token es válido
  public boolean validateToken(String token, UserDetails userDetails){
    return userDetails.getUsername().equals(userDetails.getUsername()) && !isExpired(token);
  }

  // Valida si el token ha expirado
  public boolean isExpired(String token){
    return getExpirationDate(token).before(new Date());
  }

  // Obtiene la fecha de expiración del token
  private Date getExpirationDate(String token) {
    return ((Claims) getClaims(token)).getExpiration();
  }

  // Obtiene el Claims del token -> lo que quiere decir es que obtiene el cuerpo del token
  private Object getClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }


  // Obtiene el nombre de usuario del token
  public String getUsername(String token){
    return ((Claims) getClaims(token)).getSubject();
  }
}
