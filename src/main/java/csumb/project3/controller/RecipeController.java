package csumb.project3.controller;

import csumb.project3.model.Comment;
import csumb.project3.model.Recipe;
import csumb.project3.service.CommentService; // Import the CommentService
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
    private CommentService commentService; // Autowire the CommentService

    // Post a new recipe and update the user's recipe list
    @PostMapping
    public ResponseEntity<Recipe> postRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.saveRecipeAndUpdateUser(recipe);
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

    // Get all recipes posted by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getUserRecipes(@PathVariable String userId) {
        List<Recipe> userRecipes = recipeService.getRecipesByUserId(userId);
        return ResponseEntity.ok(userRecipes);
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

    // Get a specific recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));
        return ResponseEntity.ok(recipe);
    }

    // Get comments for a specific recipe
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getCommentsForRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));

        List<Comment> comments = recipe.getComments();
        return ResponseEntity.ok(comments);
    }

  
  // Add a comment to a recipe
  @PostMapping("/{recipeId}/comments")
  public ResponseEntity<Comment> addComment(@PathVariable String recipeId, @RequestBody Comment comment) {
      comment.setRecipeId(recipeId);  // Set the recipe ID for the comment
      Comment savedComment = commentService.addComment(comment);  // Save the comment using CommentService
  
      // Update the recipe to include this new comment
      Recipe recipe = recipeService.getRecipeById(recipeId)
              .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
      recipe.addComment(savedComment);  // Add the new comment to the recipe
      recipeService.saveRecipe(recipe);  // Save the recipe with the updated comments
  
      return ResponseEntity.ok(savedComment);  // Return the saved comment
  }

  
}
