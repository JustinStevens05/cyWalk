package com.cywalk.spring_boot.Organizations;

public class JoinOrganizationRequest {
    private String username;

    public JoinOrganizationRequest() {}

    public JoinOrganizationRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
