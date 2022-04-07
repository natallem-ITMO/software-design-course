import clock.SettableClock;
import events.EventStatisticImpl;
import events.EventsStatistic;
import org.junit.*;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClockTests {
    private SettableClock clock;
    private EventsStatistic eventsStatistic;

    @Before
    public void beforeEach() {
        clock = new SettableClock(Instant.now());
        eventsStatistic = new EventStatisticImpl(clock);
    }

    @Test
    public void unknownEvent() {
        assertEquals(eventsStatistic.getEventStatisticByName("unknown"), 0.0, 0.0);
        assertTrue(eventsStatistic.getAllEventStatistic().isEmpty());
    }

    @Test
    public void singleEventNoTimeChanges() {
        String name = "name";
        eventsStatistic.incEvent(name);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 1.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap("name", 1.0 / 60.0));
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 3.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap("name", 3.0 / 60.0));
    }

    @Test
    public void multipleEventsNoTimeChanges() {
        String name1 = "name1";
        String name2 = "name2";
        eventsStatistic.incEvent(name1);
        eventsStatistic.incEvent(name2);
        eventsStatistic.incEvent(name2);
        assertEquals(eventsStatistic.getEventStatisticByName(name1), 1.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getEventStatisticByName(name2), 2.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Map.of(
                name1, 1.0 / 60.0,
                name2, 2.0 / 60.0
        ));
        eventsStatistic.incEvent(name1);
        assertEquals(eventsStatistic.getEventStatisticByName(name1), 2.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getEventStatisticByName(name2), 2.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Map.of(
                name1, 2.0 / 60.0,
                name2, 2.0 / 60.0
        ));
    }

    @Test
    public void singleEventTimeChanges() {
        String name = "name";
        eventsStatistic.incEvent(name);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 1.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap(name, 1.0 / 60.0));

        clock.addMinutes(25);
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 3.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap(name, 3.0 / 60.0));

        clock.addMinutes(25);
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        eventsStatistic.incEvent(name);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 7.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap(name, 7.0 / 60.0));

        clock.addMinutes(25);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 6.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap(name, 6.0 / 60.0));

        clock.addMinutes(60);
        assertEquals(eventsStatistic.getEventStatisticByName(name), 0.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Collections.singletonMap(name, 0.0 / 60.0));
    }

    @Test
    public void multipleEventsTimeChanges() {
        String name1 = "name1";
        String name2 = "name2";
        eventsStatistic.incEvent(name1);
        eventsStatistic.incEvent(name2);
        clock.addMinutes(40);
        eventsStatistic.incEvent(name2);
        assertEquals(eventsStatistic.getEventStatisticByName(name1), 1.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getEventStatisticByName(name2), 2.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Map.of(
                name1, 1.0 / 60.0,
                name2, 2.0 / 60.0
        ));

        clock.addMinutes(40);
        assertEquals(eventsStatistic.getEventStatisticByName(name1), 0.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getEventStatisticByName(name2), 1.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Map.of(
                name1, 0.0 / 60.0,
                name2, 1.0 / 60.0
        ));

        clock.addMinutes(40);
        assertEquals(eventsStatistic.getEventStatisticByName(name1), 0.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getEventStatisticByName(name2), 0.0 / 60.0, 0.0);
        assertEquals(eventsStatistic.getAllEventStatistic(), Map.of(
                name1, 0.0 / 60.0,
                name2, 0.0 / 60.0
        ));
    }

    @Test
    public void printStatisticTest() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        String name1 = "name1";
        String name2 = "name2";
        String name3 = "name3";
        eventsStatistic.incEvent(name1);
        eventsStatistic.incEvent(name2);
        eventsStatistic.incEvent(name2);
        eventsStatistic.incEvent(name3);
        eventsStatistic.incEvent(name3);
        eventsStatistic.incEvent(name3);
        eventsStatistic.printStatistic();
        clock.addMinutes(40);
        eventsStatistic.printStatistic();
        clock.addMinutes(40);
        eventsStatistic.printStatistic();
        assertEquals(
                "Event: name3, rpm: 3.0\n" +
                        "Event: name2, rpm: 2.0\n" +
                        "Event: name1, rpm: 1.0\n" +
                        "Event: name3, rpm: 0.075\n" +
                        "Event: name2, rpm: 0.05\n" +
                        "Event: name1, rpm: 0.025\n" +
                        "Event: name3, rpm: 0.0375\n" +
                        "Event: name2, rpm: 0.025\n" +
                        "Event: name1, rpm: 0.0125", outputStreamCaptor.toString().trim());
    }
}