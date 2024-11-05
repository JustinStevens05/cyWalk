# Summary of feature
Location sessions will be websockets to stream locations from the user to the server. This allows us to log workout "sessions" or events. we can also store these as apart of the user's information then. We still need to add functionality to view past "workouts". The frontend streams locations to the backend, and the backend streams updated distances to the frontend.

# Send to backend
A Location JSON object just like we use in the standard location logging requests.


# Sent to frontend
A double just like what gets received when the distances are totaled for the day.


# TODO
- Add ability to view sessions of these "workouts"
- Quick distance calculation (old plus received)

