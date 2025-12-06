package com.haisy.app.Services.WebSocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LeaderBoards {
    ConcurrentHashMap<String,Board> map = new ConcurrentHashMap<>();
  
    public void adduser(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            Board b = map.get(user.getQuizId());
            b.addNewUser(user); 
            System.out.println("already added in previous queue");
            return;
        }else{

            Board b = new Board();
            b.addNewUser(user);
            map.put(user.getQuizId(), b);
        }
        System.out.println("added in the new queue");
    }
    public List<UserProfile> getRankings(String quizId){
        if(map.containsKey(quizId)){
            return map.get(quizId).getRankings();
        }
        System.out.println("rankings are null");
        return null;
    }
    public void increaseScore(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).increaseScore(user.getUserName(), 1);
            System.out.println("Added score successfully");
            return;
        }
            System.out.println("invalid quiz id");
    }
    public void decreaseScore(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).decreaseScore(user.getUserName(), 1);
        }
    }
}
