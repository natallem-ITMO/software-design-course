package models;

import java.sql.Timestamp;
import java.time.Duration;

public class Visit {
    Timestamp enterTime;
    Timestamp wentOutTime;

    public Visit(Timestamp enterTime, Timestamp wentOutTime) {
        this.enterTime = enterTime;
        this.wentOutTime = wentOutTime;
    }

    public void setWentOutTime(Timestamp time) {
        wentOutTime = time;
    }

    public Timestamp getEnterTime() {
        return enterTime;
    }

    public Timestamp getWentOutTime() {
        return wentOutTime;
    }

    @Override
    public String toString() {
        return "enter : " + enterTime.toString() + " went out: " + wentOutTime.toString();
    }

    public long calculateDurationInMinutes() {
        Duration res = Duration.between(enterTime.toInstant(), wentOutTime.toInstant());
        return res.toMinutes();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Visit) {
            return enterTime.equals(((Visit) obj).enterTime)
                    && wentOutTime.equals(((Visit) obj).wentOutTime);
        }
        return false;
    }
}


