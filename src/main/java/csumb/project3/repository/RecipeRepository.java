package csumb.project3.repository;

import csumb.project3.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    // Find recipes that contain a specific ingredient (case-sensitive)
    List<Recipe> findByIngredientsContaining(String ingredient);

    // Find recipes that contain a specific ingredient (case-insensitive)
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredient);

    // Find all recipes created by a specific user
    List<Recipe> findByOwnerId(String ownerId);

    // Find all recipes with a specific dietary tag (case-sensitive)
    List<Recipe> findByDietaryTagsContaining(String dietaryTag);

    // Find all recipes with a specific dietary tag (case-insensitive)
    List<Recipe> findByDietaryTagsContainingIgnoreCase(String dietaryTag);
}
