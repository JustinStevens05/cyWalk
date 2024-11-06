package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Locations.LocationService;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FriendLocationController extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(FriendLocationController.class);
    private static LocationService locationService;
    private static PeopleService peopleService;
    private static FriendService friendService;

    private final Map<WebSocketSession, String> authenticatedPerson = new HashMap<>();
    private final Map<WebSocketSession, List<String>> sessionToFriends = new HashMap<>();
    private final Map<String, WebSocketSession> userToSession = new HashMap<>();

    @Autowired
    public void setPeopleService(PeopleService ps) {
        peopleService = ps;
    }

    @Autowired
    public void setLocationService(LocationService ls) {
        locationService = ls;
    }

    @Autowired
    public void setFriendService(FriendService fs) {
        friendService = fs;
    }

    public static Location asLocationFromString(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            JsonNode ms = mapper.readTree(json);
            return new Location(ms.get("latitude").asLong(), ms.get("longitude").asLong(), ms.get("elevation").asLong(), null);
        } catch (IOException e) {
            logger.error("Failed to parse location JSON: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            // Extract key from the query parameter
            String query = session.getUri().getQuery();
            Map<String, String> params = Arrays.stream(query.split("&"))
                    .map(param -> param.split("="))
                    .collect(Collectors.toMap(
                            p -> p[0],
                            p -> p[1]
                    ));
            String key = params.get("key");
            long keyAsLong = Long.parseLong(key);

            // Use the key as needed
            Optional<People> peopleResult = peopleService.getUserFromKey(keyAsLong);
            if (peopleResult.isEmpty()) {
                logger.error("Could not open session with key: {}", key);
            } else {
                authenticatedPerson.put(session, peopleResult.get().getUsername());
                List<People> friendsListPeople = friendService.getFriends(peopleResult.get());
                ArrayList<String> friendsListNames = new ArrayList<>();
                for (People friendsListPerson : friendsListPeople) {
                    friendsListNames.add(friendsListPerson.getUsername());
                }
                sessionToFriends.put(session, friendsListNames);
            }
        } catch (Exception e) {
            logger.error("Error parsing key from URL: {}", e.getMessage());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {}

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = authenticatedPerson.remove(session);
        // total up the final distance
        locationService.endSession(username);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("WebSocket error: {}", exception.getMessage());
    }
}
