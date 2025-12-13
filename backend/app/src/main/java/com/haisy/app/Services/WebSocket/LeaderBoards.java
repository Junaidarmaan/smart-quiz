package com.haisy.app.Services.WebSocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LeaderBoards {
    ConcurrentHashMap<String,Board> map = new ConcurrentHashMap<>();
  
    public boolean isValidQuizId(String id){
        return map.containsKey(id);
    }
    public void adduser(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            Board b = map.get(user.getQuizId());
            b.addNewUser(user); 
            System.out.println("quiz id found in the map");
            return;
        }else{
            Board b = new Board();
            b.addNewUser(user);
            map.put(user.getQuizId(), b);
        }
        System.out.println("created new quiz");
    }
    public List<UserProfile> getRankings(String quizId){
        if(map.containsKey(quizId)){
            return map.get(quizId).getRankings();
        }
        System.out.println("rankings are null");
        return null;
    }
    public void updateScore(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).updateScore(user.getUserName(), user.getScore());
            System.out.println("score updated for " +user.getUserName() + " was successfull");
            return;
        }
            System.out.println("invalid quiz id");
    }

    public UserProfile getUserProfileStatus(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            Board b = map.get(user.getQuizId());
            UserProfile u = b.getUser(user.getUserName());
            return u;
        }
        return null;
    }

    public void removeQuiz(String quizId){
        if(map.contains(quizId)){
            map.remove(quizId);
            System.out.println("delted the quiz with quiz id : " +  quizId);
        }
        System.out.println("no quiz found with quiz id " + quizId + "so nothing deleted");
    }
    public void removeUser(UserProfile user){
        System.out.println("at removing methods the quiz id is " + user.getQuizId());
        System.out.println("at removing methods the map  is " + map);

        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).removeUser(user);
            System.out.println("user removed form quiz success fuly" + user.getQuizId() + " " + user.getUserName());
            return;
        }
        System.out.println("quiz id not found to remove the user");
    }
}
