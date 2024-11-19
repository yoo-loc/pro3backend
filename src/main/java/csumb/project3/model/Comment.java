package csumb.project3.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments") // Optional: for separate storage of comments
public class Comment {

    @Id
    private String id; // Unique identifier for the comment
    private String userId; // ID of the user who created the comment
    private String content; // The content of the comment
    private LocalDateTime createdAt; // Timestamp for when the comment was created

    // Default constructor
    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public Comment(String id, String userId, String content) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
