package csumb.project3.service;

import csumb.project3.model.Comment;
import csumb.project3.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
