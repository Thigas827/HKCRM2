package com.CRM.HKCRM2.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(origins = "*")
public class ConfigSeguranca {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.cors(withDefaults())
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()
          ); // Remove .httpBasic(withDefaults())
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
