package events;

import store.SqlEvent;
import models.Membership;
import models.User;
import models.UserId;

import java.sql.Timestamp;

public class MembershipDeliveredEvent implements Event {

    UserId id;
    Timestamp deliveryTime;
    Timestamp expirationTime;

    private static final String eventType = "membership-delivered";

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MembershipDeliveredEvent) {
            return id.equals(((MembershipDeliveredEvent) obj).id) && deliveryTime.equals(((MembershipDeliveredEvent) obj).deliveryTime)
                    && expirationTime.equals(((MembershipDeliveredEvent) obj).expirationTime);
        }
        return false;
    }

    public MembershipDeliveredEvent(UserId id, Timestamp deliveryTime, Timestamp expirationTime) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.expirationTime = expirationTime;
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
        return deliveryTime;
    }

    @Override
    public Timestamp getEndTime() {
        return expirationTime;
    }

    @Override
    public void applyEventOnUser(User user) {
        user.getMembershipList().add(new Membership(deliveryTime, expirationTime));
    }

    static public Event tryToCast(SqlEvent event) {
        if (event.getEvent_type().equals(eventType)) {
            return new MembershipDeliveredEvent(new UserId(event.getUser_id()), event.getEvent_timestamp(), event.getEnd_timestamp());
        }
        return null;
    }
}
