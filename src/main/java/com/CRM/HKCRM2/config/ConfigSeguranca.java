package com.CRM.HKCRM2.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfigSeguranca {

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private CustomUsuarioDetalhesService userDetailsService;

    // expõe o AuthenticationManager para ser usado, se precisar
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
    }

    // registra um AuthenticationProvider usando seu UserDetailsService + PasswordEncoder 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setUserDetailsService(userDetailsService);
      provider.setPasswordEncoder(passwordEncoder);
      return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // adiciona o provider que sabe carregar usuários e validar senha
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(auth -> auth
          .requestMatchers(HttpMethod.POST, "/usuarios/criar", "/usuarios/login")
            .permitAll()
          .requestMatchers("/compras/**")
            .hasRole("CLIENTE")
          .requestMatchers(HttpMethod.GET, "/doces/**")
            .hasAnyRole("ATENDENTE","VENDEDOR","GERENTE")
          .requestMatchers(HttpMethod.DELETE, "/doces/**")
            .hasRole("GERENTE")
          .anyRequest()
            .authenticated()
        )
        .httpBasic(withDefaults());

      return http.build();
    }
}