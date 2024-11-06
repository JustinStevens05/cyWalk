package com.cywalk.spring_boot.organizations;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.leaderboard.LeaderboardEntry;
import com.cywalk.spring_boot.leaderboard.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    /**
     * Creates a new organization with the given name.
     *
     * @param name the name of the organization
     * @return the created Organization object, or Optional.empty() if name already exists
     */
    public Optional<Organization> createOrganization(String name) {
        if (organizationRepository.findByName(name).isPresent()) {
            return Optional.empty(); // Organization name already exists
        }
        Organization organization = new Organization(name);
        return Optional.of(organizationRepository.save(organization));
    }

    /**
     * Allows a user to join an existing organization.
     *
     * @param orgId    the ID of the organization
     * @param username the username of the user
     * @return true if successfully joined, false otherwise
     */
    public boolean joinOrganization(Long orgId, String username) {
        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        Optional<People> userOpt = peopleRepository.findByUsername(username);

        if (orgOpt.isPresent() && userOpt.isPresent()) {
            Organization organization = orgOpt.get();
            People user = userOpt.get();

            if (organization.getUsers().contains(user)) {
                return false; // User already a member
            }

            organization.addUser(user);
            organizationRepository.save(organization);
            return true;
        }
        return false; // Organization or user not found
    }

    /**
     * Retrieves the leaderboard for a specific organization.
     *
     * @param orgId the ID of the organization
     * @return a list of LeaderboardEntry objects, or Optional.empty() if organization not found
     */
    public Optional<List<LeaderboardEntry>> getOrganizationLeaderboard(Long orgId) {
        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        if (orgOpt.isPresent()) {
            // Use orgId as leaderboardId for organization-specific leaderboards
            List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard(orgId.intValue());
            return Optional.of(leaderboard);
        }
        return Optional.empty();
    }

    /**
     * Lists all users in a specific organization.
     *
     * @param orgId the ID of the organization
     * @return a list of People objects, or Optional.empty() if organization not found
     */
    public Optional<List<People>> listOrganizationUsers(Long orgId) {
        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        if (orgOpt.isPresent()) {
            List<People> users = new ArrayList<>(orgOpt.get().getUsers());
            return Optional.of(users);
        }
        return Optional.empty();
    }
}
