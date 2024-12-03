package csumb.project3.controller;

import java.util.Collections;
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

@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")




public class AuthController {

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
                   HttpSession session = request.getSession(true);
                   session.setAttribute("user", user);
   
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



      // Sign-up endpoint
      @PostMapping("/signup")
      public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> signupData) {
          String name = signupData.get("name");
          String email = signupData.get("email");
          String password = signupData.get("password");
  
          // Validate input
          if (name == null || email == null || password == null) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                      .body(Map.of("message", "Name, email, and password are required"));
          }
  
          // Check if user already exists
          if (userService.getUserByEmail(email).isPresent()) {
              return ResponseEntity.status(HttpStatus.CONFLICT)
                      .body(Map.of("message", "A user with this email already exists"));
          }
  
          // Create and save new user
          User newUser = new User();
          newUser.setUsername(name);
          newUser.setEmail(email);
          newUser.setPassword(password); // Ensure passwords are hashed in production
          userService.saveUser(newUser);
  
          return ResponseEntity.status(HttpStatus.CREATED)
                  .body(Map.of("message", "Sign-up successful", "userId", newUser.getId()));
      }
}
