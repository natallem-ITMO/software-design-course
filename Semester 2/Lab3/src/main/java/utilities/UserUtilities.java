package utilities;

import clock.Clock;
import errors.NoValidMembershipException;
import errors.NoValidVisitException;
import events.*;
import models.Membership;
import store.EventStore;
import models.User;
import models.UserId;

import java.sql.Time;
import java.sql.Timestamp;

import java.time.Instant;
import java.util.List;

public class UserUtilities {
    private EventStore store;
    private Clock clock;

    public UserUtilities(EventStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    public User recreateUserState(UserId userId) {
        User user = new User(userId);

        List<Event> events = store.getEventsByUserId(userId);
        for (Event event : events) {
            event.applyEventOnUser(user);
        }
        return user;
    }

    public void prolongMembership(UserId id, Timestamp newExpiringDate) {
        User currentUser = recreateUserState(id);
        if (currentUser.getMembershipList().isEmpty()) {
            throw new NoValidMembershipException("Cannot prolong membership for user " + id.getUserId() + " because user have not got any valid membership");
        }
        Instant now = clock.now();
        if (currentUser.canProlongMembership(now, newExpiringDate)) {
            store.storeEvent(new MembershipProlongEvent(id, Timestamp.from(now), newExpiringDate));
        } else {
            throw new NoValidMembershipException("Cannot prolong membership for user " + id.getUserId() + " because of incorrect prolongation timestamp");
        }
    }

    public void deliverMembership(UserId id, Timestamp start, Timestamp expired) {
        Instant now = clock.now();
        Membership membership = new Membership(start, expired);
        if (membership.isValid(now)) {
            store.storeEvent(new MembershipDeliveredEvent(id, start, expired));
        } else {
            throw new NoValidMembershipException("Cannot deliver membership for user " + id.getUserId() + " because of incorrect membership");
        }
    }

    public void enterUser(UserId id) {
        User currentUser = recreateUserState(id);
        Instant now = clock.now();
        if (!currentUser.hasValidMembership(now)) {
            throw new NoValidMembershipException("Client " + id.getUserId() + " have no membership");
        }
        if (currentUser.isInGym()) {
            throw new NoValidVisitException("Client " + id.getUserId() + " is already in entered");
        }
        store.storeEvent(new ClientEnteredEvent(id, Timestamp.from(now)));
    }

    public void wentOutUser(UserId id) {
        User currentUser = recreateUserState(id);
        Instant now = clock.now();
        if (!currentUser.isInGym()){
            throw new NoValidVisitException("Client " + id.getUserId() + " cannot went out from gym because haven't entered");
        }
        store.storeEvent(new ClientWentOutEvent(id, Timestamp.from(now)));
    }

    public String getUserInfo(UserId id){
        User currentUser = recreateUserState(id);
        return currentUser.toString();
    }


}
