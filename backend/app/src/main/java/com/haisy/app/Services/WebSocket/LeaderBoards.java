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
        System.out.println("at entry of leaderboard servce the quizid is " + user.getQuizId());
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
    public void updateScore(UserProfile user){
        if(map.containsKey(user.getQuizId())){
            map.get(user.getQuizId()).updateScore(user.getUserName(), user.getScore());
            System.out.println("Added score successfully");
            return;
        }
            System.out.println("invalid quiz id");
    }
    public void removeQuiz(String quizId){
        if(map.contains(quizId)){
            map.remove(quizId);
            System.out.println("delted the quiz with quiz id : " +  quizId);
        }
        System.out.println("no quiz found with quiz id " + quizId + "so nothing deleted");
    }
    public void removeUser(UserProfile user){
        if(map.contains(user.getQuizId())){
            map.get(user.getQuizId()).removeUser(user);
            System.out.println("user removed form quiz success fuly" + user.getQuizId() + " " + user.getUserName());
            return;
        }
        System.out.prinln("quiz id not found to remove the user");
    }
}
