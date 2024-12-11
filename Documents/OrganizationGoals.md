# Organization Goal API Documentation

## Base URL

```
http://localhost:8080/goals
```

## Organization Goal Overview

The organization goal functionality provides a way to set and manage step goals for individual users. These goals are separate from the organization wide goals.

### 1. Set Step Goal

Create or update a step goal for a specific user.

**Endpoint:** `/goals/{username}`
**Method:** `POST`

**Request Body:**

```json
{
  "dailyGoal": 10000,
  "weeklyGoal": 70000
}
```

- `dailyGoal`: The desired daily step goal for the user.
- `weeklyGoal`: The desired weekly step goal for the user.

**Example Request:**

```json
{
  "dailyGoal": 10000,
  "weeklyGoal": 70000
}
```

#### Responses:

- **Success:**
  - **Status:** `200 OK`
  - **Body:**
    ```json
    {
      "id": 1,
      "dailyGoal": 10000,
      "weeklyGoal": 70000,
      "people": {
        "username": "test_user",
        "organization": null,
        "userType": "USER",
        "email": "placeholder@gmail.com",
        "sentRequests": [],
        "receivedRequests": [],
        "achievements": [],
        "locations": [],
        "image": null,
        "league": null,
        "bio": null
      }
    }
    ```
    - `id`: The unique identifier for this specific step goal.
    - `dailyGoal`: The daily step goal for the user.
    - `weeklyGoal`: The weekly step goal for the user.
    - `people`: User object containing the username of this step goal
- **Failure:**
  - **Status:** `404 Not Found`
  - **Body:**
    - Returned if user is not found.

---

### 2. Get Step Goal

Retrieve the current step goal of a user.

- **Endpoint:** `/goals/{username}`
- **Method:** `GET`
- **Description:** Fetches the step goal for the given username.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    {
      "id": 1,
      "dailyGoal": 10000,
      "weeklyGoal": 70000,
      "people": {
        "username": "test_user",
        "organization": null,
        "userType": "USER",
        "email": "placeholder@gmail.com",
        "sentRequests": [],
        "receivedRequests": [],
        "achievements": [],
        "locations": [],
        "image": null,
        "league": null,
        "bio": null
      }
    }
    ```
        - `id`: The unique identifier for this specific step goal.
        - `dailyGoal`: The daily step goal for the user.
        - `weeklyGoal`: The weekly step goal for the user.
        - `people`: User object containing the username of this step goal

- **Failure:**
  - **Status:** `404 Not Found`
  - **Body:**
    - Returned if no step goal is associated with that user.

---

### 3. Delete Step Goal

Delete the step goal for a specific user.

- **Endpoint:** `/goals/{username}`
- **Method:** `DELETE`
- **Description:** Deletes the current step goal for the given username.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    - Empty body to indicate success.

- **Failure:**
  - **Status:** `404 Not Found`
  - **Body:**
    - Returned if no step goal is associated with the user.

---

## Usage Examples

### 1. Setting a Step Goal for a User

**Request:**

- **Method:** `POST`
- **URL:** `http://localhost:8080/goals/test_user`
- **Body:**

```json
{
  "dailyGoal": 10000,
  "weeklyGoal": 70000
}
```

**Response:**

- **Status:** `200 OK`
- **Body:**
  ```json
  {
    "id": 1,
    "dailyGoal": 10000,
    "weeklyGoal": 70000,
    "people": {
      "username": "test_user",
      "organization": null,
      "userType": "USER",
      "email": "placeholder@gmail.com",
      "sentRequests": [],
      "receivedRequests": [],
      "achievements": [],
      "locations": [],
      "image": null,
      "league": null,
      "bio": null
    }
  }
  ```

---

### 2. Getting a Step Goal for a User

**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/goals/test_user`

**Response:**

- **Status:** `200 OK`
- **Body:**
  ```json
  {
    "id": 1,
    "dailyGoal": 10000,
    "weeklyGoal": 70000,
    "people": {
      "username": "test_user",
      "organization": null,
      "userType": "USER",
      "email": "placeholder@gmail.com",
      "sentRequests": [],
      "receivedRequests": [],
      "achievements": [],
      "locations": [],
      "image": null,
      "league": null,
      "bio": null
    }
  }
  ```

---

### 3. Deleting a Step Goal for a User

**Request:**

- **Method:** `DELETE`
- **URL:** `http://localhost:8080/goals/test_user`

**Response:**

- **Status:** `200 OK`
- **Body:**
  - Empty.

---
