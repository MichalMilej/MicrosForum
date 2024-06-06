package pl.michalmilej.notifmicros.grpc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String id;

    private String username;

    private String email;
}
