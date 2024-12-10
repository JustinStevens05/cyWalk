package com.cywalk.spring_boot.Organizations;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.Leaderboard.LeaderboardEntry;
import com.cywalk.spring_boot.Leaderboard.LeaderboardService;
import org.aspectj.weaver.ast.Or;
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

    public Organization createOrganization(String name) {
        Optional<Organization> orgOpt = organizationRepository.findByName(name);
        if (orgOpt.isPresent()) {
            return orgOpt.get();
        }
        Organization organization = new Organization(name);
        return organizationRepository.save(organization);
    }

    public void saveOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    public boolean joinOrganization(Long orgId, String username) {
        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        Optional<People> userOpt = peopleRepository.findByUsername(username);

        if (orgOpt.isPresent() && userOpt.isPresent()) {
            Organization organization = orgOpt.get();
            People user = userOpt.get();

            if (user.getOrganization() != null) {
                return false;
            }

            organization.addUser(user);
            organizationRepository.save(organization);
            peopleRepository.save(user);
            return true;
        }
        return false;
    }

    public Optional<List<LeaderboardEntry>> getOrganizationLeaderboard(Long orgId) {
        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        if (orgOpt.isPresent()) {
            Organization organization = orgOpt.get();
            Set<People> users = organization.getUsers();
            List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard(users);
            return Optional.of(leaderboard);
        }
        return Optional.empty();
    }

    public Optional<List<People>> listOrganizationUsers(Long orgId) {
        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        if (orgOpt.isPresent()) {
            List<People> users = new ArrayList<>(orgOpt.get().getUsers());
            return Optional.of(users);
        }
        return Optional.empty();
    }

    public Optional<Long> getOrganizationIdByName(String name) {
        Optional<Organization> orgOpt = organizationRepository.findByName(name);
        if (orgOpt.isPresent()) {
            return Optional.of(orgOpt.get().getId());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Organization> getOrganizationByUsername(String username) {
        Optional<People> userOpt = peopleRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            People user = userOpt.get();
            Organization org = user.getOrganization();
            if (org != null) {
                return Optional.of(org);
            }
        }
        return Optional.empty();
    }

    public List<Organization> listAllOrganizations() {
        return organizationRepository.findAll();
    }

    public boolean organizationExists(String name) {
        return organizationRepository.findByName(name).isPresent();
    }

}