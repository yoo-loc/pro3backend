package csumb.project3.service;

import csumb.project3.model.Recipe;
import csumb.project3.model.User;
import csumb.project3.repository.UserRepository;
import csumb.project3.repository.RecipeRepository; // Import RecipeRepository

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository; // Inject RecipeRepository

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void addFavorite(String userId, String recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getFavorites().add(recipeId);
        userRepository.save(user);
    }

    public List<Recipe> getFavorites(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return recipeRepository.findAllById(user.getFavorites());
    }
}
