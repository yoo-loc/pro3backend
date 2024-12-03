package csumb.project3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection
                .csrf(csrf -> csrf.disable())

                // Enable and configure CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure request authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/signup",
                                "/api/auth/logout",
                                "/api/users/**",
                                "/recipes/all",
                                "/recipes/**", // Adjust for your specific routes
                                "/public/**" // Allow public endpoints
                        ).permitAll() // Allow these routes without authentication
                        .anyRequest().authenticated() // Require authentication for all other routes
                )

                // Disable form login
                .formLogin(form -> form.disable())

                // Disable default logout handling
                .logout(logout -> logout.disable())

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", // React frontend
                "http://10.0.2.2:3000", // Android emulator
                "http://192.168.1.225:3000" // Your machine's IP
        ));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        corsConfig.setAllowCredentials(true); // Allow cookies and credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply to all endpoints
        return source;
    }
}
