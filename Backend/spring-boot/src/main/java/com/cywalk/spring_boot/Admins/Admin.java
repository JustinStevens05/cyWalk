package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.Organization;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "requestID")
public class Admin {
    @ManyToOne
    @JoinColumn(name = "organization_id")
    @Schema(description = "Organization", example = "ISU")
    private Organization organization;

    @Schema(description = "Admin name", example = "John Doe")
    private String name;

    @Schema(description = "Admin combined name", example = "ISU John Doe")
    @Id
    @Column(unique = true, nullable = false)
    private String combinedName;

    public Admin() {}

    public void updateCombinedName() {
        this.combinedName = organization.getName() + " " + name;
    }

    public Admin(Organization organization, String name) {
        this.organization = organization;
        this.name = name;
        updateCombinedName();
    }

    public Admin(Organization organization, String name, String combinedName) {
        this.organization = organization;
        this.name = name;
        this.combinedName = combinedName;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
        updateCombinedName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateCombinedName();
    }

    public String getCombinedName() {
        return organization.getName() + " " + name;
    }

}
