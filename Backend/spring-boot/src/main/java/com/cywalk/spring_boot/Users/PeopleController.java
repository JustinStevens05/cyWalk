package com.cywalk.spring_boot.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cywalk.spring_boot.websocket.OnlineUserService;
import com.cywalk.spring_boot.websocket.OrganizationOnlineUsersWebSocket;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class PeopleController {

    @Autowired
    private PeopleService peopleService;
    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private OnlineUserService onlineUserService;

    @GetMapping("/username/{username}")
    public Optional<People> getUserByUsername(@PathVariable String username) {
        return peopleService.getUserByUsername(username);
    }

    @GetMapping
    public List<People> getAllUsers() {
        return peopleService.getAllPeople();
    }

    @PostMapping
    public Optional<People> createUser(@RequestBody People people) {
        return peopleService.createUser(people);
    }

    @GetMapping("/{id}")
    public Optional<People> getUserBySessionKey(@PathVariable Long id) {
        return peopleService.getUserFromKey(id);
    }

    @DeleteMapping("/username/{username}")
    public void deleteUserByName(@PathVariable String username) {
        peopleService.deleteUserByName(username);
    }

    /**
     * @param userRequest username password combo
     * @return the key to be used during the session
     */
    @PutMapping
    public ResponseEntity<Key> login(@RequestBody UserRequest userRequest) {
        return peopleService.login(userRequest);
    }

    /**
     * log out a user and delete the cooresponding usermodel from the database
     *
     * @param key the key that was used for the login session
     * @return the
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> logout(@PathVariable Long key) {
        Optional<UserModel> toDelete = userModelRepository.findBySecretKey(key);
        if (toDelete.isPresent()) {
            People user = toDelete.get().getUser();
            userModelRepository.deleteBySecretKey(key);

            // Update online users
            if (user.getOrganization() != null) {
                Long orgId = user.getOrganization().getId();
                onlineUserService.userLoggedOut(user.getUsername(), orgId);
                // Broadcast update
                OrganizationOnlineUsersWebSocket.broadcastOnlineUsers(
                        orgId, onlineUserService.getOnlineUsers(orgId));
            }

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Log out all instances of a user including the current one
     * @param key session key
     * @return a successful key
     */
    @DeleteMapping("/logins/{key}")
    public ResponseEntity<Void> logoutAllOfUser(@PathVariable Long key) {
       ResponseEntity<List<UserModel>> result = getActiveSessions(key);
       if (result.getStatusCode().value() == 200) {
           List<UserModel> elements = result.getBody();
           if (elements == null) {
               return ResponseEntity.notFound().build();
           }
           else {
               for (UserModel userModel : elements) {
                   peopleService.logout(userModel.getSecretKey());
               }
               return ResponseEntity.ok().build();
           }
       }
       else {
           return ResponseEntity.status(result.getStatusCode()).build();
       }
    }

    /**
     * Gets all the current UserModels in the database. intended to be wiped out with {@link #logoutAllOfUser(Long)}
     *
     * Bad request if there is no user corresponding to the passed in key
     * not found if there are no other active sessions for the user
     *
     * @param key the session key from {@link #login(UserRequest)}
     * @return all the sessions of a user
     */
    @GetMapping("/logins/{key}")
    public ResponseEntity<List<UserModel>> getActiveSessions(@PathVariable Long key) {
        Optional<UserModel> userRequest = userModelRepository.findBySecretKey(key);
        if (userRequest.isPresent()) {
            List<UserModel> current = userModelRepository.findAllByPeople(userRequest.get().getUser());
            if (current.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(current);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets the current league a user is in
     * @param key the key of said user
     * @return the current league of the user
     */
    @GetMapping("/league")
    public Optional<League> getCurrentLeague(@PathVariable Long key) {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        if (peopleResult.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(peopleResult.get().getLeague());
    }


    /**
     * updates or generates the league for the current user.
     * WARNING THIS IS VERY SLOW SO USE GET WHEN YOU CAN AND AVOID FREQUENT CALLS
     * @param key the user's session key
     * @return the league the user is in or nothing if there was an issue
     */
    @PutMapping("/league")
    public Optional<League> updateAndGetLeague(@PathVariable Long key) {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        return peopleResult.map(people -> peopleService.updateLeagueForUser(people.getUsername()));
    }

    /**
     * A very, very slow request.
     * This should be accelerated in the future by updating the user's ranking via other user's new location Sessions
     * @param key the key of the user
     * @return ranking of a user globally
     */
    @PutMapping("/ranking/global")
    public Optional<Long> updateAndGetRankingGlobal(@PathVariable Long key) {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        if (peopleResult.isEmpty()) {
            return Optional.empty();
        }
        return peopleService.getUserGlobalRanking(peopleResult.get().getUsername());
    }

    /**
     * A moderately slow request.
     * @param key the user's key
     * @return ranking of a user amongst friends
     */
    @PutMapping("/ranking/friends")
    public Optional<Long> updateAndGetRankingFriends(@PathVariable Long key) {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        if (peopleResult.isEmpty()) {
            return Optional.empty();
        }
        return peopleService.getUserRankingFriends(peopleResult.get().getUsername());
    }

    /**
     * A slow request.
     * @param key the user's key
     * @return ranking of a user amongst organizations
     */
    @PutMapping("/ranking/organization")
    public Optional<Long> updateAndGetRankingOrganization(@PathVariable Long key) {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        if (peopleResult.isEmpty()) {
            return Optional.empty();
        }
        return peopleService.getUserOrganizationRanking(peopleResult.get().getUsername());
    }


}
