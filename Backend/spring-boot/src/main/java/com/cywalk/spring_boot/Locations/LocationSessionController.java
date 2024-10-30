package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@ServerEndpoint("/location/sessions/{key}")
public class LocationSessionController {

    public static long asLocationFromString(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return new Location(mapper.readTree(json).get("latitude").asLong(), ); //TOD: add matching constructor
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            // try message into Location

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
