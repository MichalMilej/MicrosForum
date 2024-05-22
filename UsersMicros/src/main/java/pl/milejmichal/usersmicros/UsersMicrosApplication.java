package pl.milejmichal.usersmicros;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.milejmichal.usersmicros.user.UserServiceGRPCImpl;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "pl.milejmichal.usersmicros")
@RequiredArgsConstructor
public class UsersMicrosApplication implements CommandLineRunner {

    final UserServiceGRPCImpl userServiceGRPC;

    public static void main(String[] args) {
        SpringApplication.run(UsersMicrosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder.forPort(9090)
                .addService(userServiceGRPC)
                .build()
                .start();

        System.out.println("gRPC Server started at port: " + server.getPort());
        server.awaitTermination();
    }
}
