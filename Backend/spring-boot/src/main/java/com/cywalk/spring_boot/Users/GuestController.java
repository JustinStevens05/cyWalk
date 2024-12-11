package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Leaderboard.LeaderboardEntry;
import com.cywalk.spring_boot.Leaderboard.LeaderboardService;
import com.cywalk.spring_boot.Organizations.Organization;
import com.cywalk.spring_boot.Organizations.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private PeopleService peopleService;

    /**
     * Get all organizations available.
     */
    @Operation(summary = "Get all organizations")
    @ApiResponse(responseCode = "200", description = "List of organizations")
    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.listAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    /**
     * Get the global leaderboard.
     * Shows a ranking of all users by their total steps.
     */
    @Operation(summary = "Get global leaderboard")
    @ApiResponse(responseCode = "200", description = "Global leaderboard list")
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getGlobalLeaderboard() {
        List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get the total amount of steps all users have taken combined.
     * This sums the total steps from the global leaderboard.
     */
    @Operation(summary = "Get total steps walked by all users combined")
    @ApiResponse(responseCode = "200", description = "Returns total steps")
    @GetMapping("/totalsteps")
    public ResponseEntity<Long> getTotalStepsAllUsers() {
        List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard();
        long totalSteps = leaderboard.stream().mapToLong(LeaderboardEntry::getTotalSteps).sum();
        return ResponseEntity.ok(totalSteps);
    }

    /**
     * Get the amount of users the app has now.
     * This returns the total count of people in the database.
     */
    @Operation(summary = "Get total number of users")
    @ApiResponse(responseCode = "200", description = "Returns total number of users")
    @GetMapping("/users/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        long count = peopleService.getAllPeople().size();
        return ResponseEntity.ok(count);
    }
}
