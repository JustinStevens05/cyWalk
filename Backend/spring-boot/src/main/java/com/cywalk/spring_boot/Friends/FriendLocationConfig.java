package com.cywalk.spring_boot.Friends;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class FriendLocationConfig implements WebSocketConfigurer {

    private final LocationSessionController locationSessionController;

    public FriendLocationConfig(LocationSessionController locationSessionController) {
        this.locationSessionController = locationSessionController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(locationSessionController, "/locations/sessions")
                .setAllowedOrigins("*"); // Set allowed origins as needed
    }
}
