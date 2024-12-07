package com.cywalk.spring_boot.organizations;

public class CreateOrganizationRequest {
    private String name;

    public CreateOrganizationRequest() {}

    public CreateOrganizationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
