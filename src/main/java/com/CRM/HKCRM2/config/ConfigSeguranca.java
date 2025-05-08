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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(origins = "*")
public class ConfigSeguranca {

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private CustomUsuarioDetalhesService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setUserDetailsService(userDetailsService);
      provider.setPasswordEncoder(passwordEncoder);
      return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // aplica as regras de CORS definidas no corsConfigurer()
      http.cors(withDefaults())
          .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(auth -> auth
          // libera criação e login de usuários
          .requestMatchers(HttpMethod.POST, "/usuarios/criar", "/usuarios/login")
            .permitAll()
          // registro de compra: apenas clientes autenticados
          .requestMatchers(HttpMethod.POST, "/compras")
            .hasRole("CLIENTE")
          // listagem de produtos (doces) para qualquer um
          .requestMatchers(HttpMethod.GET, "/doces/**")
            .permitAll()
          // exclusão de produtos: apenas gerentes
          .requestMatchers(HttpMethod.DELETE, "/doces/**")
            .hasRole("GERENTE")
          .anyRequest()
            .authenticated()
        )
        .httpBasic(withDefaults());

      return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**")
                  .allowedOrigins("http://localhost:8080")
                  .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                  .allowCredentials(true);
        }
      };
    }

}
