package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Locations.LocationSessionController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class FriendLocationConfig implements WebSocketConfigurer {

    private final FriendLocationController friendLocationController;

    public FriendLocationConfig(FriendLocationController friendLocationController) {
        this.friendLocationController = friendLocationController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(friendLocationController, "/locations/friends")
                .setAllowedOrigins("*"); // Set allowed origins as needed
    }
}
