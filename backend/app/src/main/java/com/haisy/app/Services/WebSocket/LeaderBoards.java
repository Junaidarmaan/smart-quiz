package com.haisy.app.Services.WebSocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.haisy.app.Logs.FileLogger;

@Service
public class LeaderBoards {

    ConcurrentHashMap<String, Board> map = new ConcurrentHashMap<>();

    public boolean isValidQuizId(String id) {
        return map.containsKey(id);
    }

    public void adduser(UserProfile user) {
        FileLogger.info("Attempting to add user to quiz: " + user.getQuizId());

        if (map.containsKey(user.getQuizId())) {
            Board b = map.get(user.getQuizId());
            b.addNewUser(user);
            FileLogger.info("User added to existing quiz: " + user.getUserName());
            return;
        }

        Board b = new Board();
        b.addNewUser(user);
        map.put(user.getQuizId(), b);

        FileLogger.info("New quiz created and user added. QuizId: " + user.getQuizId());
    }

    public List<UserProfile> getRankings(String quizId) {
        if (map.containsKey(quizId)) {
            FileLogger.debug("Fetching rankings for quiz: " + quizId);
            return map.get(quizId).getRankings();
        }
        FileLogger.debug("No rankings found for quiz: " + quizId);
        return null;
    }

    public void updateScore(UserProfile user) {
        if (map.containsKey(user.getQuizId())) {
            map.get(user.getQuizId()).updateScore(user.getUserName(), user.getScore());
            FileLogger.info("Score updated for user: " + user.getUserName());
            return;
        }
        FileLogger.error("Failed to update score. Quiz not found: " + user.getQuizId());
    }

    public UserProfile getUserProfileStatus(UserProfile user) {
        if (map.containsKey(user.getQuizId())) {
            FileLogger.debug("Fetching user profile for: " + user.getUserName());
            return map.get(user.getQuizId()).getUser(user.getUserName());
        }
        FileLogger.debug("User not found for quiz: " + user.getQuizId());
        return null;
    }

    public void removeQuiz(String quizId) {
        if (map.containsKey(quizId)) {
            map.remove(quizId);
            FileLogger.info("Quiz removed successfully: " + quizId);
            return;
        }
        FileLogger.error("Attempted to remove non-existing quiz: " + quizId);
    }

    public void removeUser(UserProfile user) {
        FileLogger.info("Attempting to remove user: " + user.getUserName());

        if (map.containsKey(user.getQuizId())) {
            map.get(user.getQuizId()).removeUser(user);
            FileLogger.info("User removed successfully: " + user.getUserName());
            return;
        }

        FileLogger.error("User removal failed, quiz not found: " + user.getQuizId());
    }
}
