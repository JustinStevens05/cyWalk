# Leaderboard Service API Documentation

## Overview

The **Leaderboard Service API** provides endpoints to access and manage leaderboard data based on users' total steps. Currently, the service supports a **global leaderboard** identified by the default `leaderboardId` of `0`. This global leaderboard ranks all users in the system based on their accumulated steps.

## Base URL

```
http://localhost:8080
```

## Endpoints

### 1. Get Global Leaderboard

Retrieve the global leaderboard, which ranks all users based on their total steps.

- **Endpoint:** `/leaderboard`
- **Method:** `GET`
- **Description:** Fetches the global leaderboard entries, sorted in descending order of total steps. Each entry includes the username, total steps, rank, and leaderboard ID.
- **Parameters:** None (defaults to `leaderboardId = 0` for the global leaderboard).
- **Response:**
    - **Status:** `200 OK`
    - **Body:** An array of `LeaderboardEntry` objects.

#### `LeaderboardEntry` Object

| Field           | Type    | Description                      |
|-----------------|---------|----------------------------------|
| `username`      | String  | The username of the user.        |
| `totalSteps`    | Integer | The total number of steps taken. |
| `rank`          | Integer | The user's rank on the leaderboard. |
| `leaderboardId` | Integer | The ID of the leaderboard (default is `0`). |

#### Example Response

```json
[
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
```

### 2. [Future Endpoint Placeholder]

*Note: Currently, only the global leaderboard (`leaderboardId = 0`) is supported. Future updates may include additional endpoints to handle multiple leaderboards or different leaderboard types.*

## Data Models

### LeaderboardEntry

Represents an individual entry in the leaderboard.

```java
public class LeaderboardEntry {
    private String username;
    private int totalSteps;
    private int rank; // Leaderboard Rank
    private int leaderboardId;

    // Constructors, Getters, and Setters
}
```

### Steps

Represents the steps data associated with a user.

```java
@Entity
public class Steps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amountOfSteps;

    @ManyToOne
    @JoinColumn(name = "people_username")
    private People user;

    // Constructors, Getters, and Setters
}
```

### People

Represents a user in the system.

```java
@Entity
public class People {

    @Id
    private String username;

    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LocationDay> locations = new ArrayList<>();

    // Constructors, Getters, Setters, and other methods
}
```

## Service Layer

### LeaderboardService

Handles the business logic for generating the leaderboard.

- **Method:** `List<LeaderboardEntry> getLeaderboard()`
    - **Description:** Aggregates total steps per user, sorts them in descending order, assigns ranks, and returns the leaderboard entries.
    - **Returns:** A list of `LeaderboardEntry` objects representing the global leaderboard.

## Usage Examples

### Fetching the Global Leaderboard

To retrieve the global leaderboard, send a `GET` request to the `/leaderboard` endpoint.

- **Request:**

  ```
  GET http://localhost:8080/leaderboard
  ```

- **Response:**

  ```json
  [
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
  ```

## Additional Information

### Future Enhancements

- **Multiple Leaderboards:** Support for different types of leaderboards (e.g., daily, weekly) by allowing `leaderboardId` to be specified in requests.
- **Pagination:** Implement pagination for the leaderboard to handle large datasets efficiently.
- **Filtering and Sorting:** Add parameters to filter or sort the leaderboard based on different criteria.

