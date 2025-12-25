package com.haisy.app.Services.WebSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.haisy.app.Logs.FileLogger;

public class Board {

    List<UserProfile> userprofiles = Collections.synchronizedList(new ArrayList<>());

    public void addNewUser(UserProfile user) {
        FileLogger.info("Attempting to add user: " + user.getUserName());

        for (UserProfile el : userprofiles) {
            if (user.getUserName().equals(el.getUserName())) {
                FileLogger.info("User already exists, skipping add: " + user.getUserName());
                return;
            }
        }

        userprofiles.add(user);
        FileLogger.info("User added successfully: " + user.getUserName());

        updateTheRankings();
    }

    public void removeUser(UserProfile user) {
        FileLogger.info("Attempting to remove user: " + user.getUserName());

        userprofiles.removeIf(u -> u.getUserName().equals(user.getUserName()));

        FileLogger.info("User removed: " + user.getUserName());

        updateTheRankings();
    }

    public void updateTheRankings() {
        FileLogger.debug("Updating rankings");
        userprofiles.sort(Comparator.comparingInt(UserProfile::getScore).reversed());
    }

    public List<UserProfile> getRankings() {
        FileLogger.debug("Fetching leaderboard");
        updateTheRankings();
        return userprofiles;
    }

    public void updateScore(String userName, int inc) {
        FileLogger.info("Updating score for user: " + userName);

        for (UserProfile u : userprofiles) {
            if (u.getUserName().equals(userName)) {
                u.setScore(u.getScore() + inc);
                u.setCurQuestion(u.getCurQuestion() + 1);
                FileLogger.info("Updated score for user: " + userName);
                break;
            }
        }

        updateTheRankings();
    }

    public UserProfile getUser(String name) {
        FileLogger.debug("Fetching user: " + name);

        for (UserProfile u : userprofiles) {
            if (u.getUserName().equals(name)) {
                return u;
            }
        }
        return null;
    }
}
