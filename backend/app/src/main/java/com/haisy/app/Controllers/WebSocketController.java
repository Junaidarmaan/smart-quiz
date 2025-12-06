package com.haisy.app.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    }

    @MessageMapping("/updateScore")
    public void updateScore(UserProfile user){
        leaderBoard.increaseScore(user);
    }

    @MessageMapping("/getRankings/{quizId}")
    public void getRankings(@DestinationVariable String quizId){
        List<UserProfile> result = leaderBoard.getRankings(quizId);
        template.convertAndSend("/topic/quiz/rankings/"+quizId, result);
    }

}
