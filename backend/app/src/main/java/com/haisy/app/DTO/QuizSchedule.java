package com.haisy.app.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class QuizSchedule {
    LocalDate date;
    LocalTime time;
    int duration;

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
}
