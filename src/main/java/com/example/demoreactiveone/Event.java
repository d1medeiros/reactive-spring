package com.example.demoreactiveone;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("event")
public class Event {
    @Id
    private String _id;
    private Date when;

    public Event() {
    }

    public Event(long _id, Date when) {
        this._id = _id + "";
        this.when = when;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return "Event{" +
                "_id=" + _id +
                ", when=" + when +
                '}';
    }
}
