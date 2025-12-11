package com.haisy.app.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.haisy.app.Services.WebSocket.LeaderBoards;
import com.haisy.app.Services.WebSocket.UserProfile;

@Controller
public class WebSocketController {
    @Autowired
    LeaderBoards leaderBoard;

    @Autowired
    private SimpMessagingTemplate template;
    
    @MessageMapping("/joinQuiz")
    public void joinQuiz(UserProfile user){
        System.out.println("at entry of joinQuiz contoller the quizid is " + user.getQuizId());
        leaderBoard.adduser(user);
        List<UserProfile> result = leaderBoard.getRankings(user.getQuizId());
        template.convertAndSend("/topic/quiz/rankings/"+user.getQuizId(),result);
    }

    @MessageMapping("/updateScore")
    public void updateScore(UserProfile user){
        leaderBoard.updateScore(user);
        List<UserProfile> result = leaderBoard.getRankings(user.getQuizId());
        template.convertAndSend("/topic/quiz/rankings/"+user.getQuizId(),result);
    }

    @MessageMapping("/getRankings/{quizId}")
    public void getRankings(@DestinationVariable String quizId){
        List<UserProfile> result = leaderBoard.getRankings(quizId);
        // template.convertAndSend("/topic/quiz/rankings/"+quizId, result);
    }
    @MessageMapping("/removeQuiz")
    public void removeQuiz(String quizId){
        leaderBoard.removeQuiz(quizId);
    }
    @MessageMapping("/removeUser")
    public void removeUser(UserProfile user){
        leaderBoard.removeUser(user);
    }

    

}
