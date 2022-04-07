package clock;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RealClock implements Clock {

    public RealClock(){}

    @Override
    public Instant now() {
        return Instant.now();
    }
}