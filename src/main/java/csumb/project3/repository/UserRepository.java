package csumb.project3.repository;

import csumb.project3.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email); // Return Optional<User>

    Optional<User> findByUsername(String username);

   
    
}