package events;

import clock.Clock;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.HOURS;

public class EventStatisticImpl implements EventsStatistic {
    private Map<String, List<Long>> events = new HashMap<>();
    private final Clock clock;
    private final long startTime;

    public EventStatisticImpl(Clock clock) {
        this.clock = clock;
        startTime = this.clock.now().getEpochSecond();
    }

    @Override
    public void incEvent(String name) {
        events.computeIfAbsent(name, str -> new ArrayList<>())
                .add(clock.now().getEpochSecond());
    }

    @Override
    public double getEventStatisticByName(String name) {
        if (!events.containsKey(name)) {
            return 0;
        }
        long end = clock.now().getEpochSecond();
        long start = Math.max((end - HOURS.toSeconds(1L)), 0);
        Range<Long> timeRange = Range.between(start, end);
        return (double) events.get(name).stream().filter(timeRange::contains).count()
                / HOURS.toMinutes(1L);
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        return events.keySet().stream().collect(Collectors.toMap(Function.identity(), this::getEventStatisticByName));
    }

    @Override
    public void printStatistic() {
        long minutesLeft = Math.max((clock.now().getEpochSecond() - startTime) / HOURS.toMinutes(1L), 1);
        events.forEach((k, v) -> {
            double rpm = (double) v.size() / minutesLeft;
            System.out.println("Event: " + k + ", rpm: " + rpm);
        });
    }
}
