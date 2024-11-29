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
    private List<String> comments = new ArrayList<>(); // List of comment IDs
    private List<String> myRecipes = new ArrayList<>(); // List of recipe IDs created by the user

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String id, String username, String email, String password, List<String> favorites, List<String> comments, List<String> myRecipes) {
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

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }

    public List<String> getMyRecipes() {
        return myRecipes;
    }

    public void setMyRecipes(List<String> myRecipes) {
        this.myRecipes = myRecipes != null ? new ArrayList<>(myRecipes) : new ArrayList<>();
    }

// Favorite management
public void addFavorite(String recipeId) {
    if (recipeId != null && !recipeId.trim().isEmpty() && !favorites.contains(recipeId)) {
        favorites.add(recipeId);
    }
}

public boolean removeFavorite(String recipeId) {
    return recipeId != null && favorites.remove(recipeId);
}


    // Comment management
    public void addComment(String commentId) {
        if (!comments.contains(commentId)) {
            comments.add(commentId);
        }
    }

    public boolean removeComment(String commentId) {
        return comments.remove(commentId);
    }

    // Recipe management
    public void addRecipe(String recipeId) {
        if (!myRecipes.contains(recipeId)) {
            myRecipes.add(recipeId);
        }
    }

    public boolean removeRecipe(String recipeId) {
        return myRecipes.remove(recipeId);
    }
}
