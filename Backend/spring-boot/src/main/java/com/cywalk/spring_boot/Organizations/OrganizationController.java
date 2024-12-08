package com.cywalk.spring_boot.Organizations;
import java.util.HashMap;
import java.util.Map;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Leaderboard.LeaderboardEntry;
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
}
