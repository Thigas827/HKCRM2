package com.CRM.HKCRM2.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    // Configura o BCryptPasswordEncoder com um fator de força de 10
    // O fator de força determina a complexidade do algoritmo de hash
    // Um valor mais alto significa mais segurança, mas também mais tempo de processamento
    // O valor 10 é um bom compromisso entre segurança e desempenho
    return new BCryptPasswordEncoder(10);
  }
}