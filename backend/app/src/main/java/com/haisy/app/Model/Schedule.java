package com.haisy.app.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

@Embeddable
public class Schedule {
    LocalDateTime dateTime;   
    int duration;
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
        
}
