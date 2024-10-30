package com.cywalk.spring_boot.Locations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

// made following guide: https://www.baeldung.com/websockets-spring
// had to consult other guide as well, which concurs: https://medium.com/simform-engineering/websocket-spring-boot-build-a-real-time-bidirectional-applications-e8c95bc19cbf

@Configuration
/*@EnableWebSocketMessageBroker
public class LocationSessionConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //TODO: change endpoints
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/log");
        registry.addEndpoint("/log").withSockJS();
    }

}
*/
 public class LocationSessionConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}