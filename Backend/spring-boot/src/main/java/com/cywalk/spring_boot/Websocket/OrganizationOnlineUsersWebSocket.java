package com.cywalk.spring_boot.Websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@ServerEndpoint("/organizations/{orgId}/onlineUsers")
@Component
public class OrganizationOnlineUsersWebSocket {

    private static final Map<Long, Set<Session>> sessionsPerOrg = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session, @PathParam("orgId") Long orgId) {
        sessionsPerOrg
                .computeIfAbsent(orgId, k -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("orgId") Long orgId) {
        Set<Session> sessions = sessionsPerOrg.get(orgId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionsPerOrg.remove(orgId);
            }
        }
    }

    public static void broadcastOnlineUsers(Long orgId, Set<String> onlineUsers) {
        Set<Session> sessions = sessionsPerOrg.get(orgId);
        if (sessions != null && !sessions.isEmpty()) {
            try {
                String message = objectMapper.writeValueAsString(onlineUsers);
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        session.getAsyncRemote().sendText(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
