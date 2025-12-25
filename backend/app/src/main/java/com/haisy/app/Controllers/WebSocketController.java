package com.haisy.app.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.haisy.app.Logs.FileLogger;
import com.haisy.app.Services.WebSocket.LeaderBoards;
import com.haisy.app.Services.WebSocket.UserProfile;

@Controller
public class WebSocketController {

    @Autowired
    LeaderBoards leaderBoard;

    @Autowired
    private org.springframework.messaging.simp.SimpMessagingTemplate template;

    @MessageMapping("/joinQuiz")
    public void joinQuiz(UserProfile user) {
        FileLogger.info("User attempting to join quiz: " + user.getUserName());

        leaderBoard.adduser(user);

        List<UserProfile> result = leaderBoard.getRankings(user.getQuizId());
        template.convertAndSend("/topic/quiz/rankings/" + user.getQuizId(), result);

        FileLogger.info("User joined quiz successfully: " + user.getUserName());
    }

    @MessageMapping("/updateScore")
    public void updateScore(UserProfile user) {
        FileLogger.info("Score update received for user: " + user.getUserName());

        leaderBoard.updateScore(user);

        List<UserProfile> result = leaderBoard.getRankings(user.getQuizId());
        template.convertAndSend("/topic/quiz/rankings/" + user.getQuizId(), result);

        template.convertAndSend(
                "/topic/quiz/scoreUpdates/" + user.getUserName(),
                leaderBoard.getUserProfileStatus(user)
        );

        FileLogger.info("Score updated and broadcasted for user: " + user.getUserName());
    }

    @MessageMapping("/getRankings/{quizId}")
    public void getRankings(@DestinationVariable String quizId) {
        FileLogger.info("Fetching rankings for quiz: " + quizId);

        List<UserProfile> result = leaderBoard.getRankings(quizId);
        template.convertAndSend("/topic/quiz/rankings/" + quizId, result);
    }

    @MessageMapping("/removeQuiz")
    public void removeQuiz(String quizId) {
        FileLogger.info("Removing quiz: " + quizId);
        leaderBoard.removeQuiz(quizId);
    }

    @MessageMapping("/removeUser")
    public void removeUser(UserProfile user) {
        FileLogger.info("Removing user: " + user.getUserName());
        leaderBoard.removeUser(user);
    }
}
