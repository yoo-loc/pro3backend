package csumb.project3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "recipes") // Specify the MongoDB collection
public class Recipe {

    @Id
    private String id; // MongoDB IDs are typically Strings
    private String title;
    private String ingredients; // Ingredients as plain text
    private String instructions; // Cooking instructions as plain text
    private List<String> dietaryTags = new ArrayList<>(); // Dietary tags (e.g., vegan, gluten-free)
    private String imageUrl; // Image URL for the recipe
    private String ownerId; // ID of the user who created the recipe
    private List<String> comments = new ArrayList<>(); // List of comment IDs associated with the recipe
    private int likes = 0; // Number of likes for the recipe
    
    private int favoritesCount = 0; // Number of times this recipe is marked as favorite
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Recipe() {}

    public Recipe(String id, String title, String ingredients, String instructions, List<String> dietaryTags, String imageUrl, String ownerId, int likes) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.dietaryTags = dietaryTags != null ? new ArrayList<>(dietaryTags) : new ArrayList<>();
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.likes = likes;
    }

    // Getter and Setter Methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public List<String> getDietaryTags() { return dietaryTags; }
    public void setDietaryTags(List<String> dietaryTags) {
        this.dietaryTags = dietaryTags != null ? new ArrayList<>(dietaryTags) : new ArrayList<>();
    }

    public void addDietaryTag(String tag) {
        if (!dietaryTags.contains(tag)) {
            dietaryTags.add(tag);
        }
    }

    public void removeDietaryTag(String tag) {
        dietaryTags.remove(tag);
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getFavoritesCount() { return favoritesCount; }
    public void setFavoritesCount(int favoritesCount) { this.favoritesCount = favoritesCount; }

    public List<String> getComments() { return comments; }
    public void setComments(List<String> comments) { this.comments = comments; }

    public void addComment(String commentId) {
        if (!comments.contains(commentId)) {
            comments.add(commentId);
        }
    }

    public void removeComment(String commentId) {
        comments.remove(commentId);
    }

    // Increment or decrement favorites count
    public void incrementFavoritesCount() { this.favoritesCount++; }
    public void decrementFavoritesCount() { this.favoritesCount = Math.max(this.favoritesCount - 1, 0); }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                ", dietaryTags=" + dietaryTags +
                ", imageUrl='" + imageUrl + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", likes=" + likes +
                ", favoritesCount=" + favoritesCount +
                ", comments=" + comments +
                '}';
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
