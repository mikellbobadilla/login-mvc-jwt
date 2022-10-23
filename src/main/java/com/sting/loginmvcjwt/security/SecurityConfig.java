package com.sting.loginmvcjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sting.loginmvcjwt.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    // Configuración de seguridad para la aplicación
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Url donde el usuario puede acceder sin estar registrado
        http
        .authorizeRequests(
          authorizeRequests -> authorizeRequests
            .antMatchers("/login", "/register", "/users").permitAll() // El usuario puede acceder al formulario de login
  
            .antMatchers("/assets/**").permitAll() // Para que se puedan aplicar los estilos de bootstrap 
  
            .anyRequest().authenticated() // Cualquier otra ruta que tengamos el usuario tiene que estar autenticado
      );

        // Configuración del login
        http.formLogin(
            formLogin -> formLogin
                .loginPage("/login") // Url del formulario de login
        );

        // Configuración del logout
        http.logout(
            logout -> logout
                .deleteCookies("JSESSIONID", "jwt") // Borra la cookie de sesión y la cookie de jwt
                .logoutSuccessUrl("/login") // Url a la que se redirige en caso de exito
                .permitAll() // Permitir acceso a la página de logout a cualquiera
        );

        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // // No crear sesión
        // Configuración de CSRF
        http.csrf().disable();

        http.authenticationProvider(authenticationProvider()); // Configuración del proveedor de autenticación

        return http.build();
    }

    // Configuración del proveedor de autenticación
    @Bean
    public DaoAuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Configuración del AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
