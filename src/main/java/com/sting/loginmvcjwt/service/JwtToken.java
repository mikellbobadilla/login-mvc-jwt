package com.sting.loginmvcjwt.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtToken {
  
  String secretKey = "ElSentidoDeLavidaEsUnMisterioPorqueNoSabemosLoQueNosEsperaAlDiaSiguientePeroSoloHayQueVivirLaVida";

  SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  
  public String createToken(User user){
    String token = Jwts.builder()
                  .setHeaderParam("alg", key)
                  .setSubject(user.getUsername())
                  .setIssuedAt(new Date(System.currentTimeMillis()))
                  .setExpiration(new Date(System.currentTimeMillis() + 172800000)) // Dos dias
                  .signWith(key,SignatureAlgorithm.HS256)
                  .compact();

    return token;
  }


  public Date extractExpiration(String token){
    return getPayload(token).getExpiration();
  }

  public boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  private Claims getPayload(String token){
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  public String getUser (String token){
    return getPayload(token).getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails){
    final String username = getUser(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
