package com.cywalk.spring_boot.Achievements;

import com.cywalk.spring_boot.Achievements.Achievement;
import com.cywalk.spring_boot.Achievements.AchievementRepository;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.Locations.LocationDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    private static final double FIVE_MILES = 5 * 1609.34;
    private static final double TEN_MILES = 10 * 1609.34;
    private static final double TWENTY_MILES = 20 * 1609.34;

    public void initAchievements() {
        createIfNotExists("5 Miles", "You traveled a total of at least 5 miles!", "https://png.pngtree.com/png-clipart/20230811/original/pngtree-ranking-medal-icon-illustration-5th-place-award-mark-achievement-vector-picture-image_10347103.png");
        createIfNotExists("10 Miles", "You traveled a total of at least 10 miles!", "https://dgalywyr863hv.cloudfront.net/challenges/3101/3101-logo.png");
        createIfNotExists("20 Miles", "You traveled a total of at least 20 miles!", "https://d1nhzlodq7rhe4.cloudfront.net/5k3twdgjmoowccg4.png");
        createIfNotExists("5 Miles a Day for a Week", "You traveled at least 5 miles every day for 7 days straight!", "https://png.pngtree.com/png-clipart/20230823/original/pngtree-award-prize-batch-achievement-badge-picture-image_8233474.png");
    }

    private void createIfNotExists(String name, String description, String imageUrl) {
        if (achievementRepository.findByName(name).isEmpty()) {
            Achievement a = new Achievement(name, description, imageUrl);
            achievementRepository.save(a);
        }
    }

    public void checkAndAwardAchievements(People user) {
        initAchievements();

        Set<Achievement> userAchievements = user.getAchievements();

        double totalDistance = getTotalDistanceOfUser(user);

        if (totalDistance >= FIVE_MILES) {
            awardAchievement(user, "5 Miles");
        }

        if (totalDistance >= TEN_MILES) {
            awardAchievement(user, "10 Miles");
        }

        if (totalDistance >= TWENTY_MILES) {
            awardAchievement(user, "20 Miles");
        }

        if (hasReachedFiveMilesEveryDayForWeek(user)) {
            awardAchievement(user, "5 Miles a Day for a Week");
        }

        peopleRepository.save(user);
    }

    private boolean hasReachedFiveMilesEveryDayForWeek(People user) {

        List<LocationDay> days = user.getLocations();

        LocalDate today = LocalDate.now();
        List<LocationDay> last7Days = days.stream()
                .filter(d -> !d.getDate().isAfter(today) && !d.getDate().isBefore(today.minusDays(6)))
                .collect(Collectors.toList());

        if (last7Days.size() < 7) {
            return false;
        }

        for (LocationDay ld : last7Days) {
            if (ld.getTotalDistance() < FIVE_MILES) {
                return false;
            }
        }
        return true;
    }

    private double getTotalDistanceOfUser(People user) {

        double total = 0;
        for (LocationDay ld : user.getLocations()) {
            total += ld.getTotalDistance();
        }
        return total;
    }

    private void awardAchievement(People user, String achievementName) {
        Optional<Achievement> achOpt = achievementRepository.findByName(achievementName);
        if (achOpt.isPresent()) {
            Achievement achievement = achOpt.get();
            if (!user.getAchievements().contains(achievement)) {
                user.addAchievement(achievement);
            }
        }
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Set<Achievement> getAchievementsOfUser(People user) {
        return user.getAchievements();
    }
}