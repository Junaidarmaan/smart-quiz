package com.haisy.app.DTO;


public class QuizSchedule {
    String date;
    String time;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "QuizSchedule [date=" + date + ", time=" + time + "]";
    }
    
}
