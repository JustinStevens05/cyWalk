# Post
## Request a user
URL: *{url}/friends/{key}/request/{username}*
Where 
- **url** is the server url, 
- **key** is the session key, 
- **username** is the username that the logged in user is requesting.
Returns: HTTP status only
# Put
## Approve a request
URL: *{url}/friends/{key}/request/{username}*
Where:
- **url** is the server url
- **key** is the session key
- **username** is the username of the user you would like to approve (they must have sent a request)
Returns: HTTP status only

# Get
## Get all the friends of a user
URL: *{url}/friends/{key}*
Where:
- **url** is the server url
- **key** is the session key
Returns: List of People and HTTP status

## Get pending requests
URL: *{url}/friends/requests/{key}*
Where:
- **url** is the server url
- **key** is the session key

# Delete
## Deny a friend request
URL: *{url}/friends/{key}/deny/{username}*
- **url** is the server url
- **key** is the session key
- **username** is the username of the user who sent a request and the logged in user would like to deny

# Data Types
## Friend Request
{ \
	"peopleRequesting": "PEOPLE OBJECT" ,\
	"peopleGettingRequested": "PEOPLE OBJECT" ,\
}

## People
{ \
	"username": "USERNAME" ,\
	"email": "EMAIL",
	"locations" = [ LOCATION_DAY1, LOCATION_DAY2 ] 
}