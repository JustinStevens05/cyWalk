package com.cywalk.spring_boot.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<People> getUserByUsername(String username) {
        if (peopleRepository.findByUsername(username).isPresent()) {
            logger.warn("issue username already in use");
            return Optional.empty();
        }
        return peopleRepository.findByUsername(username);
    }

    public People saveUser(People people) {
        return peopleRepository.save(people);
    }

    public void deleteUserByName(String name) {
        peopleRepository.deleteByUsername(name);
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
    public Optional<Long> login(UserRequest request) {
        Optional<UserRequest> userRequest = userRequestRepository.findByUsername(request.getUsername());
        if (userRequest.isPresent()) {
            if (userRequest.get().getPassword().equals(request.getPassword())) {
                // return temporary key
                return generateAuthKey(request.getUsername());
                // no need to log info for else case as generate auth key already does this
            }
            else {
                logger.warn("Password incorrect for user. Tried: People: {}; Password: {}", request.getUsername(),request.getPassword());
            }
        }
        else {
            logger.warn("People not found. Tried: People: {}; Password: {}", request.getUsername(),request.getPassword());
        }
        return Optional.empty();
    }

    /**
     * Should be run at the start of all requests that ask for user specific data.
     * @param loginKey the login key returned when logging in by {@link #login(UserRequest)}
     * @return the corresponding user if it exists. Handle with Optional
     */
    public Optional<People> getUserFromLoginKey(long loginKey) {
        Optional<UserModel> userModelResult = userModelRepository.findBySecretKey(loginKey);
        if (userModelResult.isPresent()) {
            return Optional.of(userModelResult.get().getUser());
        }
        else {
            logger.info("could not find a user with login key: {}", loginKey);
            return Optional.empty();
        }
    }
}
