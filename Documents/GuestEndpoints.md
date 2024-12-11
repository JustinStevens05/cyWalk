# Guest User Service API Documentation

## Base URL

```

http://localhost:8080/guest

```

## Guest User Overview

The Guest User functionality provides a way for users to interact with the Cywalk platform without needing to create a full account. Guest users have limited access to features and are primarily designed for demonstration or trial purposes.

### Signing Up as a Guest

To sign up as a guest, you only need to provide a unique username and a password.

**Endpoint:** `/signup/guest`
**Method:** `POST`

**Request Body:**

```json
{
  "username": "your_unique_username",
  "password": "your_password"
}
```

**Example Request:**

```json
{
  "username": "GuestUser123",
  "password": "guestPass"
}
```

**Response:**

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    {
      "username": "GuestUser123",
      "id": 12345,
      "type": "GUEST"
    }
    ```
    - `username`: The username of the created guest user.
    - `id`: A unique session key for the guest user. This key should be used for subsequent authenticated requests.
    - `type`: Indicates that the user is a "GUEST".

- **Failure:**
  - **Status:** `400 Bad Request`
  - **Body:**
    ```json
    {
      "message": "Username and password are required."
    }
    ```
    - Returned if the username or password is not provided.
  - **Status:** `409 Conflict`
  - **Body:**
    ```json
    {
      "message": "Username already in use."
    }
    ```
    - Returned if the provided username is already taken.

## Endpoints

### 1. Get All Organizations

Retrieve a list of all available organizations. This endpoint is accessible to guest users and does not require authentication.

- **Endpoint:** `/organizations`
- **Method:** `GET`
- **Description:** Fetches all organizations that exist in the system.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    [
      {
        "id": 1,
        "name": "Iowa State University",
        "users": [],
        "admins": []
      },
      {
        "id": 2,
        "name": "University of Iowa",
        "users": [],
        "admins": []
      }
    ]
    ```

---

### 2. Get Global Leaderboard

Retrieve the global leaderboard, showing the ranking of all users by their total steps. This endpoint is accessible to guest users.

- **Endpoint:** `/leaderboard`
- **Method:** `GET`
- **Description:** Fetches the global leaderboard.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    [
      {
        "username": "john_doe",
        "totalSteps": 15000,
        "rank": 1,
        "leaderboardId": 0
      },
      {
        "username": "jane_doe",
        "totalSteps": 12000,
        "rank": 2,
        "leaderboardId": 0
      }
    ]
    ```

---

### 3. Get Total Steps Walked by All Users

Retrieve the total amount of steps all users have taken combined.

- **Endpoint:** `/totalsteps`
- **Method:** `GET`
- **Description:** Sums the total steps from the global leaderboard.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    27000
    ```

---

### 4. Get Total Number of Users

Retrieve the total count of users in the system.

- **Endpoint:** `/users/count`
- **Method:** `GET`
- **Description:** Returns the total number of users.

#### Responses:

- **Success:**

  - **Status:** `200 OK`
  - **Body:**
    ```json
    150
    ```

---

## Usage Examples

### 1. Retrieving All Organizations

**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/guest/organizations`

**Response:**

- **Status:** `200 OK`
- **Body:**
  ```json
  [
    {
      "id": 1,
      "name": "Iowa State University",
      "users": [],
      "admins": []
    },
    {
      "id": 2,
      "name": "University of Iowa",
      "users": [],
      "admins": []
    }
  ]
  ```

---

### 2. Retrieving the Global Leaderboard

**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/guest/leaderboard`

**Response:**

- **Status:** `200 OK`
- **Body:**
  ```json
  [
    {
      "username": "john_doe",
      "totalSteps": 15000,
      "rank": 1,
      "leaderboardId": 0
    },
    {
      "username": "jane_doe",
      "totalSteps": 12000,
      "rank": 2,
      "leaderboardId": 0
    }
  ]
  ```

---

## Guest Class Documentation

### `Guest`

Represents a guest user in the system.

#### Fields:

- `displayName` (String): The display name for the guest user. Defaults to "Guest".

#### Constructors:

- `Guest()`: Creates a new guest user with the default display name "Guest".
- `Guest(String displayName)`: Creates a new guest user with the specified display name.

#### Methods:

- `getDisplayName()`: Returns the display name of the guest user.
- `setDisplayName(String displayName)`: Sets the display name of the guest user.

#### Example:

```java
Guest guest = new Guest();
System.out.println(guest.getDisplayName()); // Output: Guest

Guest customGuest = new Guest("Visitor123");
System.out.println(customGuest.getDisplayName()); // Output: Visitor123
```

```

```
