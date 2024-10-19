package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.Key;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    /**
     * request to friend a user
     *
     * @param key session key for the current user
     * @param username the username of the person we are trying to friend
     * @return status
     */
    @PostMapping("/{key}/request/{username}")
    public ResponseEntity<Void> requestToFriend(@PathVariable Long key, @PathVariable String username) {
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
                    return ResponseEntity.status(409).build();
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
    public ResponseEntity<Void> approveFriendRequest(@PathVariable Long key, @PathVariable String username) {
        Optional<People> userRequest = peopleService.getUserFromKey(key);
        if (userRequest.isPresent()) {
            Optional<People> userRequestingRequest = peopleService.getUserByUsername(username);
            if (userRequestingRequest.isPresent()) {
               Optional<FriendRequest> fr = friendService.getFriendRequestFrom(userRequestingRequest.get(), userRequest.get());
               if (fr.isPresent()) {
                   friendService.approveFriendRequest(fr.get());
                   return ResponseEntity.ok().build();
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
    public ResponseEntity<List<People>> getFriends(@PathVariable Long key) {
        Optional<People> userRequest = peopleService.getUserFromKey(key);
        return userRequest.map(people -> ResponseEntity.ok(people.getFriends())).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     *
     * @param key User session key
     * @return the current pending friend requests for the user
     */
    @GetMapping("/requests/{key}")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable Long key) {
        Optional<People> userRequest = peopleService.getUserFromKey(key);
        return userRequest.map(people -> ResponseEntity.ok(people.getPendingFriendRequests())).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * deny a friend request
     * @param key User session key
     * @param username the username of the user
     * @return Status codes of whether success or not
     */
    @DeleteMapping("/{key}/request/deny/{username}")
    public ResponseEntity<Void> denyFriendRequest(@PathVariable Long key, @PathVariable String username) {
        Optional<People> userRequest = peopleService.getUserFromKey(key);
        if (userRequest.isPresent()) {
            Optional<People> userRequestingRequest = peopleService.getUserByUsername(username);
            if (userRequestingRequest.isPresent()) {
                Optional<FriendRequest> fr = friendService.getFriendRequestFrom(userRequestingRequest.get(), userRequest.get());
                if (fr.isPresent()) {
                    friendService.denyFriendRequest(fr.get());
                    return ResponseEntity.ok().build();
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
