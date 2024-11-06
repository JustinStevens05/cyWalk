package com.cywalk.spring_boot.organizations;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "organization_users",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "username")
    )
    private Set<People> users = new HashSet<>();

    // Constructors

    public Organization() {}

    public Organization(String name) {
        this.name = name;
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
    }

    public void removeUser(People user) {
        this.users.remove(user);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
