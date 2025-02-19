package com.example.bestworkout;

public class Workout {
    private String type;
    private final String time;
    private final String days;

    public Workout(String type, String time, String days) {
        this.type = type;
        this.time = time;
        this.days = days;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getDays() {
        return days;
    }
}