package pl.milejmichal.usersmicros.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddNotificationRequest {
    private String userId;
}
