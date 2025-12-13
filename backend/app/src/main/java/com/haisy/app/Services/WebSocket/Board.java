package com.haisy.app.Services.WebSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Board {
    List<UserProfile> userprofiles = Collections.synchronizedList(new ArrayList<>());  

    public void addNewUser(UserProfile user){
        for(UserProfile el : userprofiles){
            if(user.getUserName().equals(el.getUserName())){
                System.out.println("user alredy there in the quiz so not added again");
                return;
            }
        }
        userprofiles.add(user);
        System.out.println("user addeds new user");
        updateTheRankings();
    }
    public void removeUser(UserProfile user){
        userprofiles.removeIf(u->u.getUserName().equals(user.getUserName()));
        updateTheRankings();
    }
    public void updateTheRankings(){
        userprofiles.sort(Comparator.comparingInt(UserProfile::getScore).reversed());
    }
    public List<UserProfile> getRankings(){
        updateTheRankings();
        return  userprofiles;
    }
    public void updateScore(String userName,int inc){
        for(UserProfile u : userprofiles){
            if(u.getUserName().equals(userName)){
                u.setScore(u.getScore()+1);
                u.setCurQuestion(u.getCurQuestion()+1);
            }
        }
        updateTheRankings();
        
    }
    public UserProfile getUser(String name){
        for(UserProfile u : userprofiles){
            if(u.getUserName().equals(name)){
                return u;
            }
        }
        return null;
    }
    
    
}
