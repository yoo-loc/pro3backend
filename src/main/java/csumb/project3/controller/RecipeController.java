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

import java.time.format.DateTimeFormatter;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

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
public ResponseEntity<List<Map<String, String>>> getCommentsForRecipe(@PathVariable String recipeId) {
    Recipe recipe = recipeService.getRecipeById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));

    List<String> commentIds = recipe.getComments();
    List<Comment> comments = commentService.getCommentsByIds(commentIds);

    List<Map<String, String>> responseComments = comments.stream().map(comment -> {
        String username = userService.getUserById(comment.getUserId())
                .map(User::getUsername)
                .orElse("Unknown User");

        return Map.of(
                "id", comment.getId(),
                "userId", comment.getUserId(),
                "username", username,
                "recipeId", comment.getRecipeId(),
                "content", comment.getContent(),
                "createdAt", comment.getCreatedAt().toString()
        );
    }).toList();

    return ResponseEntity.ok(responseComments);
}

    
    




@GetMapping("/{id}/details")
public ResponseEntity<?> getSpecificRecipeWithReferences(
        @PathVariable String id,
        HttpServletRequest request) {
    
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    try {
        // Fetch the authenticated user
        User authenticatedUser = (User) session.getAttribute("user");

        // Fetch the recipe by ID
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));

        // Fetch comments by IDs from the recipe
        List<Comment> comments = recipe.getComments().isEmpty()
                ? Collections.emptyList()
                : commentService.getCommentsByIds(recipe.getComments());

        // Check if the recipe is in the user's favorites
        boolean isFavorite = authenticatedUser.getFavorites().contains(recipe.getId());

        // Prepare response
        Map<String, Object> response = Map.of(
                "recipe", Map.of(
                        "id", recipe.getId(),
                        "title", recipe.getTitle(),
                        "ingredients", recipe.getIngredients(),
                        "instructions", recipe.getInstructions(),
                        "dietaryTags", recipe.getDietaryTags(),
                        "imageUrl", recipe.getImageUrl(),
                        "ownerId", recipe.getOwnerId(),
                        "likes", recipe.getLikes()
                ),
                "comments", comments.stream().map(comment -> Map.of(
                        "id", comment.getId(),
                        "userId", comment.getUserId(),
                        "username", comment.getUsername(),
                        "content", comment.getContent(),
                        "createdAt", comment.getCreatedAt(),
                        "editedAt", comment.getEditedAt()
                )).toList(),
                "isFavorite", isFavorite
        );

        return ResponseEntity.ok(response);
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Recipe not found with ID: " + id));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "An unexpected error occurred.", "error", e.getMessage()));
    }
}





@PostMapping("/{recipeId}/comments")
public ResponseEntity<?> addComment(
        @PathVariable String recipeId,
        @RequestBody Map<String, String> requestData,
        HttpServletRequest request) {

    // Check if the user is authenticated
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    // Get the authenticated user from the session
    User user = (User) session.getAttribute("user");
    String content = requestData.get("content");

    // Validate the content of the comment
    if (content == null || content.trim().isEmpty()) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Content cannot be empty."));
    }

    // Create and save the comment with user information
    Comment comment = new Comment();
    comment.setId(UUID.randomUUID().toString());
    comment.setUserId(user.getId());
    comment.setUsername(user.getUsername()); // Include the username of the user
    comment.setRecipeId(recipeId);
    comment.setContent(content);
    comment.setCreatedAt(LocalDateTime.now());

    commentService.addComment(comment);

    // Link the comment to the recipe
    Recipe recipe = recipeService.getRecipeById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
    recipe.getComments().add(comment.getId());
    recipeService.saveRecipe(recipe); // Save the updated recipe

    // Link the comment to the user
    user.getComments().add(comment.getId());
    userService.saveUser(user); // Save the updated user

    // Prepare response with comment details including username
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of(
                "message", "Comment added successfully!",
                "comment", Map.of(
                    "id", comment.getId(),
                    "userId", comment.getUserId(),
                    "username", comment.getUsername(),
                    "recipeId", comment.getRecipeId(),
                    "content", comment.getContent(),
                    "createdAt", comment.getCreatedAt().toString()
                )
            ));
}


@DeleteMapping("/{recipeId}/comments/{commentId}")
public ResponseEntity<?> removeCommentFromRecipe(
        @PathVariable String recipeId,
        @PathVariable String commentId,
        HttpServletRequest request) {

    // Check if the user is authenticated
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    // Get the authenticated user from the session
    User authenticatedUser = (User) session.getAttribute("user");

    // Fetch the comment from the service
    Comment comment = commentService.getCommentById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

    // Check if the authenticated user owns the comment
    if (!comment.getUserId().equals(authenticatedUser.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "You are not authorized to delete this comment."));
    }

    // Fetch the recipe and remove the comment from its list
    Recipe recipe = recipeService.getRecipeById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
    boolean removedFromRecipe = recipe.getComments().remove(commentId);
    if (removedFromRecipe) {
        recipeService.saveRecipe(recipe); // Save the updated recipe
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Comment not found in the recipe's comments list."));
    }

    // Remove the comment from the user's comment list
    boolean removedFromUser = authenticatedUser.getComments().remove(commentId);
    if (removedFromUser) {
        userService.saveUser(authenticatedUser); // Save the updated user
    }

    // Delete the comment from the database
    commentService.deleteCommentById(commentId);

    return ResponseEntity.noContent().build();
}



@PatchMapping("/{recipeId}/comments/{commentId}")
public ResponseEntity<?> editComment(
        @PathVariable String recipeId,
        @PathVariable String commentId,
        @RequestBody Map<String, String> requestData,
        HttpServletRequest request) {

    // Check if the user is authenticated
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    // Get the authenticated user from the session
    User authenticatedUser = (User) session.getAttribute("user");

    // Fetch the comment from the service
    Comment comment = commentService.getCommentById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

    // Check if the authenticated user owns the comment
    if (!comment.getUserId().equals(authenticatedUser.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "You are not authorized to edit this comment."));
    }

    // Validate the new content
    String newContent = requestData.get("content");
    if (newContent == null || newContent.trim().isEmpty()) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", "Content cannot be empty."));
    }

    // Update the comment
    comment.setContent(newContent);
    comment.setEditedAt(LocalDateTime.now()); // Update the edited timestamp
    commentService.addComment(comment); // Save the updated comment

    // Prepare response with updated comment details
    return ResponseEntity.ok(Map.of(
            "message", "Comment updated successfully!",
            "comment", Map.of(
                    "id", comment.getId(),
                    "userId", comment.getUserId(),
                    "username", comment.getUsername(),
                    "recipeId", comment.getRecipeId(),
                    "content", comment.getContent(),
                    "createdAt", comment.getCreatedAt().toString(),
                    "editedAt", comment.getEditedAt().toString()
            )
    ));
}

@PostMapping("/{recipeId}/favorites")
public ResponseEntity<?> addRecipeToFavorites(
        @PathVariable String recipeId,
        HttpServletRequest request) {
    
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    User authenticatedUser = (User) session.getAttribute("user");

    // Check if the recipe exists
    if (!recipeService.getRecipeById(recipeId).isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Recipe not found with ID: " + recipeId));
    }

    if (!authenticatedUser.getFavorites().contains(recipeId)) {
        authenticatedUser.addFavorite(recipeId); // Use User's method to add favorite
        userService.saveUser(authenticatedUser);
    }

    return ResponseEntity.ok(Map.of(
        "message", "Recipe added to favorites successfully!",
        "favorites", authenticatedUser.getFavorites()
    ));
}

@DeleteMapping("/{recipeId}/favorites")
public ResponseEntity<?> removeRecipeFromFavorites(
        @PathVariable String recipeId,
        HttpServletRequest request) {
    
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated."));
    }

    User authenticatedUser = (User) session.getAttribute("user");

    // Check if the recipe exists (if necessary for your business logic)
    if (!recipeService.getRecipeById(recipeId).isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Recipe not found with ID: " + recipeId));
    }

    if (authenticatedUser.getFavorites().contains(recipeId)) {
        authenticatedUser.removeFavorite(recipeId); // Use User's method to remove favorite
        userService.saveUser(authenticatedUser);
    }

    return ResponseEntity.ok(Map.of(
        "message", "Recipe removed from favorites successfully!",
        "favorites", authenticatedUser.getFavorites()
    ));
}







    

  
}