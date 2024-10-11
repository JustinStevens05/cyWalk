# SignUpController API Documentation

Welcome to the **SignUpController** API documentation. This guide provides detailed information about the endpoints available in the `SignUpController`, including the structure of the requests and the expected responses. This will help you effectively integrate and interact with the sign-up functionalities of the application using tools like Insomnia or Postman.

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

- **Register a new user**: Create a new user account with the necessary details.
- **Check username availability**: Verify if a desired username is available for registration.

## Base URL

All endpoints related to the `SignUpController` are prefixed with:

```
/signup
```

**Example:** If your server is running on `http://localhost:8080`, the base URL for sign-up endpoints would be `http://localhost:8080/signup`.

## Endpoints

### 1. Register User

**Endpoint:**

```
POST /signup
```

**Description:**

Registers a new user in the system by providing a username and password.

**Request Body:**

The request should contain a JSON object with the following structure:

```json
{
  "username": "string",
  "password": "string"
}
```

**Fields:**

- `username` (String, **Required**): The desired username for the user.
- `password` (String, **Required**): The password for the user.

**Successful Response:**

- **Status Code:** `200 OK`
- **Body:** Returns a JSON object containing the username and an authentication key.

```json
{
  "username": "john_doe",
  "key": 12345
}
```

**Error Responses:**

- **Status Code:** `400 Bad Request`

  **Condition:** Missing `username` or `password` in the request body.

  **Body:**

  ```json
  {
    "message": "Username and password are required."
  }
  ```

- **Status Code:** `409 Conflict`

  **Condition:** The provided `username` is already in use.

  **Body:**

  ```json
  {
    "message": "Username already in use."
  }
  ```

- **Status Code:** `400 Bad Request`

  **Condition:** Failed to create the user.

  **Body:**

  ```json
  {
    "message": "Failed to create user."
  }
  ```

- **Status Code:** `500 Internal Server Error`

  **Condition:** Failed to generate authentication key.

  **Body:**

  ```json
  {
    "message": "Failed to generate authentication key."
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
  "available": true
}
```

**Explanation:**

- `true`: The username is available.
- `false`: The username is already taken.

**Error Responses:**

- **Status Code:** `400 Bad Request`

  **Condition:** Missing `username` path parameter.

  **Body:**

  ```json
  {
    "message": "Username parameter is required."
  }
  ```

## Request and Response Structures

### Register User Request

| Field    | Type   | Description                   | Required |
|----------|--------|-------------------------------|----------|
| username | String | Desired username for the user | Yes      |
| password | String | Password for the user         | Yes      |

**Example:**

```json
{
  "username": "jane_doe",
  "password": "securePassword123"
}
```

### Register User Response

**Successful Response:**

| Field    | Type    | Description                         |
|----------|---------|-------------------------------------|
| username | String  | The username of the newly created user |
| key      | Number  | Authentication key for the user     |

**Example:**

```json
{
  "username": "jane_doe",
  "key": 67890
}
```

### Check Username Availability Request

No request body is needed. The `username` is provided as a path parameter.

**Example:**

```
GET /signup/check-username/jane_doe
```

### Check Username Availability Response

| Field     | Type    | Description                            |
|-----------|---------|----------------------------------------|
| available | Boolean | `true` if available, `false` otherwise |

**Example:**

```json
{
  "available": false
}
```

## Error Handling

The `SignUpController` uses standard HTTP status codes to indicate the success or failure of an API request. Below are common status codes you might encounter:

- **200 OK**: The request was successful.
- **400 Bad Request**: The request was invalid or cannot be served. This could be due to missing required fields.
- **409 Conflict**: The request could not be completed due to a conflict with the current state of the target resource, such as attempting to register a username that already exists.
- **500 Internal Server Error**: An unexpected error occurred on the server side.

**Error Response Structure:**

```json
{
  "message": "Detailed error message."
}
```

- `message`: A detailed message explaining the error.

## Notes

- The `email` field is currently set to a placeholder value in the code (`"placeholder@gmail.com"`) and is not required in the request body.
- The `authKey` is a numerical value generated upon successful user creation.
- Ensure that the `username` and `password` fields are not empty and meet any additional validation criteria your application may have.
- The `PeopleService` and related classes (`People`, `UserRequest`, `Key`) are assumed to be properly defined in your application and handle the business logic accordingly.

---

Feel free to reach out if you have any questions or need further assistance with integrating these endpoints into your application.