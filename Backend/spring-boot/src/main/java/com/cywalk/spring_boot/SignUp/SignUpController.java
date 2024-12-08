package com.cywalk.spring_boot.SignUp;

import com.cywalk.spring_boot.Admins.AdminModel;
import com.cywalk.spring_boot.Admins.AdminService;
import com.cywalk.spring_boot.Admins.AdminSession;
import com.cywalk.spring_boot.Organizations.CreateOrganizationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.cywalk.spring_boot.Organizations.Organization;
import com.cywalk.spring_boot.Organizations.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final PeopleService peopleService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    private AdminService adminService;

    @Autowired
    public SignUpController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {

        if (userRequest.getUsername() == null || userRequest.getUsername().isEmpty()
                ||  userRequest.getPassword().isEmpty() || userRequest.getPassword() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Username and password are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (peopleService.getUserByUsername(userRequest.getUsername()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Username already in use.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }


        People user = new People();
        user.setUsername(userRequest.getUsername());
        user.setEmail("placeholder@gmail.com");

        Optional<People> createdUser = peopleService.createUser(user);

        if (createdUser.isPresent()) {
            peopleService.saveUserRequest(userRequest);

            Optional<Long> authKey = peopleService.generateAuthKey(user.getUsername());
            if (authKey.isPresent()) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("username", createdUser.get().getUsername());
                responseBody.put("key", authKey.get());
                return ResponseEntity.ok(responseBody);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Can't Gen Key!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "User wasn't created.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/organization")
    @Operation(summary = "sign up for organization", description = "sign up an organization and create an admin if one exists. uses the key from the output as session key")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Created a user for an  existing organization"),
            @ApiResponse(responseCode = "405", description = "Admin already exists for an organization", content = @Content)
    })
    public ResponseEntity<AdminSession> SignupOrganizationAndAdmin(
            @RequestBody @Parameter(name = "organization", description = "The organization") CreateOrganizationRequest organization,
            @RequestBody @Parameter(name = "admin", description = "the admin") AdminModel admin
    ) {
        Organization org = organizationService.createOrganization(organization.getName());

        if (adminService.adminExistsForOrganization(organization.getName() + " " + admin.getUsername())) {
            return ResponseEntity.ok(adminService.signUpAdmin(admin, org));
        }

        return ResponseEntity.status(405).build();
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> isUsernameAvailable(@PathVariable String username) {
        boolean available = !peopleService.getUserByUsername(username).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }
}
