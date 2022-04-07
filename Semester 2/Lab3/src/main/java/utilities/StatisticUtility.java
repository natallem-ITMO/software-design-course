package utilities;

import clock.Clock;
import clock.SettableClock;
import errors.NoValidVisitException;
import events.ClientEnteredEvent;
import events.ClientWentOutEvent;
import events.Event;
import models.UserId;
import models.Visit;
import store.EventStore;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StatisticUtility {

    private final EventStore store;
    private final Clock clock;
    int lastEventId;

    Map<UserId, Visit> visitMap = new HashMap<>();
    Map<String, Integer> dateMap = new HashMap<>();

    long allVisitsNumber = 0;
    long minutesAllVisitsDuration = 0;


    public StatisticUtility(EventStore store, Clock clock) {
        this.store = store;
        this.clock = clock;

        lastEventId = store.getStartEventId();
        collectInfo(lastEventId);
    }

    private void collectInfo(int lastEventId) {
        this.lastEventId = store.getEndEventId();
        List<Event> eventsInRange = store.getEventsInRange(lastEventId, this.lastEventId);
        for (Event event : eventsInRange) {
            processEvent(event);
        }
    }

    private void processEvent(Event event) {
        if (event instanceof ClientWentOutEvent) {
            ClientWentOutEvent wentOutEvent = (ClientWentOutEvent) event;
            UserId userId = wentOutEvent.getId();
            if (!visitMap.containsKey(userId)) {
                throw new NoValidVisitException("Cannot process Event in statistic");
            }
            Visit visit = visitMap.get(userId);
            visit.setWentOutTime(wentOutEvent.getTime());
            processVisit(visit);
            visitMap.remove(userId);
        } else if (event instanceof ClientEnteredEvent) {
            ClientEnteredEvent enteredEvent = (ClientEnteredEvent) event;
            UserId userId = enteredEvent.getId();
            visitMap.put(userId, new Visit(enteredEvent.getTime(), null));
        }
    }

    private void processVisit(Visit visit) {
        Instant enterInstant = visit.getEnterTime().toInstant();
        Date date = new Date(enterInstant.toEpochMilli());
       String dateString =  DATE_FORMATE.format(date);
        dateMap.putIfAbsent(dateString, 0);
        dateMap.compute(dateString, (k, v) -> v + 1);
        ++allVisitsNumber;
        minutesAllVisitsDuration += visit.calculateDurationInMinutes();
    }

    public String getDayStatistics() {
        collectInfo(lastEventId + 1);
        String result = dateMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining("\n"));
        return result;
    }

    public String getAverageStatistics() {
        collectInfo(lastEventId + 1);
        return "Average duration " + (double) minutesAllVisitsDuration / allVisitsNumber +"\n" +
                "Average visit number per day " + (double) allVisitsNumber / dateMap.size();
    }

    private void hello() {
        Timestamp timestamp;
        Instant now = clock.now();
        timestamp = Timestamp.from(now);
//        Instant.now().atZone(ZoneId.systemDefault()).
    }

    static private final DateFormat DATE_FORMATE = new SimpleDateFormat("dd-MM-yyyy");

    public boolean isTimestampInToday(final long timestamp) {
        return false;
//        Instant now = clock.now();
//        return sdf.format(new Date(now.toEpochMilli())).equals(sdf.format(new Date()));
    }

    public static void main(String[] args) {
        SettableClock settableClock = new SettableClock(Instant.now());
        Instant now = settableClock.now();
        System.out.println(DATE_FORMATE.format(new Date(now.toEpochMilli())));
    }
}
