# Leaderboard WebSocket Service Documentation

## Overview

The **Leaderboard WebSocket Service** enables real-time updates of leaderboards within the application. Clients can subscribe to leaderboard updates and receive live notifications whenever the leaderboard changes. This service supports both the **global leaderboard** and **organization-specific leaderboards**.

## WebSocket Endpoint

- **URL:** `ws://localhost:8080/ws-leaderboard`
- **Protocol:** STOMP over WebSocket
- **Fallback:** SockJS is enabled for browsers that do not support WebSocket.

## Subscription Topics

Clients can subscribe to the following topics to receive live leaderboard updates:

### 1. Global Leaderboard Updates

- **Destination:** `/topic/leaderboard/global`
- **Description:** Receives real-time updates for the global leaderboard, which ranks all users based on their total steps.

### 2. Organization-Specific Leaderboard Updates

- **Destination:** `/topic/leaderboard/organization/{orgId}`
- **Description:** Receives real-time updates for a specific organization's leaderboard, identified by `orgId`.
  
  - **Example:** For organization with ID `1`, subscribe to `/topic/leaderboard/organization/1`.

## Message Structure

### LeaderboardUpdate

All leaderboard updates are sent in the following JSON format:

```json
{
    "leaderboard": [
        {
            "username": "john_doe",
            "totalSteps": 15000,
            "rank": 1,
            "leaderboardId": 0
        },
        {
            "username": "jane_smith",
            "totalSteps": 12000,
            "rank": 2,
            "leaderboardId": 0
        },
        {
            "username": "alice_jones",
            "totalSteps": 9000,
            "rank": 3,
            "leaderboardId": 0
        }
    ]
}
```

- **Fields:**
  - `leaderboard` (Array of `LeaderboardEntry`): The updated leaderboard entries.

### LeaderboardEntry

Each entry within the `leaderboard` array has the following structure:

```json
{
    "username": "john_doe",
    "totalSteps": 15000,
    "rank": 1,
    "leaderboardId": 0
}
```

- **Fields:**
  - `username` (String): The username of the user.
  - `totalSteps` (Integer): The total number of steps taken by the user.
  - `rank` (Integer): The user's rank on the leaderboard.
  - `leaderboardId` (Integer): The ID of the leaderboard (`0` for global, `orgId` for organization-specific).

## How It Works

1. **Establish Connection:**
   - Clients connect to the WebSocket endpoint at `/ws-leaderboard` using SockJS and STOMP protocols.

2. **Subscribe to Topics:**
   - **Global Leaderboard:** Subscribe to `/topic/leaderboard/global` to receive updates for the global leaderboard.
   - **Organization Leaderboard:** Subscribe to `/topic/leaderboard/organization/{orgId}` to receive updates for a specific organization's leaderboard.

3. **Receive Updates:**
   - When the leaderboard (global or organization-specific) changes, the server broadcasts a `LeaderboardUpdate` message to the respective topic.
   - Subscribed clients receive the message and can update their UI accordingly.

## Example Usage

### Subscribing to Global Leaderboard

```javascript
stompClient.subscribe('/topic/leaderboard/global', function(message) {
    const update = JSON.parse(message.body);
    // Handle the global leaderboard update
    console.log('Global Leaderboard Update:', update);
});
```

### Subscribing to an Organization's Leaderboard

```javascript
const orgId = 1; // Replace with the desired organization ID
stompClient.subscribe(`/topic/leaderboard/organization/${orgId}`, function(message) {
    const update = JSON.parse(message.body);
    // Handle the organization's leaderboard update
    console.log(`Organization ${orgId} Leaderboard Update:`, update);
});
```


### WebSocketConfig.java

```java
package com.cywalk.spring_boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-leaderboard")
                .setAllowedOrigins("*") // Adjust origins for production
                .withSockJS(); // Fallback options for browsers that don't support WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Prefix for outgoing messages to clients
        config.setApplicationDestinationPrefixes("/app"); // Prefix for incoming messages from clients
    }
}
```

- **`registerStompEndpoints`:** Defines the `/ws-leaderboard` endpoint for WebSocket connections and enables SockJS as a fallback.
- **`configureMessageBroker`:** Sets up a simple in-memory message broker with `/topic` for outgoing messages and `/app` for incoming messages.

## Message Broadcasting in LeaderboardService

Ensure that the `LeaderboardService` broadcasts updates to the appropriate WebSocket topics when the leaderboard changes.



## Summary of Endpoints

### WebSocket Endpoint

- **Connect To:** `ws://localhost:8080/ws-leaderboard`

### Subscription Topics

1. **Global Leaderboard**
   - **Subscribe To:** `/topic/leaderboard/global`
   - **Receives:** `LeaderboardUpdate` messages for the global leaderboard.

2. **Organization-Specific Leaderboard**
   - **Subscribe To:** `/topic/leaderboard/organization/{orgId}`
   - **Receives:** `LeaderboardUpdate` messages for the specified organization.

## How to Use

1. **Establish Connection:**
   - Use SockJS and STOMP.js (or equivalent libraries) to connect to the WebSocket endpoint.

2. **Subscribe to Desired Topics:**
   - **Global Leaderboard:** Subscribe to `/topic/leaderboard/global` to receive updates.
   - **Organization Leaderboard:** Subscribe to `/topic/leaderboard/organization/{orgId}` by replacing `{orgId}` with the actual organization ID.

3. **Handle Incoming Messages:**
   - Listen for `LeaderboardUpdate` messages and update the UI accordingly.

## Example Workflow

1. **Client Connects:**
   - Establishes a WebSocket connection to `/ws-leaderboard`.

2. **Client Subscribes:**
   - Subscribes to `/topic/leaderboard/global` for global updates.
   - Subscribes to `/topic/leaderboard/organization/1` for organization ID `1` updates.

3. **Leaderboard Changes:**
   - When a user adds steps that affect the leaderboard, the server recalculates the leaderboard.
   - If the global or organization-specific leaderboard changes, a `LeaderboardUpdate` message is broadcasted to the respective topic.

4. **Client Receives Update:**
   - The subscribed client receives the `LeaderboardUpdate` message and updates the leaderboard display in real-time.

## Notes

- **Multiple Subscriptions:** Clients can subscribe to multiple topics simultaneously to receive various leaderboard updates.
- **Reconnection Logic:** Implement reconnection strategies to handle scenarios where the WebSocket connection is lost.
- **Security:** Ensure that only authorized clients can subscribe to sensitive topics, especially organization-specific leaderboards.
