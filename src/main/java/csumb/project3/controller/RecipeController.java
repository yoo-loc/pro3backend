package csumb.project3.controller;

import csumb.project3.model.Recipe;
import csumb.project3.repository.RecipeRepository;
import csumb.project3.service.RecipeService;
import csumb.project3.service.UserService; // Import UserService

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
    private UserService userService; // Inject UserService
      @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping("/post")
    public Recipe postRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam String ingredient) {
        return recipeService.searchByIngredient(ingredient);
    }

    @GetMapping("/all")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipeById(id); // Delegate to the service layer
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/share")
    public ResponseEntity<Recipe> shareRecipe(@RequestBody Recipe recipe) {  
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        return ResponseEntity.ok(savedRecipe);
    }

    @PostMapping("/favorites/{userId}")
    public ResponseEntity<Void> addFavorite(@PathVariable String userId, @RequestBody String recipeId) {
        userService.addFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites/{userId}")
    public List<Recipe> getFavorites(@PathVariable String userId) {
        return userService.getFavorites(userId);
    }

    @GetMapping("/discover")
    public List<Recipe> discoverRecipes() {
        // Logic for fetching curated or trending recipes
        return recipeService.getTrendingRecipes(); // This method should be implemented in RecipeService
    }
    @PostMapping("/updateLikes/{id}")
    public Recipe updateLikes(@PathVariable String id, @RequestParam int likes) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setLikes(likes);
        return recipeRepository.save(recipe);
    }
    

}
