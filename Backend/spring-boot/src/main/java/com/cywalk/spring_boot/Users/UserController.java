package com.cywalk.spring_boot.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/username/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/username/{username}")
    public void deleteUserByName(@PathVariable String username) {
        userService.deleteUserByName(username);
    }

    //TODO: @ethan implement post mapping sign up here

    /**
     * @param userRequest usernmae password combo
     * @return the key to be used during the session
     */
    @PutMapping
    public Optional<Long> login(@RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }
}
