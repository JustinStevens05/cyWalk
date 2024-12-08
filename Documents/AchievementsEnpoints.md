# Achievements Service API Documentation

## Base URL

```
http://localhost:8080
```

## Endpoints

### 1. Get All Achievements

Retrieve a list of all available achievements.

- **Endpoint:** `/achievements`
- **Method:** `GET`
- **Description:** Fetches all achievements that exist in the system.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    [
      {
        "id": 1,
        "name": "5 Miles",
        "description": "You traveled a total of 5 miles!",
        "criteria": {
          "type": "distance",
          "value": 5
        },
        "imageUrl": "http://example.com/5miles.png",
        "users": [
          {
            "username": "john_doe",
            "email": "john@example.com",
            "locations": []
          }
        ]
      },
      {
        "id": 2,
        "name": "10,000 Steps",
        "description": "You completed 10,000 steps!",
        "criteria": {
          "type": "steps",
          "value": 10000
        },
        "imageUrl": "http://example.com/10000steps.png",
        "users": []
      }
    ]
    ```

- **Failure:**
  - **Status:** `500 Internal Server Error`
  - **Description:** If there's an issue retrieving achievements from the database.

---

### 2. Get Achievements of a User by Session Key

Retrieve all achievements earned by a specific user based on their session key.

- **Endpoint:** `/achievements/user/{key}`
- **Method:** `GET`
- **Description:** Fetches the list of achievements that the user associated with the provided session key has earned.

#### Path Parameters:

- `key` (Long, required): The session key of the user.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    [
      {
        "id": 1,
        "name": "5 Miles",
        "description": "You traveled a total of 5 miles!",
        "criteria": {
          "type": "distance",
          "value": 5
        },
        "imageUrl": "http://example.com/5miles.png",
        "users": [
          {
            "username": "john_doe",
            "email": "john@example.com",
            "locations": []
          }
        ]
      },
      {
        "id": 2,
        "name": "10,000 Steps",
        "description": "You completed 10,000 steps!",
        "criteria": {
          "type": "steps",
          "value": 10000
        },
        "imageUrl": "http://example.com/10000steps.png",
        "users": []
      }
    ]
    ```

- **Failure:**
  - **Status:** `404 Not Found`
  - **Description:** If no user is associated with the provided key or if the user has not earned any achievements.

---

## Usage Examples

### 1. Retrieving All Achievements

**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/achievements`

**Response:**

- **Status:** `200 OK`
- **Body:**
  ```json
  [
    {
      "id": 1,
      "name": "5 Miles",
      "description": "You traveled a total of 5 miles!",
      "criteria": {
        "type": "distance",
        "value": 5
      },
      "imageUrl": "http://example.com/5miles.png",
      "users": [
        {
          "username": "john_doe",
          "email": "john@example.com",
          "locations": []
        }
      ]
    },
    {
      "id": 2,
      "name": "10,000 Steps",
      "description": "You completed 10,000 steps!",
      "criteria": {
        "type": "steps",
        "value": 10000
      },
      "imageUrl": "http://example.com/10000steps.png",
      "users": []
    }
  ]
  ```

---

### 2. Retrieving Achievements for a User

**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/achievements/user/1234567890`

**Response:**

- **Status:** `200 OK`
- **Body:**

  ```json
  [
    {
      "id": 1,
      "name": "5 Miles",
      "description": "You traveled a total of 5 miles!",
      "criteria": {
        "type": "distance",
        "value": 5
      },
      "imageUrl": "http://example.com/5miles.png",
      "users": [
        {
          "username": "john_doe",
          "email": "john@example.com",
          "locations": []
        }
      ]
    },
    {
      "id": 2,
      "name": "10,000 Steps",
      "description": "You completed 10,000 steps!",
      "criteria": {
        "type": "steps",
        "value": 10000
      },
      "imageUrl": "http://example.com/10000steps.png",
      "users": []
    }
  ]
  ```

- **Failure Response Example:**

**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/achievements/user/9999999999`

**Response:**

- **Status:** `404 Not Found`
- **Description:** If the user does not exist or has not earned any achievements.

```json
{
  "error": "User not found or has not earned any achievements."
}
```
