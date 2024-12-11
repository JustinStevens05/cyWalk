package com.cywalk.spring_boot.Users;

public class Guest {
    private String displayName;

    public Guest() {
        this.displayName = "Guest";
    }

    public Guest(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
