package csumb.project3.repository;

import csumb.project3.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    // Find comments by recipeId
    List<Comment> findByRecipeId(String recipeId);

    // Find comments by userId
    List<Comment> findByUserId(String userId);

}
