# Endpoint:
ws://localhost:8080/locations/friends?key={key}

# Send: 
Nothing

# Receive:
A json object everytime a friend of the logged in user updates their location.

The Json object format:
```json
{
    "id":ID,
    "username": "string",
    "latitude":LATITUDE,
    "longitude":LONGITUDE,
    "time":"HH:MM:SS",
}
```

**FIELDS:**
- 'id' (Long): ignore
- 'username' (String): the username of the friend who is sending their location
- 'latitude' (double): the latitude of the user
- 'longitude' (double): the longitude of the user
- 'time' (LocalTime, String HH:MM:SS): The local time of that the location was recorded at. Can be used for calculating how fresh a location is.


