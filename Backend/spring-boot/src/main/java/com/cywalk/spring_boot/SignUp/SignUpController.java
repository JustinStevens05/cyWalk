package com.cywalk.spring_boot.Users.SignUp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final PeopleService peopleService;

    @Autowired
    public SignUpController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @PostMapping
    public ResponseEntity<People> registerUser(@RequestBody People user) {

        return ResponseEntity.status(501).build();
    }


    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable String username) {
        return ResponseEntity.status(501).build();
    }


}
