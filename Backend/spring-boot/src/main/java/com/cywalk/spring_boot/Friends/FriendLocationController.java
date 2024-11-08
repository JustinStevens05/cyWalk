package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Locations.LocationService;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.Users.PeopleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
// @ServerEndpoint("/locations/friends")
public class FriendLocationController extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(FriendLocationController.class);
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
                /*
                List<People> friendsListPeople = friendService.getFriends(peopleResult.get());
                ArrayList<String> friendsListNames = new ArrayList<>();
                for (People friendsListPerson : friendsListPeople) {
                    friendsListNames.add(friendsListPerson.getUsername());
                }
                sessionToFriends.put(session, friendsListNames);
                 */
                userToSession.put(peopleResult.get().getUsername(), session);
                session.sendMessage(new TextMessage("Connected to user: " + peopleResult.get().getUsername()));
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
        userToSession.remove(username);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("WebSocket error: {}", exception.getMessage());
    }

    /**
     * Sends a location from a friend with a timestamp
     * @param location the location of the friend
     * @param user the user who owns the location
     * @param friend the friend of said user
     */
    public void sendLocation(Location location, People user, People friend) {
        sendLocation(location, user.getUsername(), friend.getUsername());
    }

    /**
     * Sends a location from a friend with a timestamp
     * @param location the location of the friend
     * @param username the user who owns the location's username
     * @param friendUsername the friend of the user's username
     */
    public void sendLocation(Location location, String username, String friendUsername) {
        WebSocketSession session = userToSession.get(friendUsername);
        if (session != null) {
            // craft the location to send
            FriendLocation fl;
            if (location.getTime() == null) {
                fl = new FriendLocation(username, location.getLatitude(), location.getLongitude());
            }
            else {
                fl = new FriendLocation(username, location.getLatitude(), location.getLongitude(), location.getTime());
            }
            try {
                sendFriendLocation(fl, session); // HIT
            }
            catch (IOException e) {
                logger.error("encountered an io exception");
                logger.error(e.getMessage());
            }
        }
        else {
            logger.info("no session found for user {}", friendUsername);
        }
    }

    /**
     * sends a friend location over the session
     * @param fl the friend location to send
     * @param session the session to broadcast the data to
     */
    public void sendFriendLocation(FriendLocation fl, WebSocketSession session) throws IOException {
        TextMessage tm = new TextMessage(
                fl.toString()
        );
        session.sendMessage(new TextMessage("oh yea bud"));
        // session.sendMessage(tm);
    }
}
