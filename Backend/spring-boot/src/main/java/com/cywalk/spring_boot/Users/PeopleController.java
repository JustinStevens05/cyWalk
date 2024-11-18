package com.cywalk.spring_boot.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "retrieves a People model for a corresponding username in the database")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping("/username/{username}")
    public Optional<People> getUserByUsername(@PathVariable @Parameter(name = "username", description = "the username of a user used when signed in", example = "ckugel") String username) {
        return peopleService.getUserByUsername(username);
    }

    @Operation(summary = "gets all of the users", description = "Retrieves all of the users from the database and returns those entitys")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping
    public List<People> getAllUsers() {
        return peopleService.getAllPeople();
    }

    @Operation(summary = "creates a new user with a person schema")
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
    @Operation(summary = "Log in a user with the corresponding user request")
    @PutMapping
    public ResponseEntity<Key> login(@RequestBody @Parameter(name = "userRequest", description = "the username password combination to sign in") UserRequest userRequest) {
        return peopleService.login(userRequest);
    }

    /**
     * log out a user and delete the cooresponding usermodel from the database
     *
     * @param key the key that was used for the login session
     * @return the
     */
    @Operation(summary = "Log out an instance of a user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "successfully logged out the user"),
            @ApiResponse(responseCode = "404", description = "No logged in users found")
    })
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> logout(@PathVariable @Parameter(name = "key", description = "user key", example = "1") Long key) {
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
    @Operation(summary = "Log out all instances of a user including the current one")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "successfully logged out any logged in users"),
            @ApiResponse(responseCode = "404", description = "No logged in users found")
    })
    @DeleteMapping("/logins/{key}")
    public ResponseEntity<Void> logoutAllOfUser(@PathVariable @Parameter(name = "key", description = "user key", example = "1") Long key) {
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
    @Operation(summary = "Gets all the current User Models in the database. Intended to be cleared using logoutAllOfUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got all active sessions"),
            @ApiResponse(responseCode = "404", description = "No logged in users found")
    })
    @GetMapping("/logins/{key}")
    public ResponseEntity<List<UserModel>> getActiveSessions(@PathVariable @Parameter(name = "key", description = "user key", example = "1") Long key) {
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
