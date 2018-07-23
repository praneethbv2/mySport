package com.praneeth.studio.mysport;

public class Event {

private String duration,eventName,location,time,eventkey;

    public Event() {

    }

    public Event(String duration, String eventName, String location, String time,String eventkey) {
        this.duration = duration;
        this.eventName = eventName;
        this.location = location;
        this.time = time;
        this.eventkey=eventkey;
    }

    public String getEventkey() {
        return eventkey;
    }

    public void setEventkey(String eventkey) {
        this.eventkey = eventkey;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
