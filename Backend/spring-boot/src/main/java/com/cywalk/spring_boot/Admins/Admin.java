package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.Organization;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Schema(description = "The admin entity, which is basically the admin entity.")
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonBackReference(value = "organization_admins")
    @Schema(description = "Organization", example = "ISU")
    private Organization organization;

    @Schema(description = "Admin name", example = "John Doe")
    @Column(unique = false, nullable = false)
    private String name;

    public Admin() {}

    public Admin(Organization organization, String name) {
        this.organization = organization;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Admin(Long id, Organization organization, String name) {
        this.id = id;
        this.organization = organization;
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCombinedName() {
        return organization.getName() + " " + name;
    }
}
