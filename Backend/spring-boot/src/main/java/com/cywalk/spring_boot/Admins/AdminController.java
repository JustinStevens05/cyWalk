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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    OrganizationService organizationService;


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

        AdminModel adminModel = new AdminModel(credentials.getAdminName(), credentials.getPassword());
        // second check is if the admin exists
        if (!adminService.adminExists(adminModel)) {
            return ResponseEntity.status(401).build();
        }

        // third check is if the admin cooresponding to the admin model, has credential consistent with the adminCredential object for a given admin
        Optional<AdminSession> adminSession = adminService.loginAdmin(adminModel, credentials.getOrganizationName());
        return adminSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(402).build());
    }


    @DeleteMapping("/logout/{sessionKey}")
    @Operation(summary = "Log out an admin", description = "Log out an admin and invalidate the session key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin logged out"),
            @ApiResponse(responseCode = "400", description = "no existing session key"),
    })
    public ResponseEntity<Void> logoutAdmin(
          @PathVariable @Parameter(name = "sessionKey", description = "The session key") Long sessionKey
    ) {
        if (adminService.logoutAdmin(sessionKey)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }





}
