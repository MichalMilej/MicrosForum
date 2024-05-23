package pl.michalmilej.notifmicros.notification.request;

import lombok.Data;

@Data
public class UpdateObservedUserIdsRequest {
    private String[] observedUserIds;
}
