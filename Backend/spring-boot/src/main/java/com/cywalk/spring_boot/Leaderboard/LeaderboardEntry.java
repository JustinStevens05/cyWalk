package com.cywalk.spring_boot.leaderboard;

public class LeaderboardEntry {
    private String username;
    private int totalSteps;
    private int rank;
    private int leaderboardId;

    public LeaderboardEntry() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaderboardEntry that = (LeaderboardEntry) o;

        if (totalSteps != that.totalSteps) return false;
        if (rank != that.rank) return false;
        if (leaderboardId != that.leaderboardId) return false;
        return username != null ? username.equals(that.username) : that.username == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + totalSteps;
        result = 31 * result + rank;
        result = 31 * result + leaderboardId;
        return result;
    }
}