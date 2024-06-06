package pl.milejmichal.usersmicros.user.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.milejmichal.usersmicros.user.UserRepository;
import user.User;
import user.UserServiceGrpc;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceGRPCImpl extends UserServiceGrpc.UserServiceImplBase {

    final UserRepository userRepository;

    @Override
    public void getUser(User.GetUserRequest request, StreamObserver<User.GetUserResponse> responseObserver) {
        var userOptional = userRepository.findById(request.getId());
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            User.GetUserResponse getUserResponse = User.GetUserResponse.newBuilder()
                    .setId(user.getId())
                    .setUsername(user.getUsername())
                    .setEmail(user.getEmail())
                    .build();
            responseObserver.onNext(getUserResponse);
        } else {
            responseObserver.onError(new RuntimeException("User not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void notifyNewCommentId(User.NotifyNewCommentIdRequest request,
                                   StreamObserver<User.NotifyNewCommentIdResponse> responseObserver) {
        String postId = request.getPostId();
        String postAuthorId = request.getPostAuthorId();
        String commentId = request.getCommentId();

        System.out.println("Received new comment notification: Post ID: " + postId + ", Post author ID: " + postAuthorId +
                ", Comment ID: " + commentId);

        var userOptional = userRepository.findById(request.getPostAuthorId());
        if (userOptional.isPresent()) {
            var user = userOptional.get();

            var newCommentIds = user.getNewCommentIds();
            if (!newCommentIds.containsKey(postId)) {
                newCommentIds.put(postId, new HashSet<>());
                newCommentIds.get(postId).add(commentId);
            } else {
                newCommentIds.get(postId).add(commentId);
            }
            userRepository.save(user);
        }

        User.NotifyNewCommentIdResponse response = User.NotifyNewCommentIdResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
