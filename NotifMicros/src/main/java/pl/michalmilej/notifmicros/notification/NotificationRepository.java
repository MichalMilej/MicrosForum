package pl.michalmilej.notifmicros.notification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ 'newPostIds': { $exists: true, $not: { $size: 0} } }")
    List<Notification> getNotificationsWithNewPostIds();
}
