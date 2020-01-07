package com.goldengear.tikvah;

public class Event {
    String eventType;
    String homeoraway;
    String name;
    String time;
    public Event(String eventtype, String homeoraway, String name, String time) {
        this.eventType = eventtype;
        this.homeoraway = homeoraway;
        this.name = name;
        this.time = time;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getHomeoraway() {
        return homeoraway;
    }

    public void setHomeoraway(String homeoraway) {
        this.homeoraway = homeoraway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
