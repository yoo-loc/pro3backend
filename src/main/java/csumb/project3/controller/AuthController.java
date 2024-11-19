package csumb.project3.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import csumb.project3.model.User;
import csumb.project3.service.UserService;


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

    @Autowired
    private UserService userService; // Assume UserService exists with necessary methods

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request, @RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) { // Ensure passwords are hashed in production
                HttpSession session = request.getSession(true); // Create a new session
                session.setAttribute("user", user); // Store the authenticated user in the session

                return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "user", Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail()
                    )
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get the current session, if it exists
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }

        return ResponseEntity.ok("Logout successful");
    }

    // Check authentication status
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get the current session, if it exists
        boolean isAuthenticated = (session != null && session.getAttribute("user") != null);

        return ResponseEntity.ok(Map.of(
            "isAuthenticated", isAuthenticated,
            "user", isAuthenticated ? session.getAttribute("user") : null
        ));
    }

   


}
