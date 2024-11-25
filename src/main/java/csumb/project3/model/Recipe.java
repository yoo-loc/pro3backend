package csumb.project3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "recipes") // Specify the MongoDB collection
public class Recipe {

    @Id
    private String id; // MongoDB IDs are typically Strings
    private String title;
    private String ingredients; // List of ingredients in plain text or serialized format
    private String instructions; // Cooking instructions
    private List<String> dietaryTags = new ArrayList<>(); // Dietary tags stored as a list (e.g., "vegan", "gluten-free")
    private String url; // Recipe URL
    private String imageUrl; // URL of an image for the recipe
    private String ownerId; // ID of the user who created this recipe
    private List<Comment> comments = new ArrayList<>(); // List of comments associated with the recipe
    private int likes; // Number of likes for the recipe

    // Constructors
    public Recipe() {
    }

    public Recipe(String id, String title, String ingredients, String instructions, List<String> dietaryTags, String url, String imageUrl, String ownerId, int likes) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.dietaryTags = dietaryTags != null ? new ArrayList<>(dietaryTags) : new ArrayList<>();
        this.url = url;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.likes = likes;
    }

    // Getters and Setters for comments
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(String commentId) {
        this.comments.removeIf(comment -> comment.getId().equals(commentId));
    }

    // Getters and Setters for other fields
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
    public Comment getCommentById(String commentId) {
        return this.comments.stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElse(null); // Return null if no match is found
    }
}