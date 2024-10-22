package com.cywalk.spring_boot.leaderboard;

import com.cywalk.spring_boot.steps.Steps;
import com.cywalk.spring_boot.steps.StepsRepository;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private StepsRepository stepsRepository;

    public List<LeaderboardEntry> getLeaderboard() {
        List<People> users = peopleRepository.findAll();

        Map<String, Integer> userStepsMap = new HashMap<>();

        for (People user : users) {
            List<Steps> stepsList = stepsRepository.findByUserUsername(user.getUsername());
            int totalSteps = stepsList.stream().mapToInt(Steps::getAmountOfSteps).sum();
            userStepsMap.put(user.getUsername(), totalSteps);
        }

        List<LeaderboardEntry> leaderboard = userStepsMap.entrySet().stream()
                .map(entry -> new LeaderboardEntry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        leaderboard.sort(Comparator.comparingInt(LeaderboardEntry::getTotalSteps).reversed());

        int rank = 1;
        for (LeaderboardEntry entry : leaderboard) {
            entry.setRank(rank++);

            entry.setLeaderboardId(1);
        }

        return leaderboard;
    }
}
