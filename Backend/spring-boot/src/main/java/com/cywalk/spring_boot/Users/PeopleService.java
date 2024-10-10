package com.cywalk.spring_boot.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private UserRequestRepository userRequestRepository;

    private final Logger logger = LoggerFactory.getLogger(PeopleService.class);

    public PeopleService() {}

    public Optional<People> createUser(People user) {
        if (peopleRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("issue username already in use");
            return Optional.empty();
        }
        return Optional.of(peopleRepository.save(user));
    }

    public Optional<People> getUserByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    public People saveUser(People people) {
        return peopleRepository.save(people);
    }

    /**
     * Delete a user in the database based off of the passed in username
     * @param name the username of the user to delete
     */
    public void deleteUserByName(String name) {
        if (getUserByUsername(name).isPresent()) {
            peopleRepository.deleteByUsername(name);
        }
    }

    /**
     * generates an authentication key that the front end should keep track of for.
     * This will return -1 if the id does now work
     *
     * @param username the People key in the entity
     * @return a valid auth key
     */
    public Optional<Long> generateAuthKey(String username) {
        Optional<People> userResult = peopleRepository.findByUsername(username);
        if (userResult.isPresent()) {
            // make a user model which tracks the People
           UserModel model = new UserModel(userResult.get());
           userModelRepository.save(model);
            // return the key to be used the rest of the time
           return Optional.of(model.getSecretKey());
        }
        else {
            logger.warn("somehow could not find user in the People Table. Tried: {}", username);
            return Optional.empty();
        }
    }

    /**
     * verifies whether a key is correct or not.
     * If it isn't valid the return wil be empty.
     * If the key is valid it will return the user object.
     *
     * @param key the secret temporary key
     * @return A user if it's available
     */
    public Optional<People> getUserFromKey(Long key) {
        Optional<UserModel> userModelResult = userModelRepository.findBySecretKey(key);

        if (userModelResult.isPresent()) {
            UserModel userModel = userModelResult.get();
            return Optional.of(userModel.getUser());
        }
        else {
            logger.warn("key did not find a value. key passed in: {}", key);
            return Optional.empty();
        }
    }


    /**
     * Login a user
     * @param request the username password combination
     * @return a key to be used throughout the session until the user logs out
     */
    public ResponseEntity<Key> login(UserRequest request) {
        Optional<UserRequest> userRequest = userRequestRepository.findByUsername(request.getUsername());
        if (userRequest.isPresent()) {
            if (userRequest.get().getPassword().equals(request.getPassword())) {
                // try and generate the key
                Optional<Long> toReturn = generateAuthKey(request.getUsername());
                if (toReturn.isPresent()) {
                    // return temporary key
                    return ResponseEntity.ok(new Key(toReturn.get()));
                }
                // no need to log info for else case as generate auth key already does this
            }
            else {
                logger.warn("Password incorrect for user. Tried: People: {}; Password: {}", request.getUsername(),request.getPassword());
            }
        }
        else {
            logger.warn("People not found. Tried: People: {}; Password: {}", request.getUsername(),request.getPassword());
        }
        return ResponseEntity.notFound().build();
    }

    public List<People> getAllPeople() {
        return peopleRepository.findAll();
    }
}
