package com.cywalk.spring_boot.Users;
import com.cywalk.spring_boot.Friends.FriendService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cywalk.spring_boot.websocket.OnlineUserService;
import com.cywalk.spring_boot.websocket.OrganizationOnlineUsersWebSocket;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PeopleService {

    // I love comptime
    public static final int AMOUNT_LEAGUES = League.values().length;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private UserRequestRepository userRequestRepository;

    private final Logger logger = LoggerFactory.getLogger(PeopleService.class);
    @Autowired
    private FriendService friendService;

    public PeopleService() {}

    public Optional<People> createUser(People user) {
        if (peopleRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Issue: username already in use");
            return Optional.empty();
        }
        return Optional.of(peopleRepository.save(user));
    }

    @Transactional
    public void saveUserRequest(UserRequest userRequest) {
        userRequestRepository.save(userRequest);
    }

    @Transactional
    public Optional<People> getUserByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    @Transactional
    public People saveUser(People people) {
        return peopleRepository.save(people);
    }

    /**
     * Delete a user in the database based off of the passed in username
     * @param name the username of the user to delete
     */
    @Transactional
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

            UserModel model = new UserModel(userResult.get());
            userModelRepository.save(model);

            People user = userResult.get();
            if (user.getOrganization() != null) {
                Long orgId = user.getOrganization().getId();
                onlineUserService.userLoggedIn(username, orgId);

                OrganizationOnlineUsersWebSocket.broadcastOnlineUsers(
                        orgId, onlineUserService.getOnlineUsers(orgId));
            }

            return Optional.of(model.getSecretKey());
        } else {

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

    @Transactional
    public ResponseEntity<Void> logout(Long key) {
        Optional<UserModel> toDelete = userModelRepository.findBySecretKey(key);
        if (toDelete.isPresent()) {
            userModelRepository.deleteBySecretKey(key);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @return all users in the database
     */
    public List<People> getAllPeople() {
        return peopleRepository.findAll();
    }

    /**
     * updates and returns the league that the current user is in.
     * This function calculates the user's global ranking relative to everyone else and then returns that
     * @param username the username of the user.
     *                 THIS FUNCTION SHOULD ONLY BE CALLED WHEN WE KNOW FOR SURE THAT THE USER FOR SAID USERNAME EXISTS
     * @return the league that they are in
     */
    /*
    public League updateLeagueForUser(String username) {
        Optional<Long> rankingResult = ;
        long ranking = 1;
        if (rankingResult.isPresent()) {
            ranking = rankingResult.get();
        }
        int amountOfUsers = getAllPeople().size();
        // ranking as a percent corresponds to the league
        double pointInRank = (double) ranking / (double) amountOfUsers;
        // this number times the amount of leagues gives the league a user is in
        return League.values()[(int) pointInRank * AMOUNT_LEAGUES];
    }
     */

}
