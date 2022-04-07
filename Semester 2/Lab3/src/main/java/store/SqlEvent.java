package store;

import java.sql.Timestamp;

public class SqlEvent {
    String event_type;
    String user_id;
    Timestamp event_timestamp;
    Timestamp end_timestamp;
    Integer id;

    public SqlEvent(){}


    public void setEvent_type(String event_type) {

        this.event_type = event_type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getEvent_type() {
        return event_type;
    }

    public Integer getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setEvent_timestamp(Timestamp event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public void setEnd_timestamp(Timestamp end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public Timestamp getEvent_timestamp() {
        return event_timestamp;
    }

    public Timestamp getEnd_timestamp() {
        return end_timestamp;
    }
}
