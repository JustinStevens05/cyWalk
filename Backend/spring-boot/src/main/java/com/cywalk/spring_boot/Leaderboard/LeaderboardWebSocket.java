package com.cywalk.spring_boot.Leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/leaderboard")
@Component
public class LeaderboardWebSocket {

    private static Set<Session> sessions = new CopyOnWriteArraySet<>();
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static LeaderboardService leaderboardService;

    @Autowired
    public void setLeaderboardService(LeaderboardService service) {
        leaderboardService = service;
    }

    private final Logger logger = LoggerFactory.getLogger(LeaderboardWebSocket.class);

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("New connection established. Session ID: {}", session.getId());

        LeaderboardUpdate update = new LeaderboardUpdate(leaderboardService.getLeaderboard());
        try {
            String message = objectMapper.writeValueAsString(update);
            session.getAsyncRemote().sendText(message);
        } catch (IOException e) {
            logger.error("Error sending leaderboard data to Session ID {}: {}", session.getId(), e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        logger.info("Connection closed. Session ID: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Received message from Session ID {}: {}", session.getId(), message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("Error in Session ID {}: {}", session.getId(), throwable.getMessage());
    }

    public static void broadcast(LeaderboardUpdate update) {
        String message;
        try {
            message = objectMapper.writeValueAsString(update);
        } catch (IOException e) {
            System.err.println("Error serializing leaderboard update: " + e.getMessage());
            return;
        }

        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}