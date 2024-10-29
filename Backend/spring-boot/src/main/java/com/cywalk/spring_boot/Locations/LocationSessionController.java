package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Controller
@ServerEndpoint("/location/sessions/{key}")
public class LocationSessionController {

    private static LocationService locationService;

    private static PeopleService peopleService;

    @Autowired
    public void setPeopleService(PeopleService ps) {
        peopleService = ps;
    }

    @Autowired
    public void setLocationService(LocationService ls) {
        locationService = ls;
    }

    private Map<Session, People> authenticatedPerson = null;

    @OnOpen
    public void onOpen(Session session, @PathParam("key") long key) throws IOException {
        authenticatedPerson.put(session, peopleService.getUserFromKey(key).get());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // message should be a Location

    }


    @OnClose
    public void onClose(Session session) throws IOException {
        authenticatedPerson.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

}
