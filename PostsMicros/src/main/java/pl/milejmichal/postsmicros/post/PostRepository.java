package pl.milejmichal.postsmicros.post;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(String userId);
    List<Post> findByIdIn(List<String> id);
}
