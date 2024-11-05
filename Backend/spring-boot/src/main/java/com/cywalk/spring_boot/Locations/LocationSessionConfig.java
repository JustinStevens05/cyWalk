package com.cywalk.spring_boot.Locations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class LocationSessionConfig implements WebSocketConfigurer {

    private final LocationSessionController locationSessionController;

    public LocationSessionConfig(LocationSessionController locationSessionController) {
        this.locationSessionController = locationSessionController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(locationSessionController, "/location/sessions")
                .setAllowedOrigins("*"); // Set allowed origins as needed
    }
}
