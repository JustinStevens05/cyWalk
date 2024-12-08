package com.cywalk.spring_boot.Achievements;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private PeopleService peopleService;

    @Operation(summary = "Get all achievements")
    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievements);
    }

    @Operation(summary = "Get achievements of a user by session key")
    @GetMapping("/user/{key}")
    public ResponseEntity<Set<Achievement>> getAchievementsOfUser(@PathVariable Long key) {
        Optional<People> userOpt = peopleService.getUserFromKey(key);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Set<Achievement> userAchievements = achievementService.getAchievementsOfUser(userOpt.get());
        return ResponseEntity.ok(userAchievements);
    }
}
