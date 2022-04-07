package events;

import errors.NoValidMembershipException;
import store.SqlEvent;
import models.Membership;
import models.User;
import models.UserId;

import java.sql.Timestamp;
import java.util.List;

public class MembershipProlongEvent implements Event {
    private static final String eventType = "membership-prolong";
    UserId id;
    Timestamp prolongationTime;
    Timestamp expirationTime;

    public MembershipProlongEvent(UserId id, Timestamp prolongationTime, Timestamp expirationTime) {
        this.id = id;
        this.prolongationTime = prolongationTime;
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
        return prolongationTime;
    }

    @Override
    public Timestamp getEndTime() {
        return expirationTime;
    }

    @Override
    public void applyEventOnUser(User user) {
        List<Membership> membershipList = user.getMembershipList();
        if (membershipList.isEmpty()){
            throw new NoValidMembershipException("Cannot apply MembershipProlongEvent on user, because it doesn't have any memberships");
        }
        membershipList.get(membershipList.size() - 1).setExpirationDate(expirationTime);
    }

    static public Event tryToCast(SqlEvent event) {
        if (event.getEvent_type().equals(eventType)) {
            return new MembershipProlongEvent(new UserId(event.getUser_id()), event.getEvent_timestamp(), event.getEnd_timestamp());
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MembershipProlongEvent) {
            return id.equals(((MembershipProlongEvent) obj).id) && prolongationTime.equals(((MembershipProlongEvent) obj).prolongationTime)
                    && expirationTime.equals(((MembershipProlongEvent) obj).expirationTime);
        }
        return false;
    }
}
