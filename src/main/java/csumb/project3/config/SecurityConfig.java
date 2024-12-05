package csumb.project3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection for APIs
                .csrf(csrf -> csrf.disable())

                // Enable and configure CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure request authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",   // Login endpoint
                                "/api/auth/signup",  // Signup endpoint
                                "/api/auth/logout",  // Logout endpoint
                                "/api/users/**",     // User-related endpoints
                                "/recipes/all",  // Fetch all recipes
                                "/recipes/**",   // Recipe-specific endpoints
                                "/public/**"         // Public endpoints
                        ).permitAll() // Allow unauthenticated access to these routes
                        .anyRequest().authenticated() // Require authentication for all other routes
                )

                // Disable default form login
                .formLogin(form -> form.disable())

                // Disable Spring Security's default logout handling
                .logout(logout -> logout.disable())

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Set allowed origins
        corsConfig.setAllowedOrigins(Arrays.asList(
        "http://localhost:3000", 
        "http://10.0.2.2:3000",  
        "http://192.168.1.225:3000"
));
corsConfig.setAllowCredentials(true);
        // Allow standard HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Allow all headers
       corsConfig.setAllowedHeaders(Collections.singletonList("*"));

        // Enable credentials for session cookies
        corsConfig.setAllowCredentials(true);

        // Apply CORS configuration to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
