package pl.michalmilej.notifmicros.user;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

@Service
public class UserClient {

    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public UserClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public User.GetUserResponse getUser(String userId) {
        User.GetUserRequest request = User.GetUserRequest.newBuilder().setId(userId).build();
        return userServiceBlockingStub.getUser(request);
    }
}
