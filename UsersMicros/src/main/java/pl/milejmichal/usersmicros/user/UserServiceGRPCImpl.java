package pl.milejmichal.usersmicros.user;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

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
                    .addAllObservedUserIds(user.getObservedUserIds())
                    .addAllNewPostIds(user.getNewPostIds())
                    .putAllPostNewCommentIds(user.getNewPostCommentIds())
                    .build();
            responseObserver.onNext(getUserResponse);
        } else {
            responseObserver.onError(new RuntimeException("User not found"));
        }
        responseObserver.onCompleted();
    }
}
