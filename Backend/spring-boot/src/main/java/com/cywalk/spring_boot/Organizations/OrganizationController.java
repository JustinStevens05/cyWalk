package com.cywalk.spring_boot.Organizations;
import java.util.HashMap;
import java.util.Map;

import com.cywalk.spring_boot.Admins.Admin;
import com.cywalk.spring_boot.Admins.AdminService;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Leaderboard.LeaderboardEntry;
import com.cywalk.spring_boot.Users.PeopleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private PeopleService peopleService;

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody CreateOrganizationRequest request) {
        return ResponseEntity.ok(organizationService.createOrganization(request.getName()));

    }

    @PostMapping("/get-id")
    public ResponseEntity<Map<String, Long>> getOrganizationIdByName(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        Optional<Long> orgIdOpt = organizationService.getOrganizationIdByName(name);
        if (orgIdOpt.isPresent()) {
            Map<String, Long> response = new HashMap<>();
            response.put("id", orgIdOpt.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/get-info")
    public ResponseEntity<Organization> getOrganizationInfoByUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Optional<Organization> orgOpt = organizationService.getOrganizationByUsername(username);
        if (orgOpt.isPresent()) {
            return ResponseEntity.ok(orgOpt.get());
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{orgId}/join")
    public ResponseEntity<Void> joinOrganization(@PathVariable Long orgId, @RequestBody JoinOrganizationRequest request) {
        boolean success = organizationService.joinOrganization(orgId, request.getUsername());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build(); // Organization/User not found or already a member
        }
    }

    @GetMapping("/{orgId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getOrganizationLeaderboard(@PathVariable Long orgId) {
        Optional<List<LeaderboardEntry>> leaderboardOpt = organizationService.getOrganizationLeaderboard(orgId);
        if (leaderboardOpt.isPresent()) {
            return ResponseEntity.ok(leaderboardOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{orgId}/users")
    public ResponseEntity<List<People>> listOrganizationUsers(@PathVariable Long orgId) {
        Optional<List<People>> usersOpt = organizationService.listOrganizationUsers(orgId);
        if (usersOpt.isPresent()) {
            return ResponseEntity.ok(usersOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @Schema(description = "Get all organizations")
    @ApiResponse(responseCode = "200", description = "List of all organizations")
    public ResponseEntity<List<Organization>> listAllOrganizations() {
        return ResponseEntity.ok(organizationService.listAllOrganizations());
    }

    @PostMapping("/remove/{key}/{username}")
    @Schema(description = "Remove a user from an organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User removed from organization"),
            @ApiResponse(responseCode = "421", description = "organization not found"),
            @ApiResponse(responseCode = "422", description = "user not found")
    })
    public ResponseEntity<Void> removeUserFromOrganization(
            @PathVariable @Parameter(name = "key", description = "admin session key", example = "1") Long key,
            @PathVariable @Parameter(name = "username", description = "Username to remove", example = "cdp") String username) {
       Optional<Admin> admin = adminService.getAdminFromSession(key);
       if (admin.isEmpty()) {
           return ResponseEntity.status(421).build();
       }
       Organization organization = admin.get().getOrganization();
       Optional<People> toRemove = peopleService.getUserByUsername(username);
       if (toRemove.isEmpty()) {
           return ResponseEntity.status(422).build();
       }
       organization.removeUser(toRemove.get());
       organizationService.saveOrganization(organization);
       peopleService.saveUser(toRemove.get());
       return ResponseEntity.ok().build();
    }
}
