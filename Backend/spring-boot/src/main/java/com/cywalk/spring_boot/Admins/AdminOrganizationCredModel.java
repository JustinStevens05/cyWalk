package com.cywalk.spring_boot.Admins;

public class AdminOrganizationCredModel {
    String organizationName;
    String adminName;
    String password;

    public AdminOrganizationCredModel(String organizationName, String adminName, String password) {
        this.organizationName = organizationName;
        this.adminName = adminName;
        this.password = password;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
