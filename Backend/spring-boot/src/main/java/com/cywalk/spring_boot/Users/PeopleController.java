package com.cywalk.spring_boot.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class PeopleController {

    @Autowired
    private PeopleService peopleService;

    @GetMapping("/username/{username}")
    public Optional<People> getUserByUsername(@PathVariable String username) {
        return peopleService.getUserByUsername(username);
    }

    @GetMapping
    public List<People> getAllUsers() {
        return peopleService.getAllPeople();
    }

    @PostMapping
    public People createUser(@RequestBody People people) {
        return peopleService.saveUser(people);
    }

    @GetMapping("/{id}")
    public Optional<People> getUserBySessionKey(@PathVariable Long id) {
        return peopleService.getUserFromKey(id);
    }

    @DeleteMapping("/username/{username}")
    public void deleteUserByName(@PathVariable String username) {
        peopleService.deleteUserByName(username);
    }

    //TODO: @ethan implement post mapping sign up here

    /**
     * @param userRequest usernmae password combo
     * @return the key to be used during the session
     */
    @PutMapping
    public Optional<Long> login(@RequestBody UserRequest userRequest) {
        return peopleService.login(userRequest);
    }
}
