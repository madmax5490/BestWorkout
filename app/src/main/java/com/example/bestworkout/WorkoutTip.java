package com.example.bestworkout;

public class WorkoutTip {
    private String tipText;
    private int imageResourceId;

    public WorkoutTip(String tipText, int imageResourceId) {
        this.tipText = tipText;
        this.imageResourceId = imageResourceId;
    }

    public String getTipText() {
        return tipText;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}