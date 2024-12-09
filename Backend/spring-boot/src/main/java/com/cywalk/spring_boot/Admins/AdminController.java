package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.CreateOrganizationRequest;
import com.cywalk.spring_boot.Organizations.Organization;
import com.cywalk.spring_boot.Organizations.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    OrganizationService organizationService;

    @PutMapping("/login")
    @Operation(summary = "Log in an admin", description = "Log in an admin and get back a key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin logged in"),
            @ApiResponse(responseCode = "400", description = "no existing organization"),
            @ApiResponse(responseCode = "401", description = "no existing admin"),
            @ApiResponse(responseCode = "402", description = "Unauthorized")
    })
    public ResponseEntity<AdminSession> loginAdmin(
            @RequestBody @Parameter(name = "credentials", description = "The combined credentials") AdminOrganizationCredModel credentials
    ) {
        // first check is if the organization exists
        if (!organizationService.organizationExists(credentials.getOrganizationName())) {
            return ResponseEntity.status(400).build();
        }

        AdminModel adminModel = new AdminModel(credentials.getAdminName(), credentials.getPassword());q
        // second check is if the admin exists
        if (!adminService.adminExists(adminModel)) {
            return ResponseEntity.status(401).build();
        }

        // third check is if the admin cooresponding to the admin model, has credential consistent with the adminCredential object for a given admin
        Optional<AdminSession> adminSession = adminService.loginAdmin(adminModel);
        return adminSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(402).build());
    }



}
