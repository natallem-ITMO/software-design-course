package store;

import events.*;
import models.UserId;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventStoreJdbc extends JdbcDaoSupport implements EventStore {

    private final int START_EVENT_ID = 0;

    public EventStoreJdbc(DataSource dataSource) {
        super();
        setDataSource(dataSource);

    }

    private int getMaxId() {
        String sql = "select max(id) from events;";
        Integer count = getJdbcTemplate().queryForObject(sql, Integer.class);
        if (count == null) {
            return START_EVENT_ID;
        }
        return count + 1;
    }

    @Override
    public void storeEvent(Event event) {
        Integer id = getMaxId();
        String sql = "insert into events(event_type, user_id, event_timestamp, end_timestamp, id) values (?,?,?,?,?)";
        getJdbcTemplate().update(sql, event.getEventType(), event.getUserId(), event.getEventTime(), event.getEndTime(), id);
    }

    @Override
    public List<Event> getAllEvents() {
        String sql_events = "select * from events order by id asc";
        List<SqlEvent> lists = getJdbcTemplate().query(sql_events, new BeanPropertyRowMapper(SqlEvent.class));
        List<Event> result = lists.stream().map(this::createEvent).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Event> getEventsByUserId(UserId userId) {
        String sql_user_events = "select * from events where user_id=" + userId.getUserId() + " order by id asc";
        List<SqlEvent> lists = getJdbcTemplate().query(sql_user_events, new BeanPropertyRowMapper(SqlEvent.class));
        List<Event> result = lists.stream().map(this::createEvent).collect(Collectors.toList());
        return result;
    }

    @Override
    public int getStartEventId() {
        return START_EVENT_ID;
    }

    @Override
    public int getEndEventId() {
        Integer id = getMaxId();
        return id == START_EVENT_ID ? START_EVENT_ID : id - 1;
    }

    @Override
    public List<Event> getEventsInRange(int beginEventId, int endEventId) {
        String sql_events = "select * from events where id <= " +
                endEventId + " and id >= " + beginEventId + " order by id asc";
        List<SqlEvent> lists = getJdbcTemplate().query(sql_events, new BeanPropertyRowMapper(SqlEvent.class));
        List<Event> result = lists.stream().map(this::createEvent).collect(Collectors.toList());
        return result;
    }

    private Event createEvent(SqlEvent event) {
        List<Event> list = new ArrayList<>();
        list.add(ClientEnteredEvent.tryToCast(event));
        list.add(ClientWentOutEvent.tryToCast(event));
        list.add(MembershipDeliveredEvent.tryToCast(event));
        list.add(MembershipProlongEvent.tryToCast(event));
        Event event1 = list.stream().filter(Objects::nonNull).collect(Collectors.toList()).get(0);
        return event1;
    }
}
