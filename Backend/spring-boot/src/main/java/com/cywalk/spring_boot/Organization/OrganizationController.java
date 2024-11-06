package com.cywalk.spring_boot.organizations;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.leaderboard.LeaderboardEntry;
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

    /**
     * Endpoint to create a new organization.
     *
     * @param request the CreateOrganizationRequest containing the organization name
     * @return the created Organization object or 400 Bad Request if name exists
     */
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody CreateOrganizationRequest request) {
        Optional<Organization> orgOpt = organizationService.createOrganization(request.getName());
        if (orgOpt.isPresent()) {
            return ResponseEntity.ok(orgOpt.get());
        } else {
            return ResponseEntity.badRequest().build(); // Name already exists
        }
    }

    /**
     * Endpoint for a user to join an organization.
     *
     * @param orgId    the ID of the organization
     * @param request the JoinOrganizationRequest containing the username
     * @return 200 OK if joined successfully, 400 Bad Request otherwise
     */
    @PostMapping("/{orgId}/join")
    public ResponseEntity<Void> joinOrganization(@PathVariable Long orgId, @RequestBody JoinOrganizationRequest request) {
        boolean success = organizationService.joinOrganization(orgId, request.getUsername());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build(); // Organization/User not found or already a member
        }
    }

    /**
     * Endpoint to retrieve the leaderboard of an organization.
     *
     * @param orgId the ID of the organization
     * @return the list of LeaderboardEntry objects or 404 Not Found if organization doesn't exist
     */
    @GetMapping("/{orgId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getOrganizationLeaderboard(@PathVariable Long orgId) {
        Optional<List<LeaderboardEntry>> leaderboardOpt = organizationService.getOrganizationLeaderboard(orgId);
        if (leaderboardOpt.isPresent()) {
            return ResponseEntity.ok(leaderboardOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to list all users in an organization.
     *
     * @param orgId the ID of the organization
     * @return the list of People objects or 404 Not Found if organization doesn't exist
     */
    @GetMapping("/{orgId}/users")
    public ResponseEntity<List<People>> listOrganizationUsers(@PathVariable Long orgId) {
        Optional<List<People>> usersOpt = organizationService.listOrganizationUsers(orgId);
        if (usersOpt.isPresent()) {
            return ResponseEntity.ok(usersOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
