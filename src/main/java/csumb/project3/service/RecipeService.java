package csumb.project3.service;

import csumb.project3.model.Comment;
import csumb.project3.model.Recipe;
import csumb.project3.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Save a new recipe
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // Search for recipes by ingredient
    public List<Recipe> searchByIngredient(String ingredient) {
        return recipeRepository.findByIngredientsContainingIgnoreCase(ingredient);
    }

    // Get all recipes
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Get a single recipe by ID
    public Optional<Recipe> getRecipeById(String id) {
        return recipeRepository.findById(id);
    }

    // Get all recipes created by a specific user
    public List<Recipe> getRecipesByUserId(String userId) {
        return recipeRepository.findByOwnerId(userId);
    }

    // Update an existing recipe
    public Recipe updateRecipe(String id, Recipe updatedRecipe) {
        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(id);
        if (existingRecipeOpt.isPresent()) {
            Recipe existingRecipe = existingRecipeOpt.get();
            existingRecipe.setTitle(updatedRecipe.getTitle());
            existingRecipe.setIngredients(updatedRecipe.getIngredients());
            existingRecipe.setInstructions(updatedRecipe.getInstructions());
            existingRecipe.setDietaryTags(updatedRecipe.getDietaryTags());
            existingRecipe.setUrl(updatedRecipe.getUrl());
            existingRecipe.setImageUrl(updatedRecipe.getImageUrl());
            return recipeRepository.save(existingRecipe);
        } else {
            throw new RuntimeException("Recipe not found with ID: " + id);
        }
    }

    // Delete a recipe by ID
    public void deleteRecipeById(String id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recipe not found with ID: " + id);
        }
    }

    // Add a comment to a recipe
    public Recipe addCommentToRecipe(String recipeId, Comment comment) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
        recipe.addComment(comment);
        return recipeRepository.save(recipe);
    }

    // Remove a comment from a recipe
    public Recipe removeCommentFromRecipe(String recipeId, String commentId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
        recipe.removeComment(commentId);
        return recipeRepository.save(recipe);
    }
}
