package com.cywalk.spring_boot;

import com.cywalk.spring_boot.Friends.FriendLocationController;
import com.cywalk.spring_boot.Locations.LocationSessionController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {


    private final LocationSessionController locationSessionController;
    private final FriendLocationController friendLocationController;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    public WebSocketConfig(FriendLocationController friendLocationController, LocationSessionController locationSessionController) {
        this.friendLocationController = friendLocationController;
        this.locationSessionController = locationSessionController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(friendLocationController, "/locations/friends")
                .setAllowedOrigins("*"); // Set allowed origins as needed

        registry.addHandler(locationSessionController, "/locations/sessions")
                .setAllowedOrigins("*"); // Set allowed origins as needed

    }
}
