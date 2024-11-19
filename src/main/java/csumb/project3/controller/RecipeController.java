package csumb.project3.controller;

import csumb.project3.model.Recipe;
import csumb.project3.repository.RecipeRepository;
import csumb.project3.service.RecipeService;
import csumb.project3.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeRepository recipeRepository;

    // Post a new recipe
    @PostMapping
    public ResponseEntity<Recipe> postRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        return ResponseEntity.ok(savedRecipe);
    }

    // Search recipes by ingredient
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String ingredient) {
        List<Recipe> recipes = recipeService.searchByIngredient(ingredient);
        return ResponseEntity.ok(recipes);
    }

    // Get all recipes
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    // Delete a recipe by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipeById(id);
        return ResponseEntity.noContent().build();
    }

    // Share a recipe (repost)
    @PostMapping("/share")
    public ResponseEntity<Recipe> shareRecipe(@RequestBody Recipe recipe) {
        Recipe sharedRecipe = recipeService.saveRecipe(recipe);
        return ResponseEntity.ok(sharedRecipe);
    }

    // Add a recipe to user's favorites
    @PostMapping("/favorites/{userId}")
    public ResponseEntity<Void> addFavorite(@PathVariable String userId, @RequestBody String recipeId) {
        userService.addFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    // Get a user's favorite recipes
    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<Recipe>> getFavorites(@PathVariable String userId) {
        List<Recipe> favoriteRecipes = userService.getFavorites(userId);
        return ResponseEntity.ok(favoriteRecipes);
    }

    // Get all recipes posted by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getUserRecipes(@PathVariable String userId) {
        List<Recipe> userRecipes = recipeService.getRecipesByUserId(userId);
        return ResponseEntity.ok(userRecipes);
    }

    // Discover curated or trending recipes
    @GetMapping("/discover")
    public ResponseEntity<List<Recipe>> discoverRecipes() {
        List<Recipe> trendingRecipes = recipeService.getTrendingRecipes(); // Implement this method in RecipeService
        return ResponseEntity.ok(trendingRecipes);
    }

    // Update likes for a recipe
    @PostMapping("/updateLikes/{id}")
    public ResponseEntity<Recipe> updateLikes(@PathVariable String id, @RequestParam int likes) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));
        recipe.setLikes(likes); // Ensure the `likes` field exists in the Recipe class
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return ResponseEntity.ok(updatedRecipe);
    }
}
