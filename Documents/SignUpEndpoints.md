# SignUpController API Documentation

Welcome to the **SignUpController** API documentation. This guide provides detailed information about the endpoints available in the `SignUpController`, including the structure of the requests and the expected responses. This will help you effectively integrate and interact with the Sign-Up functionalities of the application using tools like Insomnia.

## Table of Contents

1. [Overview](#overview)
2. [Base URL](#base-url)
3. [Endpoints](#endpoints)
    - [1. Register User](#1-register-user)
    - [2. Check Username Availability](#2-check-username-availability)
4. [Request and Response Structures](#request-and-response-structures)
    - [Register User](#register-user-request)
    - [Check Username Availability](#check-username-availability-request)
5. [Error Handling](#error-handling)
6. [Notes](#notes)

---

## Overview

The `SignUpController` is responsible for handling user registration and related operations within the application. It provides endpoints to:

- **Register a new user**: Create a new user account with necessary details.
- **Check username availability**: Verify if a desired username is available for registration.

## Base URL

All endpoints related to the `SignUpController` are prefixed with:

```
/signup
```

**Example:** If your server is running on `http://localhost:8080`, the base URL for Sign-Up endpoints would be `http://localhost:8080/signup`.

## Endpoints

### 1. Register User

**Endpoint:**

```
POST /signup
```

**Description:**

Registers a new user in the system by creating a `People` entity with the provided details.

**Request Body:**

The request should contain a JSON object representing the user to be registered. Below is the structure of the expected JSON:

```json
{
  "username": "string",
  "email": "string",
}
```

**Fields:**

- `username` (String, **Required**): The unique username for the user.
- `email` (String, **Required**): The user's email address.



**Successful Response:**

- **Status Code:** `200 OK`
- **Body:** Returns the created `People` object with all its details.

```json
{
  "username": "john_doe",
  "email": "john.doe@example.com",
  "key": 1232
}
```

**Error Responses:**

- **Status Code:** `409 Conflict`

  **Condition:** The provided `username` is already in use.

  **Body:**

  ```json
  {
    "message": "Username already in use."
  }
  ```


### 2. Check Username Availability

**Endpoint:**

```
GET /signup/check-username/{username}
```

**Description:**

Checks whether a specific `username` is available for registration.

**Path Parameters:**

- `username` (String, **Required**): The username to check for availability.

**Example:**

```
GET /signup/check-username/john_doe
```

**Successful Response:**

- **Status Code:** `200 OK`
- **Body:** Returns a boolean indicating availability.

```json
{
  "available": false
}
```

**Explanation:**

- `true`: The username is available.
- `false`: The username is already taken.




**Status Code:** `404 Not Found`

  **Condition:** The endpoint is accessed incorrectly or the username parameter is missing.

  **Body:**

  ```json
  {
    "error": "Not Found",
    "message": "Endpoint not found.",
    "path": "/signup/check-username/"
  }
  ```

## Request and Response Structures

### Register User Request

| Field      | Type       | Description                        | Required |
|------------|------------|------------------------------------|----------|
| username   | String     | Unique username for the user       | Yes      |
| email      | String     | User's email address               | Yes      |
| locations  | Array      | List of associated `LocationDay` objects | No       |

**Example:**

```json
{
  "username": "jane_doe",
  "email": "jane.doe@example.com",
  "locations": [
    {
      "locationId": 201,
      "date": "2024-09-15",
      "details": "Attended a concert."
    }
  ]
}
```

### Check Username Availability Request

**Path Parameter:**

- `username` (String): The username to check.

**Example:**

```
/signup/check-username/jane_doe
```

**Response Body:**

| Field      | Type    | Description                         |
|------------|---------|-------------------------------------|
| available  | Boolean | `true` if available, `false` otherwise |

**Example:**

```json
{
  "available": true
}
```

## Error Handling

The `SignUpController` follows standard HTTP status codes to indicate the success or failure of an API request. Below are common status codes you might encounter:

- **200 OK**: The request was successful.
- **201 Created**: A new resource has been successfully created. *(Not used in current endpoints but recommended for POST requests)*
- **400 Bad Request**: The request was invalid or cannot be served. This could be due to malformed JSON, missing required fields, or invalid data formats.
- **409 Conflict**: The request could not be completed due to a conflict with the current state of the target resource, such as attempting to register a username that already exists.
- **404 Not Found**: The requested resource could not be found.
- **500 Internal Server Error**: An unexpected error occurred on the server side.

**Error Response Structure:**

```json
{
  "timestamp": "2024-10-09T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message.",
  "path": "/signup"
}
```

- `timestamp`: The date and time when the error occurred.
- `status`: The HTTP status code.
- `error`: A brief description of the error.
- `message`: A detailed message explaining the error.
- `path`: The endpoint path that was accessed.



