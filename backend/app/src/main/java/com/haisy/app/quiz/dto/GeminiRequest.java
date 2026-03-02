package com.haisy.app.quiz.dto;


public class GeminiRequest {
    private String topic;
    private int quantity;
    private int difficulty;

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
}