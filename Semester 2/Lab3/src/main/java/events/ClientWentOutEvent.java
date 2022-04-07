package events;

import errors.NoValidVisitException;
import models.Visit;
import store.SqlEvent;
import models.User;
import models.UserId;

import java.sql.Timestamp;
import java.util.List;

public class ClientWentOutEvent implements  Event{
    private static final String eventType = "client-went-out";
    UserId id;
    Timestamp time;

    public ClientWentOutEvent(UserId id, Timestamp time) {
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
        List<Visit> visitList = user.getVisitList();
        if (visitList.isEmpty()){
            throw new NoValidVisitException("Cannot apply ClientWentOutEvent on user, because user doesn't entered gym");
        }
        if ( visitList.get(visitList.size() - 1).getWentOutTime() != null){
            throw new NoValidVisitException("Cannot apply ClientWentOutEvent on user, because user has already went out of gym");
        }
        visitList.get(visitList.size() - 1).setWentOutTime(time);
    }

    static public Event tryToCast(SqlEvent event) {
        if (event.getEvent_type().equals(eventType)) {
            return new ClientWentOutEvent(new UserId(event.getUser_id()), event.getEvent_timestamp());
        }
        return null;
    }

    public Timestamp getTime() {
        return time;
    }

    public UserId getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClientWentOutEvent) {
            return id.equals(((ClientWentOutEvent) obj).id) && time.equals(((ClientWentOutEvent) obj).time);
        }
        return false;
    }
}
