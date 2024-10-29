package com.cywalk.spring_boot.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LeaderboardWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public LeaderboardWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendLeaderboardUpdate(LeaderboardUpdate update) {
        messagingTemplate.convertAndSend("/topic/leaderboard", update);
    }
}
