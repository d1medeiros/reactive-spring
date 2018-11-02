package com.example.demoreactiveone;

import java.util.Date;

public class Event {
    private long id;
    private Date when;

    public Event(long id, Date when) {
        this.id = id;
        this.when = when;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}
