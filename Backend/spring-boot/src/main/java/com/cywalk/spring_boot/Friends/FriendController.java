package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Leaderboard.LeaderboardEntry;
import com.cywalk.spring_boot.Users.Key;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserModelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.JsonArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserModelRepository userModelRepository;

    public static String asJsonString(Object o) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * request to friend a user
     *
     * @param key session key for the current user
     * @param username the username of the person we are trying to friend
     * @return status
     */
    @Operation(summary = "request to friend someone", description = "person A requests to friend person B")
    @PostMapping("/{key}/request/{username}")
    public ResponseEntity<String> requestToFriend(
            @PathVariable @Parameter(name = "key", description = "The session key of the person doing the request (person A)") Long key,
            @PathVariable @Parameter(name = "username", description = "The username of the of the user that is being requested to friend (Person B)") String username) {
        Optional<People> sessionRequest = peopleService.getUserFromKey(key);
        if (sessionRequest.isPresent()) {
            Optional<People> peopleRequest = peopleService.getUserByUsername(username);
            if (peopleRequest.isPresent()) {
                People userRequestingToFriend = peopleRequest.get();
                People userRequesting = sessionRequest.get();
                if (friendService.requestToFriend(userRequesting, userRequestingToFriend)) {
                    return ResponseEntity.ok().build();
                }
                else {
                    // supposed to denote duplicate already exists
                    return ResponseEntity.of(Optional.of("[]"));
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * approve a friend request
     * @param key of the user approving
     * @param username of the user to approve
     * @return status of how the approval went
     */
    @Operation(summary = "Approve a friend request", description = "pretext: Person A requests to friend Person B. \nWhat this endpoint does: Person B approves request from Person A")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request was correctly approved"),
            @ApiResponse(responseCode = "404", description = "Did not find a request from person A to person B for person B to approve")
    })
    @PutMapping("/{key}/request/approve/{username}")
    public ResponseEntity<String> approveFriendRequest(
            @PathVariable @Parameter(name = "key", allowEmptyValue = false, description = "The session key for the user approving. User B session key") Long key,
            @PathVariable @Parameter(name = "username", allowEmptyValue = false, description = "The username for the user who sent the original request. User A username") String username) {
        Optional<People> userRequest = peopleService.getUserFromKey(key);
        if (userRequest.isPresent()) {
            Optional<People> userRequestingRequest = peopleService.getUserByUsername(username);
            if (userRequestingRequest.isPresent()) {
               Optional<FriendRequest> fr = friendService.getFriendRequestFrom(userRequestingRequest.get(), userRequest.get());
               if (fr.isPresent()) {
                   friendService.approveFriendRequest(key, username);
                   return ResponseEntity.of(Optional.of("[]"));
               }
               else {
                   return ResponseEntity.badRequest().build();
               }
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *
     * @param key User session key
     * @return the friends list of a user
     */
    @Operation(summary = "get the friends of a user", description = "List out all of the friends of a user as a string list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = false, description = "gets a list of the usernames of the friends of a user.Person A, Person B, Person C, ...]"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @GetMapping("/{key}")
    public ResponseEntity<String> getFriends(@PathVariable @Parameter(name = "key", description = "the session key for the user") Long key) {
	Optional<People> peopleResult = peopleService.getUserFromKey(key);
	if (peopleResult.isEmpty()) {
	    return ResponseEntity.badRequest().build();
	}
	List<People> friendsResult = friendService.getFriends(peopleResult.get()); 

        ArrayList<String> usernames = new ArrayList<>(friendsResult.size());

        // StringBuilder resultingMessage = new StringBuilder("{\"usernames\":[");
        for (int i = 0; i < friendsResult.size(); i++) {
            People person = friendsResult.get(i);
           // resultingMessage.append("{\"username\":\"" + fr.getSender().getUsername() + "\"},");
             usernames.add(person.getUsername());
        }
        // if (requestsResult.get().size() > 0) {
         //    FriendRequest fr = requestsResult.get().get(requestsResult.get().size() - 1);
         //    resultingMessage.append("{\"username\":\"" + fr.getSender().getUsername() + "\"}");
       // }
        // resultingMessage.append("]}");

        return ResponseEntity.of(Optional.of(asJsonString(usernames)));
    }

    @Operation(summary = "get Friend Leaderboard using session Key. Returns Leaderboard", description = "Returns Leaderboard")
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getFriendLeaderboard(@RequestHeader("User Key") Long sessionKey) {
        Optional<People> userOpt = peopleService.getUserFromKey(sessionKey);
        if (userOpt.isPresent()) {
            People user = userOpt.get();
            Optional<List<LeaderboardEntry>> leaderboardOpt = friendService.getFriendLeaderboard(user);
            if (leaderboardOpt.isPresent()) {
                return ResponseEntity.ok(leaderboardOpt.get());
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //
        }
    }

    /**
     *
     * @param key User session key
     * @return the current pending friend requests for the user
     */
    @Operation(summary = "get pending requests", description = "returns a list of usernames of the pending friend requests")
    @ApiResponses(value = {
            @ApiResponse(useReturnTypeSchema = false, responseCode = "200", description = "A list of all the usernames currently requesting the active user"),
            @ApiResponse(useReturnTypeSchema = false, responseCode = "404", description = "User not found")
    })
    @GetMapping("/requests/{key}")
    public ResponseEntity<String> getFriendRequests(@PathVariable @Parameter(name = "key", description = "the session key of the user") Long key) {
        Optional<List<FriendRequest>> requestsResult = friendService.getPendingFriendRequests(key);

        if (requestsResult.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ArrayList<String> usernames = new ArrayList<>(requestsResult.get().size());

        // StringBuilder resultingMessage = new StringBuilder("{\"usernames\":[");
        for (int i = 0; i < requestsResult.get().size(); i++) {
            FriendRequest fr = requestsResult.get().get(i);
           // resultingMessage.append("{\"username\":\"" + fr.getSender().getUsername() + "\"},");
             usernames.add(fr.getSender().getUsername());
        }
        // if (requestsResult.get().size() > 0) {
         //    FriendRequest fr = requestsResult.get().get(requestsResult.get().size() - 1);
         //    resultingMessage.append("{\"username\":\"" + fr.getSender().getUsername() + "\"}");
       // }
        // resultingMessage.append("]}");

        return ResponseEntity.of(Optional.of(asJsonString(usernames)));
    }

    @Operation(summary = "get all of the requests, for all users", description = "All of the requests that have been made. Debugging endpoint")
    @ApiResponse(useReturnTypeSchema = true, description = "a list of all the friend request objects in the database")
    @GetMapping("/all")
    public List<FriendRequest> getAllRequests() {
        return friendService.getAllRequests();
    }

    /**
     * deny a friend request
     * @param key User session key
     * @param username the username of the user
     * @return Status codes of whether success or not
     */
    @Operation(summary = "denys a friend request", description = "pretext: Person A requests Person B.\nThis endpoint: Person B denys Person A")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a request and will return the username of Person A"),
            @ApiResponse(responseCode = "404", description = "No request from Person A to Person B or Person B was not specified as the user")
    })
    @DeleteMapping("/{key}/request/deny/{username}")
    public ResponseEntity<String> denyFriendRequest(
            @PathVariable @Parameter(name = "key", description = "the user session key. Person B") Long key,
            @Parameter(name = "username", description = "The username of the user who sent the request") @PathVariable String username) {
        Optional<People> userRequest = peopleService.getUserFromKey(key);
        if (userRequest.isPresent()) {
            Optional<People> userRequestingRequest = peopleService.getUserByUsername(username);
            if (userRequestingRequest.isPresent()) {
                Optional<FriendRequest> fr = friendService.getFriendRequestFrom(userRequestingRequest.get(), userRequest.get());
                if (fr.isPresent()) {
                    friendService.denyFriendRequest(key, username);
                    return ResponseEntity.of(Optional.of("[]"));
                }
                else {
                    return ResponseEntity.badRequest().build();
                }
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "suggested friends", description = "gets a list of suggested friends for a given user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully got a list of suggested friends"),
            @ApiResponse(responseCode = "510", description = "User not found")
    })
    @PutMapping("/{key}/suggested")
    public ResponseEntity<List<String>> getSuggestedFriends(@PathVariable @Parameter(name = "key", description = "the session key of the user") Long key) {
        Optional<People> user = peopleService.getUserFromKey(key);
        if (user.isPresent()) {
            List<String> suggestedFriends = friendService.getSuggestedFriends(user.get());
            return ResponseEntity.ok(suggestedFriends);
        } else {
            return ResponseEntity.status(510).build();
        }
    }
}
