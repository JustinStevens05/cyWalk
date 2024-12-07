package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.Organization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.aspectj.weaver.ast.Or;

@Entity
public class AdminCredentials {
    @Schema(description = "Admin name", example = "John Doe")
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Schema(description = "Admin combined name", example = "ISU John Doe")
    @Id
    private String combinedName;

    @Schema(description = "Admin password", example = "password")
    private String password;

    public AdminCredentials() {}

    public void updateCombinedName() {
        this.combinedName = admin.getCombinedName();
    }

    public void setCombinedName(String combinedName) {
        this.combinedName = combinedName;
    }

    public String getPassword() {
        return password;
    }

    public AdminCredentials(Admin admin, String password) {
        this.admin = admin;
        this.password = password;
        updateCombinedName();
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        updateCombinedName();
    }

    public String getCombinedName() {
        return combinedName;
    }

}
