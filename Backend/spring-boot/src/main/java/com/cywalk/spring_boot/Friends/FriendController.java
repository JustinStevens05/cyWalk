package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.Key;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/{key}/request/{username}")
    public ResponseEntity<String> requestToFriend(@PathVariable Long key, @PathVariable String username) {
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
    @PutMapping("/{key}/request/approve/{username}")
    public ResponseEntity<String> approveFriendRequest(@PathVariable Long key, @PathVariable String username) {
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
    @GetMapping("/{key}")
    public ResponseEntity<String> getFriends(@PathVariable Long key) {
	Optional<People> peopleResult = peopleService.getUserFromKey(key);
	if (peopleResult.isEmpty()) {
	    return ResponseEntity.badRequest().build();
	}
	List<People> friendsResult = friendService.getFriends(peopleResult.get()); 
	if (friendsResult.isEmpty()) {
            return ResponseEntity.of(Optional.of());
        }

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

    /**
     *
     * @param key User session key
     * @return the current pending friend requests for the user
     */
    @GetMapping("/requests/{key}")
    public ResponseEntity<String> getFriendRequests(@PathVariable Long key) {
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
    @DeleteMapping("/{key}/request/deny/{username}")
    public ResponseEntity<String> denyFriendRequest(@PathVariable Long key, @PathVariable String username) {
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
}
