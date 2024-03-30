package pl.milejmichal.usersmicros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.milejmichal.usersmicros.user.UserRepository;
import pl.milejmichal.usersmicros.user.UserService;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "pl.milejmichal.usersmicros")
public class UsersMicrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersMicrosApplication.class, args);
    }

}
