package csumb.project3.controller;

import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import csumb.project3.model.User;
import csumb.project3.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from the frontend
public class UserController {

    @Autowired
    private UserService userService;

    // Fetch all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Fetch a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "favorites", user.getFavorites()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with ID: " + id));
        }
    }

    // Create a new user (Signup)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> signupData) {
        String email = signupData.get("email");
        String password = signupData.get("password");
        String username = signupData.get("username");

        if (email == null || password == null || username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "All fields (email, password, username) are required"));
        }

        if (userService.getUserByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email is already in use"));
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // TODO: Hash the password before storing
        user.setUsername(username);
        User savedUser = userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully", "id", savedUser.getId()));
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String id, @RequestBody User user) {
        Optional<User> existingUserOptional = userService.getUserById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setFavorites(user.getFavorites());
            User updatedUser = userService.saveUser(existingUser);

            return ResponseEntity.ok(Map.of(
                    "message", "User updated successfully",
                    "id", updatedUser.getId()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with ID: " + id));
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User with ID " + id + " deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with ID: " + id));
        }
    }

    // Check if email is already in use
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = userService.getUserByEmail(email).isPresent();
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Admin function to update user IDs to sequential values
    @PostMapping("/update-ids")
    public ResponseEntity<Map<String, String>> updateUserIds() {
        userService.updateUserIdsToSimpleIds();
        return ResponseEntity.ok(Map.of("message", "User IDs updated to simple sequential IDs."));
    }

    // Add a recipe to favorites
    @PostMapping("/{userId}/favorites")
    public ResponseEntity<?> addToFavorites(@PathVariable String userId, @RequestBody String recipeId) {
        Optional<User> userOpt = userService.getUserById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.getFavorites().contains(recipeId)) {
                user.getFavorites().add(recipeId);
                userService.saveUser(user); // Save updated user through service
                return ResponseEntity.ok(Map.of("message", "Recipe added to favorites."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Recipe already in favorites."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with ID: " + userId));
        }
    }

    @DeleteMapping("/{userId}/favorites")
public ResponseEntity<?> removeFromFavorites(@PathVariable String userId, @RequestBody String recipeId) {
    Optional<User> userOpt = userService.getUserById(userId);

    if (userOpt.isPresent()) {
        User user = userOpt.get();
        if (user.getFavorites().contains(recipeId)) {
            user.getFavorites().remove(recipeId);
            userService.saveUser(user); // Save updated user
            return ResponseEntity.ok(Map.of("message", "Recipe removed from favorites."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Recipe not found in favorites."));
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found with ID: " + userId));
    }
}
}