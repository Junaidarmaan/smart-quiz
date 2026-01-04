package com.haisy.app.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Results {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String joinCode;
    byte[] resultXL;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getJoinCode() {
        return joinCode;
    }
    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }
    public byte[] getResultXL() {
        return resultXL;
    }
    public void setResultXL(byte[] resultXL) {
        this.resultXL = resultXL;
    }
}
