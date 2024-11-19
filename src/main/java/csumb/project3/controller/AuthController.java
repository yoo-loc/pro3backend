package csumb.project3.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getAuthStatus(HttpServletRequest request) {
        boolean isAuthenticated = request.getSession(false) != null; // Checks if a session exists
        Map<String, Boolean> response = Collections.singletonMap("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate session
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }

   


}
