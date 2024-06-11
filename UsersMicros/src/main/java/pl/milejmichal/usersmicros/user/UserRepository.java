package pl.milejmichal.usersmicros.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username: '?0'}")
    Optional<User> findByUsername(String username);

    @NotNull
    LinkedList<User> findAll();
}
