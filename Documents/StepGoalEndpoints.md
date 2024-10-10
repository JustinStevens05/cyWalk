# Step Goals API Documentation

This document provides detailed information about the API endpoints for managing step goals in the Step Goals Spring Boot Application. It covers how to set, retrieve, and delete daily and weekly step goals for users.

## Table of Contents

- [Base URL](#base-url)
- [Endpoints](#endpoints)
    - [Set Step Goal](#set-step-goal)
    - [Get Step Goal](#get-step-goal)
    - [Delete Step Goal](#delete-step-goal)
- [Response Structure](#response-structure)
- [Error Handling](#error-handling)
- [Examples](#examples)

---

## Base URL

All endpoints are prefixed with the base URL:

```
http://localhost:8080/goals
```

---

## Endpoints

### Set Step Goal

**Endpoint**

```
POST /goals/{username}
```

**Description**

Sets or updates the daily and weekly step goals for a specific user.

**Path Parameters**

- `username` (String): The username of the user.

**Request Body**

```json
{
  "dailyGoal": 10000,
  "weeklyGoal": 70000
}
```

- `dailyGoal` (int): The number of steps set as the daily goal.
- `weeklyGoal` (int): The number of steps set as the weekly goal.

**Responses**

- **200 OK**

  **Description:** Step goals successfully set or updated.

  **Response Body:**

  ```json
  {
    "id": 1,
    "dailyGoal": 10000,
    "weeklyGoal": 70000,
    "people": {
      "username": "john_doe",
      "name": "John Doe"
      // other user fields
    }
  }
  ```

- **404 Not Found**

  **Description:** User with the specified username does not exist.

  **Response Body:** *Empty*



---

### Get Step Goal

**Endpoint**

```
GET /goals/{username}
```

**Description**

Retrieves the daily and weekly step goals for a specific user.

**Path Parameters**

- `username` (String): The username of the user.

**Responses**

- **200 OK**

  **Description:** Step goals retrieved successfully.

  **Response Body:**

  ```json
  {
    "id": 1,
    "dailyGoal": 10000,
    "weeklyGoal": 70000,
    "people": {
      "username": "john_doe",
      "name": "John Doe"
      // other user fields
    }
  }
  ```

- **404 Not Found**

  **Description:** No step goals found for the specified user.

  **Response Body:** *Empty*


---

### Delete Step Goal

**Endpoint**

```
DELETE /goals/{username}
```

**Description**

Deletes the daily and weekly step goals for a specific user.

**Path Parameters**

- `username` (String): The username of the user.

**Responses**

- **200 OK**

  **Description:** Step goals successfully deleted.

  **Response Body:** *Empty*

- **404 Not Found**

  **Description:** No step goals found for the specified user.

  **Response Body:** *Empty*



---

## Response Structure

All successful responses return JSON objects with the following structure:

```json
{
  "id": Long,
  "dailyGoal": int,
  "weeklyGoal": int,
  "people": {
    "username": String,
    "name": String
    // other user fields
  }
}
```

- `id`: Unique identifier for the step goal entry.
- `dailyGoal`: User's daily step goal.
- `weeklyGoal`: User's weekly step goal.
- `people`: Associated user information.

## Error Handling

- **404 Not Found:** Returned when the specified user does not exist or when there are no step goals associated with the user.
- **400 Bad Request:** Returned when the request body is malformed or missing required fields.
- **500 Internal Server Error:** Returned when an unexpected error occurs on the server.

## Examples

### Setting a Step Goal

```bash
curl -X POST http://localhost:8080/goals/jane_doe \
     -H "Content-Type: application/json" \
     -d '{"dailyGoal": 8000, "weeklyGoal": 56000}'
```

**Response:**

```json
{
  "id": 2,
  "dailyGoal": 8000,
  "weeklyGoal": 56000,
  "people": {
    "username": "jane_doe",
    "name": "Jane Doe"
    // other user fields
  }
}
```

### Retrieving a Step Goal

```bash
curl -X GET http://localhost:8080/goals/jane_doe
```

**Response:**

```json
{
  "id": 2,
  "dailyGoal": 8000,
  "weeklyGoal": 56000,
  "people": {
    "username": "jane_doe",
    "name": "Jane Doe"
    // other user fields
  }
}
```

### Deleting a Step Goal

```bash
curl -X DELETE http://localhost:8080/goals/jane_doe
```

**Response:**

*No content, HTTP status 200 OK*

---

*For further assistance or to report issues, please contact the development team.*