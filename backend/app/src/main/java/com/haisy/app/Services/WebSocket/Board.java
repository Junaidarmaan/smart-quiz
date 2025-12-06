package com.haisy.app.Services.WebSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Board {
    List<UserProfile> userprofiles = Collections.synchronizedList(new ArrayList<>());  

    public void addNewUser(UserProfile user){
        userprofiles.add(user);
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
    public void increaseScore(String userName,int inc){
        for(UserProfile u : userprofiles){
            if(u.getUserName().equals(userName)){
                u.setScore(u.getScore()+inc);
            }
        }
        updateTheRankings();
    }
    public void decreaseScore(String userName,int inc){
        for(UserProfile u : userprofiles){
            if(u.getUserName().equals(userName)){
                u.setScore(u.getScore()-inc);
            }
        }
        updateTheRankings();
    }
}
