# Live Leaderboard Feature Documentation

This document provides a comprehensive guide to the **Live Leaderboard** feature of your application. It includes instructions on setting up the environment, running the application, understanding the codebase, and interacting with the API endpoints and WebSocket for real-time updates.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Prerequisites](#prerequisites)
3. [Project Structure](#project-structure)
4. [Setup and Installation](#setup-and-installation)
5. [Running the Application](#running-the-application)
6. [API Endpoints](#api-endpoints)
    - [User Management](#user-management)
    - [Location Tracking](#location-tracking)
    - [Leaderboard](#leaderboard)
    - [Organizations](#organizations)
7. [WebSocket Configuration](#websocket-configuration)
    - [Connecting to the WebSocket](#connecting-to-the-websocket)
    - [Receiving Live Leaderboard Updates](#receiving-live-leaderboard-updates)
8. [Testing the Application](#testing-the-application)
9. [Additional Notes](#additional-notes)
10. [Conclusion](#conclusion)

---

## Introduction

The **Live Leaderboard** feature allows users to see real-time updates of their rankings based on the number of steps they've taken. Steps are calculated using the Haversine formula on the location data provided by the users. This feature enhances user engagement by providing instant feedback and fostering a competitive environment.

---


The application will start on `http://localhost:8080`.

---

## API Endpoints

### User Management

#### **1. Sign Up a User**

- **Endpoint:** `POST /signup`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "username": "user1",
    "password": "password1"
  }
  ```

- **Response:**

  ```json
  {
    "username": "user1",
    "key": 1234567890
  }
  ```

#### **2. Log In a User**

- **Endpoint:** `PUT /users`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "username": "user1",
    "password": "password1"
  }
  ```

- **Response:**

  ```json
  {
    "key": 1234567890
  }
  ```

### Location Tracking

#### **1. Start a Location Session**

- **Endpoint:** `POST /{key}/locations/start`
- **Headers:** None
- **Body:** Empty
- **Note:** Replace `{key}` with the session key obtained during signup or login.

#### **2. Log Location Data**

- **Endpoint:** `POST /{key}/locations/log`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "latitude": 37.7749,
    "longitude": -122.4194,
    "elevation": 10.0
  }
  ```

- **Note:** Send multiple location data points to simulate movement.

#### **3. End the Location Session**

- **Endpoint:** `DELETE /{key}/locations/end`
- **Headers:** None
- **Body:** Empty

### Leaderboard

#### **1. Get the Global Leaderboard**

- **Endpoint:** `GET /leaderboard`
- **Headers:** None
- **Response:**

  ```json
  [
    {
      "username": "user1",
      "totalSteps": 1500,
      "rank": 1,
      "leaderboardId": 0
    },
    {
      "username": "user2",
      "totalSteps": 1200,
      "rank": 2,
      "leaderboardId": 0
    }
  ]
  ```

### Organizations

#### **1. Create an Organization**

- **Endpoint:** `POST /organizations`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "name": "Fitness Enthusiasts"
  }
  ```

- **Response:**

  ```json
  {
    "id": 1,
    "name": "Fitness Enthusiasts",
    "users": []
  }
  ```

#### **2. Join an Organization**

- **Endpoint:** `POST /organizations/{orgId}/join`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "username": "user1"
  }
  ```

- **Response:** `200 OK` if successful.

#### **3. Get Organization Leaderboard**

- **Endpoint:** `GET /organizations/{orgId}/leaderboard`
- **Response:**

  ```json
  [
    {
      "username": "user1",
      "totalSteps": 5000,
      "rank": 1,
      "leaderboardId": 0
    }
  ]
  ```

#### **4. List Organization Users**

- **Endpoint:** `GET /organizations/{orgId}/users`
- **Response:**

  ```json
  [
    {
      "username": "user1",
      "email": "user1@example.com",
      "locations": []
    }
  ]
  ```

#### **5. Get Organization ID by Name**

- **Endpoint:** `POST /organizations/get-id`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "name": "Fitness Enthusiasts"
  }
  ```

- **Success Response:**

  ```json
  {
    "id": 1
  }
  ```

- **Failure Response:**

    - **Status:** 404 Not Found

#### **6. Get Organization Info by Username**

- **Endpoint:** `POST /organizations/get-info`
- **Headers:** `Content-Type: application/json`
- **Body:**

  ```json
  {
    "username": "user1"
  }
  ```

- **Success Response:**

  ```json
  {
    "id": 1,
    "name": "Fitness Enthusiasts",
    "users": [
      {
        "username": "user1",
        "email": "user1@example.com",
        "locations": []
      },
      // Other users...
    ]
  }
  ```

- **Failure Response:**

    - **Status:** 404 Not Found

---
