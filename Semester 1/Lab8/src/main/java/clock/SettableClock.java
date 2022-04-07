package clock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SettableClock implements Clock {

    private  Instant now;

    public SettableClock(Instant now) {
        this.now = now;
    }

    public void setNow(Instant now){
        this.now = now;
    }

    public void addMinutes(int minutes) {
        now = now.plus(minutes, ChronoUnit.MINUTES);
    }

    @Override
    public Instant now() {
        return now;
    }
}
