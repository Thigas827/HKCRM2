package com.CRM.HKCRM2.config;
import static org.springframework.security.config.Customizer.withDefaults;  // <<-- import estático
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.CRM.HKCRM2.config.CustomUsuarioDetalhesService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//
// Configuração de segurança da aplicação
// Define a configuração de segurança da aplicação
// Define a classe como um componente de configuração do Spring

@Configuration
public class ConfigSeguranca { // Configuração de segurança
    // Injeta o PasswordEncoder para codificar senhas
    // Injeta o CustomUsuarioDetalhesService para carregar detalhes do usuário

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CustomUsuarioDetalhesService userDetailsService;


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      // .authenticationProvider(authenticationProvider())  <<--- REMOVA/COMENTE ISSO
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
