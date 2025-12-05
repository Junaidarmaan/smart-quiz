package com.haisy.app.Services.WebSocket;

public class UserProfile {
    String userName;
    String quizId;
    int score;
    public UserProfile(String userName){
        this.userName = userName;
        this.score = 0;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getQuizId() {
        return quizId;
    }
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
