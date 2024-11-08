package com.cywalk.spring_boot.Users;

import org.apache.catalina.User;
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
                   userModelRepository.deleteBySecretKey(userModel.getSecretKey());
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
}
