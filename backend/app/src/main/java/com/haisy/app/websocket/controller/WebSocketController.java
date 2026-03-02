package com.haisy.app.websocket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.haisy.app.websocket.services.LeaderBoards;
import com.haisy.app.websocket.services.UserProfile;

@Controller
public class WebSocketController {
    private final LeaderBoards leaderBoard;
    private final SimpMessagingTemplate template;

    public WebSocketController(LeaderBoards leaderBoard, SimpMessagingTemplate template) {
        this.leaderBoard = leaderBoard;
        this.template = template;
    }

    @MessageMapping("/joinQuiz")
    public void joinQuiz(UserProfile user) {
        leaderBoard.adduser(user);
        List<UserProfile> result = leaderBoard.getRankings(user.getQuizId());
        template.convertAndSend("/topic/quiz/rankings/" + user.getQuizId(), result);
    }

    @MessageMapping("/updateScore")
    public void updateScore(UserProfile user) {
        leaderBoard.updateScore(user);
        List<UserProfile> result = leaderBoard.getRankings(user.getQuizId());
        template.convertAndSend("/topic/quiz/rankings/" + user.getQuizId(), result);
        template.convertAndSend("/topic/quiz/scoreUpdates/" + user.getUserName(),
                leaderBoard.getUserProfileStatus(user));
    }

    @MessageMapping("/getRankings/{quizId}")
    public void getRankings(@DestinationVariable String quizId) {
        List<UserProfile> result = leaderBoard.getRankings(quizId);
        template.convertAndSend("/topic/quiz/rankings/"+quizId, result);
    }

    @MessageMapping("/removeQuiz")
    public void removeQuiz(String quizId) {
        leaderBoard.removeQuiz(quizId);
    }

    @MessageMapping("/removeUser")
    public void removeUser(UserProfile user) {
        leaderBoard.removeUser(user);
    }

}
