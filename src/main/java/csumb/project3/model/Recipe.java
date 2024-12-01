package csumb.project3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "recipes") // Specify the MongoDB collection
public class Recipe {

    @Id
    private String id; // MongoDB IDs are typically Strings
    private String title;
    private String ingredients; // Ingredients as plain text or serialized format
    private String instructions; // Cooking instructions
    private List<String> dietaryTags = new ArrayList<>(); // Dietary tags (e.g., vegan, gluten-free)
    private String imageUrl; // Image URL for the recipe
    private String ownerId; // ID of the user who created the recipe
    private List<String> comments = new ArrayList<>(); // List of comment IDs associated with the recipe
    private int likes; // Number of likes for the recipe
    private LocalDateTime createdAt = LocalDateTime.now(); 
    private int favoritesCount = 0; 
    // Constructors
    public Recipe() {
    }

    public Recipe(String id, String title, String ingredients, String instructions, List<String> dietaryTags, String url, String imageUrl, String ownerId, int likes) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.dietaryTags = dietaryTags != null ? new ArrayList<>(dietaryTags) : new ArrayList<>();

        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.likes = likes;
    }

    // Getters and Setters for Comments
    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    // Add a comment to the recipe
    public void addComment(String commentId) {
        if (!comments.contains(commentId)) {
            this.comments.add(commentId);
        } else {
            System.out.println("Comment ID " + commentId + " already exists.");
        }
    }

    // Remove a comment from the recipe by ID
    public void removeComment(String commentId) {
        if (comments.contains(commentId)) {
            this.comments.removeIf(comment -> comment.equals(commentId));
        } else {
            System.out.println("Comment ID " + commentId + " not found in recipe.");
        }
    }

    // Getters and Setters for Other Fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getDietaryTags() {
        return dietaryTags;
    }

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

   

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Utility Methods
    public boolean isOwnedBy(String userId) {
        return this.ownerId != null && this.ownerId.equals(userId);
    }

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
                ", comments=" + comments +
                '}';
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

   // Getter for favoritesCount
   public int getFavoritesCount() {
    return favoritesCount;
}

// Setter for favoritesCount
public void setFavoritesCount(int favoritesCount) {
    this.favoritesCount = favoritesCount;
}
}
