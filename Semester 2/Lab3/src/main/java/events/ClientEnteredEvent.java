package events;

import models.Visit;
import store.SqlEvent;
import models.User;
import models.UserId;

import java.sql.Timestamp;

public class ClientEnteredEvent implements Event {
    private static final String eventType = "client-entered";
    UserId id;
    Timestamp time;

    public ClientEnteredEvent(UserId id, Timestamp time) {
        this.id = id;
        this.time = time;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getUserId() {
        return id.getUserId();
    }

    @Override
    public Timestamp getEventTime() {
        return time;
    }

    @Override
    public Timestamp getEndTime() {
        return null;
    }

    @Override
    public void applyEventOnUser(User user) {
        user.getVisitList().add(new Visit(time, null));
    }

    static public Event tryToCast(SqlEvent event) {
        if (event.getEvent_type().equals(eventType)) {
            return new ClientEnteredEvent(new UserId(event.getUser_id()), event.getEvent_timestamp());
        }
        return null;
    }

    public UserId getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClientEnteredEvent) {
            return id.equals(((ClientEnteredEvent) obj).id) && time.equals(((ClientEnteredEvent) obj).time);
        }
        return false;
    }
}
