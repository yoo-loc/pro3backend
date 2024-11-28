package csumb.project3.service;

import csumb.project3.model.Comment;
import csumb.project3.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Add a comment
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);  // Save the comment to the database
    }
    
    // Get all comments (optional)
    public List<Comment> getAllComments() {
        return commentRepository.findAll();  // Fetch all comments from the DB
    }

    // Get comments by a list of IDs
    public List<Comment> getCommentsByIds(List<String> commentIds) {
        return commentRepository.findAllById(commentIds);  // Find all comments by their IDs
    }

    // Get a comment by ID
    public Optional<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);  // Fetch a single comment by ID
    }

    // Delete a comment by ID
    public void deleteCommentById(String commentId) {
        commentRepository.deleteById(commentId);  // Delete the comment by its ID
    }
}
