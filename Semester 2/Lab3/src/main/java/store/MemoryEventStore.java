package store;

import events.Event;
import models.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryEventStore implements EventStore {
    List<Event> events = new ArrayList<>();
    private Integer id;
    private final int START_EVENT_ID = 0;

    public MemoryEventStore() {
        id = START_EVENT_ID;
    }

    @Override
    public void storeEvent(Event event) {
        ++id;
        events.add(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return events;
    }

    @Override
    public List<Event> getEventsByUserId(UserId userId) {
        return events.stream()
                .filter(x -> x.getUserId().equals(userId.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public int getStartEventId() {
        return START_EVENT_ID;
    }

    @Override
    public int getEndEventId() {
        return id - 1;
    }

    @Override
    public List<Event> getEventsInRange(int beginEventId, int endEventId) {
        return events.subList(beginEventId, endEventId + 1);
    }

    public void clear() {
        id = START_EVENT_ID;
        events.clear();
    }
}
