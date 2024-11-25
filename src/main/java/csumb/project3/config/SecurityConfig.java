package csumb.project3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (for stateless APIs)
                .csrf(csrf -> csrf.disable())
                
                // Enable and configure CORS
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow frontend origin
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow HTTP methods
                    corsConfig.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
                    corsConfig.setAllowCredentials(true); // Allow cookies or credentials
                    return corsConfig;
                }))
                
                // Configure authorization for API routes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login", 
                                "/api/auth/signup", 
                                "/api/auth/logout",
                                "/recipes/all",
                                "/recipes/**", // Adjust as needed for public routes
                                "/public/**" // Allow public endpoints (optional)
                        ).permitAll() // No authentication required for these routes
                        .anyRequest().authenticated() // Require authentication for all other routes
                )
                
                // Disable form-based login and logout redirection
                .formLogin(login -> login.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}
