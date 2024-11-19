package csumb.project3.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id; // Custom ID for simplicity
    private String username;
    private String email;
    private String password;
    private List<String> favorites = new ArrayList<>(); // List of favorite recipe IDs
    private List<Comment> comments = new ArrayList<>(); // Comments made by the user
    private List<Recipe> myRecipes = new ArrayList<>(); // Recipes created by the user

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String id, String username, String email, String password, List<String> favorites, List<Comment> comments, List<Recipe> myRecipes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.favorites = favorites != null ? new ArrayList<>(favorites) : new ArrayList<>();
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
        this.myRecipes = myRecipes != null ? new ArrayList<>(myRecipes) : new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites != null ? new ArrayList<>(favorites) : new ArrayList<>();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }

    public List<Recipe> getMyRecipes() {
        return myRecipes;
    }

    public void setMyRecipes(List<Recipe> myRecipes) {
        this.myRecipes = myRecipes != null ? new ArrayList<>(myRecipes) : new ArrayList<>();
    }

    // Favorite management
    public void addFavorite(String recipeId) {
        if (!favorites.contains(recipeId)) {
            favorites.add(recipeId);
        }
    }

    public boolean removeFavorite(String recipeId) {
        return favorites.remove(recipeId);
    }

    // Comment management
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public boolean editComment(String commentId, String newContent) {
        for (Comment comment : comments) {
            if (comment.getId().equals(commentId)) {
                comment.setContent(newContent);
                return true;
            }
        }
        return false;
    }

    public boolean deleteComment(String commentId) {
        return comments.removeIf(comment -> comment.getId().equals(commentId));
    }

    // Recipe management
    public void addRecipe(Recipe recipe) {
        myRecipes.add(recipe);
    }

    public boolean removeRecipe(String recipeId) {
        return myRecipes.removeIf(recipe -> recipe.getId().equals(recipeId));
    }

    public List<Recipe> getAllRecipes() {
        return myRecipes;
    }

    public List<Recipe> getRecipesByUser(String userId) {
        if (this.id.equals(userId)) {
            return myRecipes;
        }
        return new ArrayList<>();
    }
}
