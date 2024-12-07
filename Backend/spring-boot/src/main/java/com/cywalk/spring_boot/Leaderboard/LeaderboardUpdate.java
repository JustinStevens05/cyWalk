package com.cywalk.spring_boot.Leaderboard;

import java.util.List;

public class LeaderboardUpdate {
    private List<LeaderboardEntry> leaderboard;

    public LeaderboardUpdate() {}

    public LeaderboardUpdate(List<LeaderboardEntry> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<LeaderboardEntry> leaderboard) {
        this.leaderboard = leaderboard;
    }
}