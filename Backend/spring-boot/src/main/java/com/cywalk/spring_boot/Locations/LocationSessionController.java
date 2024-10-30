package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@ServerEndpoint("/location/sessions/{key}")
public class LocationSessionController extends TextWebSocketHandler {

    public static Location asLocationFromString(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            JsonNode ms = mapper.readTree(json);
            return new Location(ms.get("latitude").asLong(), ms.get("longitude").asLong(), ms.get("elevation").asLong(), null); //TOD: add matching constructor
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    private static LocationService locationService;

    private static PeopleService peopleService;

    private static final Logger logger = LoggerFactory.getLogger(LocationSessionController.class);

    @Autowired
    public void setPeopleService(PeopleService ps) {
        peopleService = ps;
    }

    @Autowired
    public void setLocationService(LocationService ls) {
        locationService = ls;
    }

    // session and username
    private final Map<Session, String> authenticatedPerson = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("key") long key) throws IOException {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        if (peopleResult.isEmpty()) {
            logger.error("Could not open session with key: {}", key);
        }
        else {
            authenticatedPerson.put(session, peopleResult.get().getUsername());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // message should be a Location
        String username = authenticatedPerson.get(session);
        if (username == null) {
            logger.error("On message was called despite the fact that no user matches the current session");
        }
        else {
            Optional<People> personResult = peopleService.getUserByUsername(username);
            if (personResult.isEmpty()) {
                logger.error("the username is in the map, however not in the database??");
            }
            else {
                // try message into Location
                Location location = asLocationFromString(message);
                if (location != null) {
                    locationService.appendLocation(personResult.get(), location);
                }
            }
        }
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        authenticatedPerson.remove(session);

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error(throwable.toString());
    }

}
