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
        }else{
            Board b = new Board();
            b.addNewUser(user);
            map.put(user.getQuizId(), b);
        }
    }
    public List<UserProfile> getRankings(String quizId){
        if(map.containsKey(quizId)){
            return map.get(quizId).getRankings();
        }
        return null;
    }
}
