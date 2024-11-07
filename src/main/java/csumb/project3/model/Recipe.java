package csumb.project3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recipes") // Specify the MongoDB collection
public class Recipe {

    @Id
    private String id; // MongoDB IDs are typically Strings

    private String title;
    private String ingredients;
    private String instructions;
    private String dietaryTags;

    // Getters and Setters
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

    public String getDietaryTags() {
        return dietaryTags;
    }

    public void setDietaryTags(String dietaryTags) {
        this.dietaryTags = dietaryTags;
    }
}
