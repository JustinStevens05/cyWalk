package com.cywalk.spring_boot.Admins;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
public class AdminSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Admin session key", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @Schema(description = "Admin entity pointer")
    private Admin admin;

    @Schema(description = "Organization id", example = "1")
    private Long orgId;

    public AdminSession(Admin admin, Long orgId) {
        this.admin = admin;
        this.orgId = orgId;
    }

    public AdminSession(Long key, Admin admin, Long orgId) {
        this.id = key;
        this.admin = admin;
        this.orgId = orgId;
    }

    public AdminSession(Admin admin) {
        this.admin = admin;
    }

    public AdminSession() {

    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long key) {
        this.id = key;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
