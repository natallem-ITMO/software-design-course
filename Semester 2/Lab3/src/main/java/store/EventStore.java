package store;

import events.Event;
import models.UserId;

import java.util.List;

public interface EventStore {

    void storeEvent(Event event);

    List<Event> getAllEvents();

    List<Event> getEventsByUserId(UserId userId);

    int getStartEventId();

    int getEndEventId();

    List<Event> getEventsInRange(int beginEventId, int endEventId);
}
