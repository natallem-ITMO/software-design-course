package utilities;

//import org.apache.http.client.utils.URIBuilder;

import clock.SettableClock;
import errors.NoValidMembershipException;
import errors.NoValidVisitException;
import events.Event;
import events.MembershipDeliveredEvent;
import models.User;
import models.UserId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import store.MemoryEventStore;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class StatisticUtilitiesTest {
    Instant instant
            = Instant.parse("2000-01-05T00:00:00.00Z");
    SettableClock clock = new SettableClock(instant);
    MemoryEventStore memoryEventStore = new MemoryEventStore();
    StatisticUtility statisticUtility = new StatisticUtility(memoryEventStore, clock);
    UserUtilities userUtilities = new UserUtilities(memoryEventStore, clock);

    @Before
    public void setUp() {
        memoryEventStore.clear();
    }

    Timestamp timestampFromString(String str) {
        return Timestamp.from(Instant.parse(str));
    }

    @Test
    public void testEmptyStorageOneVisit() {
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-01T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
        clock.setNow(Instant.parse("2000-01-07T00:01:00.00Z"));
        userUtilities.wentOutUser(new UserId("1"));
        String dayStatistics = statisticUtility.getDayStatistics();
        String averageStatistics = statisticUtility.getAverageStatistics();
        Assert.assertEquals(dayStatistics, "07-01-2000 -> 1");
        Assert.assertEquals(averageStatistics,
                "Average duration 1.0\n" +
                "Average visit number per day 1.0");
    }

    @Test
    public void testEmptyStorageManyVisits() {
        userUtilities.deliverMembership(new UserId("1"), timestampFromString("2000-01-01T00:00:00.00Z"), timestampFromString("2000-01-10T00:00:00.00Z"));
        clock.setNow(Instant.parse("2000-01-07T00:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
        clock.setNow(Instant.parse("2000-01-07T00:01:00.00Z"));
        userUtilities.wentOutUser(new UserId("1"));

        clock.setNow(Instant.parse("2000-01-08T13:00:00.00Z"));
        userUtilities.enterUser(new UserId("1"));
        clock.setNow(Instant.parse("2000-01-08T14:30:00.00Z"));
        userUtilities.wentOutUser(new UserId("1"));

        userUtilities.deliverMembership(new UserId("2"), timestampFromString("2000-01-15T00:00:00.00Z"), timestampFromString("2000-01-20T00:00:00.00Z"));
        clock.setNow(Instant.parse("2000-01-18T16:00:00.00Z"));
        userUtilities.enterUser(new UserId("2"));
        clock.setNow(Instant.parse("2000-01-18T16:56:00.00Z"));
        userUtilities.wentOutUser(new UserId("2"));

        clock.setNow(Instant.parse("2000-01-18T19:00:00.00Z"));
        userUtilities.enterUser(new UserId("2"));
        clock.setNow(Instant.parse("2000-01-18T19:56:00.00Z"));
        userUtilities.wentOutUser(new UserId("2"));


        String dayStatistics = statisticUtility.getDayStatistics();
        String averageStatistics = statisticUtility.getAverageStatistics();
        Assert.assertEquals(dayStatistics,
                "07-01-2000 -> 1\n" +
                        "08-01-2000 -> 1\n" +
                        "18-01-2000 -> 2"
        );
        Assert.assertEquals(averageStatistics,
                "Average duration 50.75\n" +
                        "Average visit number per day 1.3333333333333333");
    }

}
