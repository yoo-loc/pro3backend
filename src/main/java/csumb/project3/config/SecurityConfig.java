package csumb.project3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/api/users/signup", "/api/auth/login", "/api/auth/logout").permitAll() // Allow public access to signup, login, and logout
                                .anyRequest().authenticated() // Require authentication for other routes
                )
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management for APIs
                )
                .formLogin(login -> login.disable()); // Disable form-based login

        return http.build();
    }
}
