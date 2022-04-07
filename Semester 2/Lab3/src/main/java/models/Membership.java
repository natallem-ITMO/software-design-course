package models;

import events.ClientEnteredEvent;

import java.sql.Timestamp;
import java.time.Instant;

public class Membership {
    Timestamp start;
    Timestamp expires;

    public Membership(Timestamp start, Timestamp expires) {
        this.start = start;
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "from : " + start.toString() + " to: " + expires;
    }

    public void setExpirationDate(Timestamp expirationTime) {
        expires = expirationTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Membership) {
            return start.equals(((Membership) obj).start) && expires.equals(((Membership) obj).expires);
        }
        return false;
    }

    public boolean isValid(Instant now) {
        Instant expireInstant = expires.toInstant();
        Instant startInstant = start.toInstant();
        boolean before1 = startInstant.isBefore(expireInstant);
        boolean before = now.isBefore(expireInstant);
        return startInstant.isBefore(expireInstant) && now.isBefore(expireInstant);
    }
}
