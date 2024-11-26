package csumb.project3.controller;

import csumb.project3.model.Comment;
import csumb.project3.model.Recipe;
import csumb.project3.model.User;
import csumb.project3.service.CommentService; // Import the CommentService
import csumb.project3.service.RecipeService;
import csumb.project3.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService; // Autowire the CommentService

    @PostMapping
    public ResponseEntity<?> postRecipe(@RequestBody Recipe recipe, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get current session
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not authenticated."));
        }
    
        User authenticatedUser = (User) session.getAttribute("user");
        recipe.setOwnerId(authenticatedUser.getId()); // Set recipe owner ID
    
        // Validate the recipe
        if (recipe.getTitle() == null || recipe.getTitle().isEmpty() ||
            recipe.getIngredients() == null || recipe.getIngredients().isEmpty() ||
            recipe.getInstructions() == null || recipe.getInstructions().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Recipe title, ingredients, and instructions are required."));
        }
    
        // Save the recipe
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
    
        // Update the user's myRecipes field
        authenticatedUser.getMyRecipes().add(savedRecipe.getId());
        userService.saveUser(authenticatedUser); // Save updated user
    
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "message", "Recipe posted successfully!",
                    "recipe", savedRecipe
                ));
    }
    


    // Search recipes by ingredient
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String ingredient) {
        List<Recipe> recipes = recipeService.searchByIngredient(ingredient);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecipes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not authenticated."));
        }
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

// Get all comments for a recipe
@GetMapping("/{recipeId}/comments")
public ResponseEntity<List<Comment>> getCommentsForRecipe(@PathVariable String recipeId) {
    Recipe recipe = recipeService.getRecipeById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
    List<String> commentIds = recipe.getComments();
    List<Comment> comments = commentService.getCommentsByIds(commentIds);
    return ResponseEntity.ok(comments);
}


    // Add a comment to a recipe
    // @PostMapping("/{recipeId}/comments")
    // public ResponseEntity<Comment> addCommentToRecipe(@PathVariable String recipeId, @RequestBody Map<String, String> requestData) {
    //     String commentId = requestData.get("commentId");
    //     String content = requestData.get("content");

    //     if (commentId == null || content == null) {
    //         return ResponseEntity.badRequest().body(null); // Ensure fields are provided
    //     }

    //     Comment comment = new Comment();
    //     comment.setId(commentId);
    //     comment.setRecipeId(recipeId);
    //     comment.setContent(content);
    //     Comment savedComment = commentService.addComment(comment);

    //     recipeService.addCommentToRecipe(recipeId, commentId);
    //     return ResponseEntity.ok(savedComment);
    // }


// Remove a comment from a recipe
@DeleteMapping("/{recipeId}/comments/{commentId}")
public ResponseEntity<Void> removeCommentFromRecipe(@PathVariable String recipeId, @PathVariable String commentId) {
    recipeService.removeCommentFromRecipe(recipeId, commentId);
    return ResponseEntity.noContent().build();
}


@GetMapping("/{id}/details")
public ResponseEntity<?> getSpecificRecipeWithComments(
        @PathVariable String id,
        HttpServletRequest request) {
    
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    try {
        // Fetch the recipe by ID
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));

        // Fetch the comments for the recipe
        List<String> commentIds = recipe.getComments();
        List<Comment> comments = commentIds.isEmpty() 
                ? Collections.emptyList() 
                : commentService.getCommentsByIds(commentIds);

        // Prepare the response
        Map<String, Object> response = Map.of(
                "recipe", recipe,
                "comments", comments
        );

        return ResponseEntity.ok(response);
    } catch (NoSuchElementException e) {
        // Handle case when recipe is not found
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Recipe not found with ID: " + id));
    } catch (Exception e) {
        // Handle unexpected errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "An unexpected error occurred.", "error", e.getMessage()));
    }
}



@PostMapping("/{recipeId}/comments")
public ResponseEntity<?> addComment(
        @PathVariable String recipeId,
        @RequestBody Map<String, String> requestData,
        HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    User user = (User) session.getAttribute("user");
    String content = requestData.get("content");

    if (content == null || content.trim().isEmpty()) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Content cannot be empty."));
    }

    // Create and save the comment
    Comment comment = new Comment();
    comment.setId(UUID.randomUUID().toString());
    comment.setUserId(user.getId());
    comment.setRecipeId(recipeId);
    comment.setContent(content);
    comment.setCreatedAt(LocalDateTime.now());

    commentService.addComment(comment);

    // Optionally, link the comment to the recipe
    recipeService.addCommentToRecipe(recipeId, comment.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of(
                "message", "Comment added successfully!",
                "comment", comment
            ));
}

    

  
}
