package com.cywalk.spring_boot.Websocket;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {

    private final Map<Long, Set<String>> onlineUsersByOrg = new ConcurrentHashMap<>();

    public void userLoggedIn(String username, Long orgId) {
        Set<String> users = onlineUsersByOrg
                .computeIfAbsent(orgId, k -> ConcurrentHashMap.newKeySet());
        users.add(username);

        // Broadcast update
        OrganizationOnlineUsersWebSocket.broadcastOnlineUsers(orgId, users);
    }

    public void userLoggedOut(String username, Long orgId) {
        Set<String> users = onlineUsersByOrg.get(orgId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                onlineUsersByOrg.remove(orgId);
            }

            // Broadcast update
            OrganizationOnlineUsersWebSocket.broadcastOnlineUsers(orgId, users);
        }
    }


    public Set<String> getOnlineUsers(Long orgId) {
        return onlineUsersByOrg.getOrDefault(orgId, Collections.emptySet());
    }
}
