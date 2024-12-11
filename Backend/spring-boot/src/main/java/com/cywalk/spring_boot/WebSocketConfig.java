package com.cywalk.spring_boot;

import com.cywalk.spring_boot.Friends.FriendLocationController;
import com.cywalk.spring_boot.Locations.LocationSessionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Must autowire the controllers that will be used as web socket handlers.
     */
    private LocationSessionController locationSessionController;
    private FriendLocationController friendLocationController;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setFriendLocationController(FriendLocationController friendLocationController) {
        this.friendLocationController = friendLocationController;
    }

    @Autowired
    public void setLocationSessionController(LocationSessionController locationSessionController) {
        this.locationSessionController = locationSessionController;
    }

    /**
     * Whenever you want to add a web socket handler, you need to register it here.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(friendLocationController, "/locations/friends")
                .setAllowedOrigins("*");

        registry.addHandler(locationSessionController, "/locations/sessions")
                .setAllowedOrigins("*");

    }
}