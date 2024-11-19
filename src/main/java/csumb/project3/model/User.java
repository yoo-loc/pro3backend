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
    private List<String> favorites = new ArrayList<>();

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String id, String username, String email, String password, List<String> favorites) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.favorites = favorites != null ? new ArrayList<>(favorites) : new ArrayList<>();
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
}
