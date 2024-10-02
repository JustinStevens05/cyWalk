package com.cywalk.spring_boot.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService() {

    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * delete a user based on the id
     *
     * @param id delete the user from the repository
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * generates an authentication key that the front end should keep track of for.
     * This will return -1 if the id does now work
     *
     * @param id the User key in the entity
     * @return a valid auth key
     */
    public Optional<Long> getAuthKey(Long id) {
        Optional<User> userResult = userRepository.findById(id);
        if (userResult.isPresent()) {
           UserModel model = new UserModel(userResult.get());
           return Optional.of(model.getSecretKey());
        }
        else {
            logger.warn("User not found in repository. id: {}", id);
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
    public Optional<User> getUserFromKey(Long key) {
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
}
