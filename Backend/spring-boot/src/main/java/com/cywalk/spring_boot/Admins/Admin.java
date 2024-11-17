package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.Organization;
import jakarta.persistence.*;

@Entity
public class Admin {
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Id
    private String name;


}
