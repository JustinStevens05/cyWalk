
# Organization Service API Documentation


## Base URL
```
http://localhost:8080
```

## Endpoints

### 1. Create Organization
Create a new organization with a unique name.

- **Endpoint:** `/organizations`
- **Method:** `POST`
- **Description:** Allows a user to create a new organization by providing a unique organization name.

#### Request Body:
```json
{
    "name": "Fitness Enthusiasts"
}
```

#### Fields:
- `name` (String, required): The unique name of the organization.

#### Responses:

- **Success:**
  - **Status:** `200 OK`
  - **Body:**
    ```json
    {
        "id": 1,
        "name": "Fitness Enthusiasts",
        "users": []
    }
    ```

- **Failure:**
  - **Status:** `400 Bad Request`
  - **Description:** If an organization with the provided name already exists.

---

### 2. Join Organization
Allow a user to join an existing organization.

- **Endpoint:** `/organizations/{orgId}/join`
- **Method:** `POST`
- **Description:** Enables a user to join an organization by providing their username and the organization's ID.

#### Path Parameters:
- `orgId` (Long, required): The ID of the organization to join.

#### Request Body:
```json
{
    "username": "john_doe"
}
```

#### Fields:
- `username` (String, required): The username of the user joining the organization.

#### Responses:

- **Success:**
  - **Status:** `200 OK`
  - **Description:** User successfully joined the organization.

- **Failure:**
  - **Status:** `400 Bad Request`
  - **Description:** If the organization does not exist, the user does not exist, or the user is already a member of the organization.

---

### 3. List Organization Leaderboard
Retrieve the leaderboard specific to an organization.

- **Endpoint:** `/organizations/{orgId}/leaderboard`
- **Method:** `GET`
- **Description:** Fetches the leaderboard for a specific organization, ranking its members based on their total steps.

#### Path Parameters:
- `orgId` (Long, required): The ID of the organization whose leaderboard is being retrieved.

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
            "leaderboardId": 1
        },
        {
            "username": "jane_smith",
            "totalSteps": 12000,
            "rank": 2,
            "leaderboardId": 1
        },
        {
            "username": "alice_jones",
            "totalSteps": 9000,
            "rank": 3,
            "leaderboardId": 1
        }
    ]
    ```

- **Failure:**
  - **Status:** `404 Not Found`
  - **Description:** If the organization does not exist.

---

### 4. List All Users in Organization
Retrieve a list of all users who are members of a specific organization.

- **Endpoint:** `/organizations/{orgId}/users`
- **Method:** `GET`
- **Description:** Lists all users that belong to a particular organization.

#### Path Parameters:
- `orgId` (Long, required): The ID of the organization whose users are being listed.

#### Responses:

- **Success:**
  - **Status:** `200 OK`
  - **Body:**
    ```json
    [
        {
            "username": "john_doe",
            "email": "john@example.com",
            "locations": []
        },
        {
            "username": "jane_smith",
            "email": "jane@example.com",
            "locations": []
        },
        {
            "username": "alice_jones",
            "email": "alice@example.com",
            "locations": []
        }
    ]
    ```

- **Failure:**
  - **Status:** `404 Not Found`
  - **Description:** If the organization does not exist.

---


## Usage Examples

### 1. Creating a New Organization
**Request:**

- **Method:** `POST`
- **URL:** `http://localhost:8080/organizations`
- **Body:**
```json
{
    "name": "Fitness Enthusiasts"
}
```

**Response:**

- **Status:** `200 OK`
- **Body:**
```json
{
    "id": 1,
    "name": "Fitness Enthusiasts",
    "users": []
}
```

---

### 2. Joining an Organization
**Request:**

- **Method:** `POST`
- **URL:** `http://localhost:8080/organizations/1/join`
- **Body:**
```json
{
    "username": "john_doe"
}
```

**Response:**

- **Status:** `200 OK`

---

### 3. Retrieving an Organization's Leaderboard
**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/organizations/1/leaderboard`

**Response:**

- **Status:** `200 OK`
- **Body:**
```json
[
    {
        "username": "john_doe",
        "totalSteps": 15000,
        "rank": 1,
        "leaderboardId": 1
    },
    {
        "username": "jane_smith",
        "totalSteps": 12000,
        "rank": 2,
        "leaderboardId": 1
    },
    {
        "username": "alice_jones",
        "totalSteps": 9000,
        "rank": 3,
        "leaderboardId": 1
    }
]
```

---

### 4. Listing All Users in an Organization
**Request:**

- **Method:** `GET`
- **URL:** `http://localhost:8080/organizations/1/users`

**Response:**

- **Status:** `

200 OK`
- **Body:**
```json
[
    {
        "username": "john_doe",
        "email": "john@example.com",
        "locations": []
    },
    {
        "username": "jane_smith",
        "email": "jane@example.com",
        "locations": []
    },
    {
        "username": "alice_jones",
        "email": "alice@example.com",
        "locations": []
    }
]
```

---

## Additional Information

### Data Validation
Ensure that the data provided in requests meets the necessary criteria. For example:
- Organization Name: Must be unique and not empty.
- Username: Must exist in the system before joining an organization.

### Error Handling
The API returns appropriate HTTP status codes based on the outcome of requests:
- `200 OK`: Successful operations.
- `400 Bad Request`: Invalid input or request (e.g., organization name already exists, user already a member).
- `404 Not Found`: Resource not found (e.g., organization does not exist).

### Security Considerations
- **Authentication:** Ensure that only authenticated users can create or join organizations.
- **Authorization:** Implement role-based access control if certain actions should be restricted (e.g., only admins can delete organizations).
- **Input Sanitization:** Protect against injection attacks by validating and sanitizing all input data.

### WebSocket Integration for Organization Leaderboards
Organizations have their own leaderboards. To receive real-time updates for an organization's leaderboard:

- **WebSocket Endpoint:** Clients should subscribe to `/topic/leaderboard/organization/{orgId}`.
- **Example:** `/topic/leaderboard/organization/1`

**Broadcasting Updates:** When an organization's leaderboard changes (e.g., when a user within the organization adds new steps), the `LeaderboardService` should broadcast the updated leaderboard to the corresponding WebSocket topic.

---