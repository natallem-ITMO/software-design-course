package utilities;


import clock.SettableClock;
import errors.NoValidMembershipException;
import errors.NoValidVisitException;
import events.Event;
import events.MembershipDeliveredEvent;
import models.UserId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import store.MemoryEventStore;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class UserUtilitiesTest {
    Instant instant
            = Instant.parse("2018-11-30T18:35:24.00Z");
    SettableClock clock = new SettableClock(instant);
    MemoryEventStore memoryEventStore = new MemoryEventStore();
    UserUtilities userUtilities = new UserUtilities(memoryEventStore, clock);

    @Before
    public void setUp() {
        memoryEventStore.clear();
    }

    Timestamp timestampFromString(String str) {
        return Timestamp.from(Instant.parse(str));
    }

    @Test
    public void testNormalDeliverMembership() {
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2018-11-25T18:35:24.00Z"), timestampFromString("2018-12-29T18:35:24.00Z"));
        List<Event> allEvents =
                memoryEventStore.getAllEvents();
        Assert.assertEquals(allEvents, List.of(new MembershipDeliveredEvent(new UserId("1"), timestampFromString("2018-11-25T18:35:24.00Z"), timestampFromString("2018-12-29T18:35:24.00Z"))));
    }

    @Test(expected = NoValidMembershipException.class)
    public void testDeliverMembershipNowAfterExpires() {
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2018-11-25T18:35:24.00Z"), timestampFromString("2018-11-29T18:35:24.00Z"));
    }

    @Test(expected = NoValidMembershipException.class)
    public void testDeliverMembershipInvalidMembership() {
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-01T00:00:00.00Z"));
    }

    @Test(expected = NoValidMembershipException.class)
    public void testProlongMembershipNoMembership() {
        userUtilities.prolongMembership(new UserId("1"), timestampFromString("2000-01-03T00:00:00.00Z"));
    }

    @Test
    public void testProlongMembershipCorrect() {
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        userUtilities.prolongMembership(new UserId("1"), timestampFromString("2000-01-15T00:00:00.00Z"));
    }

    @Test(expected = NoValidMembershipException.class)
    public void testProlongMembershipNoValidMembership() {
        clock.setNow(Instant.parse("2000-01-03T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        clock.setNow(Instant.parse("2000-01-13T00:00:00.00Z"));
        userUtilities.prolongMembership(new UserId("1"), timestampFromString("2000-01-15T00:00:00.00Z"));
    }

    @Test
    public void testEnterUserCorrect() {
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));

    }

    @Test(expected = NoValidMembershipException.class)
    public void testEnterUserNoMembership() {
        clock.setNow(Instant.parse("2000-01-03T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        clock.setNow(Instant.parse("2000-01-13T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
    }

    @Test(expected = NoValidVisitException.class)
    public void testEnterUserAlreadyEntered() {
        clock.setNow(Instant.parse("2000-01-03T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
        clock.setNow(Instant.parse("2000-01-07T01:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
    }

    @Test
    public void testWentOutUserCorrect() {
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
        clock.setNow(Instant.parse("2000-01-07T01:00:00.00Z"));
        userUtilities.wentOutUser(new UserId("1"));
    }


    @Test(expected = NoValidVisitException.class)
    public void testWentOutUserAlreadyWentOut() {
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-05T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
        clock.setNow(Instant.parse("2000-01-07T01:00:00.00Z"));
        userUtilities.wentOutUser(new UserId("1"));
        userUtilities.wentOutUser(new UserId("1"));
    }

}
