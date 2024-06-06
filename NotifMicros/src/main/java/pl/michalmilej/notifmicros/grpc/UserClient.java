package pl.michalmilej.notifmicros.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

import java.util.concurrent.CompletableFuture;

@Service
public class UserClient {

    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private final UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;

    public UserClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
        userServiceFutureStub = UserServiceGrpc.newFutureStub(channel);
    }

    public User.GetUserResponse getUser(String userId) {
        User.GetUserRequest request = User.GetUserRequest.newBuilder().setId(userId).build();
        return userServiceBlockingStub.getUser(request);
    }

    public CompletableFuture<User.NotifyNewCommentIdResponse> notifyNewPostCommentId(String postId,
                                                                                      String postAuthorId,
                                                                                      String commentId) {
        User.NotifyNewCommentIdRequest request = User.NotifyNewCommentIdRequest.newBuilder()
                .setPostId(postId)
                .setPostAuthorId(postAuthorId)
                .setCommentId(commentId)
                .build();

        ListenableFuture<User.NotifyNewCommentIdResponse> futureResponse = userServiceFutureStub.notifyNewCommentId(request);

        CompletableFuture<User.NotifyNewCommentIdResponse> completableFuture = new CompletableFuture<>();
        futureResponse.addListener(() -> {
            try {
                completableFuture.complete(futureResponse.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);

        return completableFuture;
    }
}
