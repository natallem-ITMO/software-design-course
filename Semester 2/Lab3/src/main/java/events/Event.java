package events;

import models.User;

import java.sql.Timestamp;

public interface Event {

    String getEventType();

    String getUserId();

    Timestamp getEventTime();

    Timestamp getEndTime();

    void applyEventOnUser(User user);

}
