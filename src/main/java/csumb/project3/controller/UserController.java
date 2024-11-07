package csumb.project3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import csumb.project3.model.User; // Import User class


@RestController
@RequestMapping("/api")
public class UserController {

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/user")
    public User getUser() {
        // Replace this with actual logic to retrieve a user
        return new User(); // Return a User object
    }
}
