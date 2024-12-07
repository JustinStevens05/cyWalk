package com.cywalk.spring_boot.Leaderboard;

import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Locations.LocationActivity;
import com.cywalk.spring_boot.Locations.LocationDay;
import com.cywalk.spring_boot.Locations.LocationUtils;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.Leaderboard.LeaderboardWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private PeopleRepository peopleRepository;

    private List<LeaderboardEntry> currentLeaderboard = new ArrayList<>();

    // Existing method for the global leaderboard
    public List<LeaderboardEntry> getLeaderboard() {
        List<People> users = peopleRepository.findAll();
        List<LeaderboardEntry> leaderboard = calculateLeaderboard(users);

        // Broadcast updates if the leaderboard has changed
        if (!leaderboard.equals(currentLeaderboard)) {
            currentLeaderboard = leaderboard;
            LeaderboardUpdate update = new LeaderboardUpdate(currentLeaderboard);
            LeaderboardWebSocket.broadcast(update);
        }

        return leaderboard;
    }

    // New method for organization-specific leaderboards
    public List<LeaderboardEntry> getLeaderboard(Set<People> users) {
        return calculateLeaderboard(users);
    }

    // Helper method to calculate the leaderboard
    private List<LeaderboardEntry> calculateLeaderboard(Collection<People> users) {
        Map<String, Integer> userStepsMap = new HashMap<>();

        for (People user : users) {
            double totalDistanceMeters = 0.0;

            List<LocationDay> locationDays = user.getLocations();
            if (locationDays != null) {
                for (LocationDay ld : locationDays) {
                    List<LocationActivity> activities = ld.getActivities();
                    if (activities != null) {
                        for (LocationActivity activity : activities) {
                            List<Location> locations = activity.getLocations();
                            if (locations != null && locations.size() > 1) {
                                for (int i = 1; i < locations.size(); i++) {
                                    Location loc1 = locations.get(i - 1);
                                    Location loc2 = locations.get(i);
                                    double distance = LocationUtils.calculateDistance(loc1, loc2);
                                    totalDistanceMeters += distance;
                                }
                            }
                        }
                    }
                }
            }

            // Convert totalDistance from meters to steps
            double stepsPerMeter = 2000.0 / 1609.34; // Steps per meter
            double totalStepsDouble = totalDistanceMeters * stepsPerMeter;
            int totalSteps = (int) Math.round(totalStepsDouble);

            userStepsMap.put(user.getUsername(), totalSteps);
        }

        List<LeaderboardEntry> leaderboard = userStepsMap.entrySet().stream()
                .map(entry -> new LeaderboardEntry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Sort the leaderboard by total steps in descending order
        leaderboard.sort(Comparator.comparingInt(LeaderboardEntry::getTotalSteps).reversed());

        // Assign ranks
        int rank = 1;
        for (LeaderboardEntry entry : leaderboard) {
            entry.setRank(rank++);
            entry.setLeaderboardId(0); // Adjust if needed
        }

        return leaderboard;
    }
}
