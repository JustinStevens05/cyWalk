package com.cywalk.spring_boot.Organization;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Organization {
    @Id
    private String organizationName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<People> members;

    @OneToOne
    private People admin;

    public Organization(String organizationName, Set<People> members, People admin) {
        this.organizationName = organizationName;
        this.members = members;
        this.admin = admin;
    }

    public Organization() {

    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Set<People> getMembers() {
        return members;
    }

    public void setMembers(Set<People> members) {
        this.members = members;
    }

    public People getAdmin() {
        return admin;
    }

    public void setAdmin(People admin) {
        this.admin = admin;
    }

    public boolean hasMember(People person) {
        return members.contains(person);
    }
}
