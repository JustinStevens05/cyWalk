package com.example.demo.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/steps/{username}")
@Component
public class StepTrackingServer {

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private static Map<String, Integer> userStepCounts = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(StepTrackingServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        logger.info("[onOpen] " + username);

        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else {

            sessionUsernameMap.put(session, username);

            usernameSessionMap.put(username, session);

            userStepCounts.put(username, 0);

            sendMessageToUser(username, "Welcome to the Step Tracking Server, " + username);

            broadcast("User: " + username + " has joined the Step Tracking App");
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        String username = sessionUsernameMap.get(session);

        logger.info("[onMessage] " + username + ": " + message);

        if (message.startsWith("steps:")) {
            try {

                int steps = Integer.parseInt(message.substring(6));
                updateStepCount(username, steps);
            } catch (NumberFormatException e) {
                sendMessageToUser(username, "Invalid step count format. Use 'steps:<number>'.");
                logger.info("[Invalid Step Count] " + e.getMessage());
            }
        } else {
            sendMessageToUser(username, "Unknown command. To update steps, use 'steps:<number>'.");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        String username = sessionUsernameMap.get(session);

        logger.info("[onClose] " + username);

        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        userStepCounts.remove(username);

        broadcast("User: " + username + " has left the Step Tracking App");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

        String username = sessionUsernameMap.get(session);

        logger.error("[onError] " + username + ": " + throwable.getMessage());
    }

    private void updateStepCount(String username, int steps) {
        int currentSteps = userStepCounts.getOrDefault(username, 0);
        int updatedSteps = currentSteps + steps;
        userStepCounts.put(username, updatedSteps);
        broadcast("User: " + username + " has updated their steps to " + updatedSteps);
    }

    private void sendMessageToUser(String username, String message) {
        try {
            Session session = usernameSessionMap.get(username);
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error("[Send Message Exception] " + e.getMessage());
        }
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                logger.error("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}