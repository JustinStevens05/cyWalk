package com.cywalk.spring_boot.leaderboard;

public class LeaderboardEntry {
    private String username;
    private int totalSteps;
    private int rank;
    private int leaderboardId;

    public LeaderboardEntry(String username, int totalSteps) {
        this.username = username;
        this.totalSteps = totalSteps;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(int leaderboardId) {
        this.leaderboardId = leaderboardId;
    }
}
