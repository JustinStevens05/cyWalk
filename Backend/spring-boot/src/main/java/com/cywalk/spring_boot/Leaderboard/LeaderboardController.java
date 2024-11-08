package com.cywalk.spring_boot.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboardService.getLeaderboard();
    }

}
