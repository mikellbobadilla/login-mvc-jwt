package com.sting.loginmvcjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sting.loginmvcjwt.filter.JwtRequestFilter;
import com.sting.loginmvcjwt.service.UserService;

@Configuration
public class SecurityConfig {

  @Autowired
  UserService userService;

  @Autowired
  JwtRequestFilter jwtRequestFilter;

  // Configuración de seguridad para la aplicación
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Url donde el usuario puede acceder sin estar logueado
    http
      .authorizeRequests(
          authorizeRequests -> authorizeRequests
              .antMatchers("/login", "/register", "/users").permitAll()
              .antMatchers(HttpMethod.POST, "/validate").permitAll()
              .antMatchers("/assets/**").permitAll()
              .anyRequest().authenticated() // Cualquier otra url requiere autenticacion
      );

    // Configuracion del login
    http.formLogin(
      formLogin -> formLogin
          .loginPage("/login") // Url del formulario de login
          .loginProcessingUrl("/validate").permitAll() // Url donde se envian las credenciales
    );

    // Configuracion del logout
    http.logout(
      logout -> logout
          .deleteCookies("JSESSIONID", "jwt") // Borra la cookie de sesion y la cookie de jwt
          .logoutSuccessUrl("/login") // Url a la que se redirige en caso de exito
          .permitAll() // Permitir acceso a la pagina de logout a cualquiera
    );
    
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // No crear sesion
  
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro de jwt

    // Configuracion de CSRF
    http.csrf().disable();
    
    // http.authenticationProvider(authenticationProvider()); // Configuracion del proveedor de autenticacion

    return http.build();
  }

  // Configuracion del proveedor de autenticacion
  // @Bean
  // public DaoAuthenticationProvider authenticationProvider() throws Exception {
  //   DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
  //   provider.setUserDetailsService(userService);
  //   provider.setPasswordEncoder(passwordEncoder());
  //   return provider;
  // }

  // Codificador de contraseñas
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Configuracion del AuthenticationManager
  // @Bean
  // public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
  //   return configuration.getAuthenticationManager();
  // }
}
