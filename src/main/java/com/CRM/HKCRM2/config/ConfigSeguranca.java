package com.CRM.HKCRM2.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//
// Configuração de segurança da aplicação
// Define a configuração de segurança da aplicação
// Define a classe como um componente de configuração do Spring

@Configuration
public class ConfigSeguranca { // Configuração de segurança
    // Injeta o PasswordEncoder para codificar senhas
    // Injeta o CustomUsuarioDetalhesService para carregar detalhes do usuário

    @Autowired private PasswordEncoder encoder;
    @Autowired private CustomUsuarioDetalhesService userDetailsService;


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
        // desabilita CSRF (útil para APIs REST stateless)
        .csrf(csrf -> csrf.disable())
  
        // como é API stateless, não usamos sessão
        .sessionManagement(sm -> sm
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
  
        // regras de autorização
        .authorizeHttpRequests(auth -> auth
          // endpoints públicos
          .requestMatchers(HttpMethod.POST, "/usuarios/criar", "/usuarios/login")
            .permitAll()
          // só CLIENTE pode comprar
          .requestMatchers("/compras/**")
            .hasRole("CLIENTE")
          // métodos GET em /doces acessíveis a atendente, vendedor e gerente
          .requestMatchers(HttpMethod.GET, "/doces/**")
            .hasAnyRole("ATENDENTE", "VENDEDOR", "GERENTE")
          // DELETE em /doces só para gerente
          .requestMatchers(HttpMethod.DELETE, "/doces/**")
            .hasRole("GERENTE")
          // todo o resto exige autenticação
          .anyRequest()
            .authenticated()
        )
  
        // HTTP Basic Auth (pode trocar por formLogin ou JWT)
        .httpBasic();
  
      return http.build();
    }
  }
