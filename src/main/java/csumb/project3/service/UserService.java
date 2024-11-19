package csumb.project3.service;

import csumb.project3.model.Recipe;
import csumb.project3.model.User;
import csumb.project3.repository.UserRepository;
import csumb.project3.repository.RecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    // Save or update a user with simple ID logic
    public User saveUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(generateSimpleId());
        }
        return userRepository.save(user);
    }

    // Generate a simple sequential ID for new users
    private String generateSimpleId() {
        List<User> users = userRepository.findAll();
        int nextId = users.stream()
                .mapToInt(user -> {
                    try {
                        return Integer.parseInt(user.getId());
                    } catch (NumberFormatException e) {
                        return 0; // Ignore non-numeric IDs
                    }
                })
                .max()
                .orElse(0) + 1;
        return String.valueOf(nextId);
    }

    // Add a recipe to the user's favorites
    public void addFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId)

                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getFavorites().contains(recipeId)) { // Avoid duplicates
            user.getFavorites().add(recipeId);
            userRepository.save(user);
        }

                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!user.getFavorites().contains(recipeId)) {
            user.getFavorites().add(recipeId);
            userRepository.save(user);
        }
    }

    // Remove a recipe from the user's favorites
    public void removeFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getFavorites().remove(recipeId)) {
            userRepository.save(user);
        }

    }

    // Get all favorite recipes for a user
    public List<Recipe> getFavorites(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return recipeRepository.findAllById(user.getFavorites());
    }

    // Find a user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get a user by ID
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID
    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void updateUserIdsToSimpleIds() {
        List<User> users = userRepository.findAll();
        int newId = 1;

        for (User user : users) {
            user.setId(String.valueOf(newId)); // Assign a simple sequential ID
            userRepository.save(user); // Save the updated user
            newId++;
        }
    }
}
