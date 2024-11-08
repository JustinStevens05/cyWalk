# Organization WebSocket Guide

This README provides information on how to use the Organization WebSocket feature in your application. The Organization WebSocket allows clients to receive real-time updates about which users are currently online within an organization. Whenever a user logs in or logs out, all connected clients receive an updated list of online usernames.

## Table of Contents

- [Overview](#overview)
- [WebSocket Endpoint](#websocket-endpoint)
- [Data Format](#data-format)
- [Setup and Configuration](#setup-and-configuration)
- [Usage](#usage)
    - [Connecting to the WebSocket](#connecting-to-the-websocket)
    - [Handling Messages](#handling-messages)
- [Example Implementation](#example-implementation)
    - [Client-Side JavaScript Example](#client-side-javascript-example)
- [API Endpoints](#api-endpoints)
    - [User Login](#user-login)
    - [User Logout](#user-logout)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The Organization WebSocket is designed to provide real-time communication between the server and clients regarding which users are online within an organization. It ensures that all clients connected to the WebSocket receive immediate updates when any user logs in or out, enhancing the collaborative experience.

## WebSocket Endpoint

Clients can connect to the WebSocket endpoint using the following URI pattern:

```
ws://<server-address>/organizations/{orgId}/onlineUsers
```

- **`<server-address>`**: The base URL of your server (e.g., `localhost:8080` or `example.com`).
- **`{orgId}`**: The unique identifier of the organization.

**Example:**

```plaintext
ws://localhost:8080/organizations/1/onlineUsers
```

## Data Format

The server sends messages in JSON format. Each message is an array of usernames representing the users currently online in the organization.

**Example Message:**

```json
["Ethan", "Alice", "Bob"]
```

## API Endpoints

The WebSocket functionality relies on users logging in and out via the API endpoints. Below are the relevant endpoints:

### User Login

- **Endpoint**: `PUT /users`
- **Description**: Logs in a user and returns a session key.
- **Request Body**:

  ```json
  {
    "username": "charlie",
    "password": "password123"
  }
  ```

- **Response**:

  ```json
  {
    "key": 3
  }
  ```

### User Logout

- **Endpoint**: `DELETE /users/{key}`
- **Description**: Logs out a user using their session key.
- **Parameters**:
    - **`{key}`**: The session key obtained during login.
- **Example**:

  ```bash
  curl -X DELETE http://localhost:8080/users/3
  ```

## Testing

To test the Organization WebSocket:

1. **Start the Server**: Ensure your Spring Boot application is running.

2. **Connect a Client**: Use a WebSocket client to connect to the endpoint. You can use web browsers, specialized tools like [WebSocket King Client](https://chrome.google.com/webstore/detail/websocket-king-client/cbcbkhdmedgianpaifchdaddpnmgnknn), or implement a simple HTML page as shown above.

3. **Login and Logout Users**:

    - Use the API endpoints to log in and out users.
    - Observe the messages received by the WebSocket client.
    - Verify that the online user list updates correctly.

4. **Simulate Multiple Clients**: Open multiple clients to simulate different users and ensure all receive updates simultaneously.

