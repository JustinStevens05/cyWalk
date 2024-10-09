# Disclaimer
All of the available endpoints will need to be prefaced with the url of the backend server.
If running locally then {url} = http://localhost:8080

# Get mappings
## User operations
{url}/users
 
 -  returns a list of People objects

{url}/users/username/{username}
 - {username} is simply a string of the name of the user
 - this will return the People object for a cooresponding user

{url}/users/{id}
 - gets a People object based off of their session key from logging in

## Location Operations
**{url}/{key}/locations**
 - key represents the session key from logging in
 - gets all of the locations for a given user as a list of Location objects

**{url}/{key}/location/total**
 - key represents the session key from logging in
 - gets the distance traveled today

**{url}/{key}/location/day**
 - key represents the session key from logging in
 - gets a list of all the location days ascribed to a user. 

# Post mappings
TODO: SIGNUP MAPPING HERE \
**{url}/{key}/locations/createLocation**
 - {key} cooresponds to the session key recieved from logging into the account
 - this mapping will add a location to our system and put it into the appropriate location day
 - Request body will be a location (don't populate id or time fields, just send a coordinate object)

# Put mappings
{url}/users
 - this is the login request.
 - the body sent for this request must be a UserRequest object (see JSON object formattings section)
 - returns the session key as a Long. will return "empty" if it could not create the user

# Delete mappings
LOGOUT MAPPING HERE \
DELETE ACCOUNT MAPPING HERE


# JSON object formattings
## People
We couldn't call it user technically but that is what it represents. 

### Sending
Data format (JSON): 
{ \
    "username": "REPLACE_WITH_THE_USERNAME", \
    "email": "REPLACE_WITH_THE_EMAIL" \
}

Example for the ckugel user:
{ \
    "username": "ckugel", \
    "email": "calebkugel1@gmail.com" \
}

### Recieving
When you revieve a person object it will also contain a list of location days.

Data format (JSON):
{
    "username": "USERNAME",
    "email": "EMAIL",
    "locations": [ LOCATION_DAY1, LOCATION_DAY2 ]
}


## Location
JSON: \
{ \
    "latitude": LATITUDE_HERE, \
    "longitude": LONGITUDE_HERE, \
    "elevation" : ELEVATION,\
    "timestamp": "TIME_DATA_WAS_LOGGED_AT", // in format: HH:mm::ss \
}


## Location Day
This data type is meant to represent all of the logged locations over the course of a day. \
TODO: In the future we may add location sessions and then bundle location sessions into each location day. \
JSON:
{ \
    "date": "DATE", // in the format: "uuuu-MM-DD" \
    "totalDistance": TOTAL_DISTANCE_FOR_THE_DAY_AS_A_DOUBLE, \
    "locations": [ LOCATION_1, LOCATION_2, ... LOCATION_N ] \
}


## User request
JSON: 
{ \
    "username": "USERNAME_HERE", \
    "password": "PASSWORD_HERE" \
}


