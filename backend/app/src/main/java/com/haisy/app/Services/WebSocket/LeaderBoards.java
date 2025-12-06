package com.haisy.app.Services.WebSocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LeaderBoards {
    ConcurrentHashMap<String,Board> map = new ConcurrentHashMap<>();
  
    public String adduser(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            Board b = map.get(user.getQuizId());
            b.addNewUser(user); 
            return "already added in previous queue";
        }else{
            Board b = new Board();
            b.addNewUser(user);
            map.put(user.getQuizId(), b);
        }
        return "added new quiz then added";
    }
    public List<UserProfile> getRankings(String quizId){
        if(map.containsKey(quizId)){
            return map.get(quizId).getRankings();
        }
        return null;
    }
    public String increaseScore(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).increaseScore(user.getUserName(), 1);
            return "added score successfully";
        }
        return "invalid quiz id";
    }
    public String decreaseScore(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).decreaseScore(user.getUserName(), 1);
            return "decreased score successfully";
        }
        return "invalid quiz id";
    }
}
