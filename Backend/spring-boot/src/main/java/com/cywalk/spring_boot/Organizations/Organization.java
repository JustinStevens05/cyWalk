package com.cywalk.spring_boot.Organizations;

import com.cywalk.spring_boot.Admins.Admin;
import com.cywalk.spring_boot.Users.People;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<People> users = new HashSet<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Admin> admins = new HashSet<>();

    public Organization() {}

    public Organization(String name) {
        this.name = name;
    }

    public Organization(Set<Admin> admin, String name) {
        this.admins = admin;
        this.name = name;
    }

    public Organization(Long id, String name, Set<People> users, Set<Admin> admin) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.admins = admin;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<People> getUsers() {
        return users;
    }

    public void setUsers(Set<People> users) {
        this.users = users;
    }

    public void addUser(People user) {
        this.users.add(user);
        user.setOrganization(this);
    }

    public Set<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Admin> admins) {
        this.admins = admins;
    }

    public void addAdmin(Admin admin) {
        this.admins.add(admin);
        admin.setOrganization(this);
    }

    public void removeUser(People user) {
        this.users.remove(user);
        user.setOrganization(null);
    }


    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", admins=" + admins +
                '}';
    }
}