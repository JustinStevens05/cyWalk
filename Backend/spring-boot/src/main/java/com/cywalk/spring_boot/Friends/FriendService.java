package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {


    private static final Logger log = LoggerFactory.getLogger(FriendService.class);
    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    PeopleService peopleService;

    /**
     * Request to friend someone
     * @param userRequesting user requesting to friend the other
     * @param userGettingRequested user getting requested to be added
     * @return true if the request was sent. false if it already existed
     */
    public boolean requestToFriend(People userRequesting, People userGettingRequested) {
        Optional<FriendRequest> fr = getFriendRequestFrom(userRequesting, userGettingRequested);
        if (fr.isPresent()) {
            return false;
        }
        FriendRequest fr2 = new FriendRequest(userRequesting, userGettingRequested);
        friendRequestRepository.save(fr2);
        return true;
    }

    Optional<List<FriendRequest>> getPendingFriendRequests(Long sessionKey) {
        Optional<People> peopleRequest = peopleService.getUserFromKey(sessionKey);
        if (peopleRequest.isEmpty()) {
            log.warn("Could not fetch pending requests for user as there is no user with the provided key");
            return Optional.empty();
        }
        return Optional.of(friendRequestRepository.findByReceiverAndStatus(peopleRequest.get(), FriendRequestStatus.PENDING));
    }



    /**
     * Approves a given friend request and then updates the database correspondingly
     * @param sessionKey the key of the current session
     * @param username of the user to approve
     */
    public boolean approveFriendRequest(Long sessionKey, String username) {
        Optional<People> peopleRequest = peopleService.getUserFromKey(sessionKey);
        if (peopleRequest.isEmpty()) {
            log.warn("Approval failed due to invalid key");
            return false;
        }
        Optional<People> person2Request = peopleService.getUserByUsername(username);
        if (person2Request.isEmpty()) {
            log.warn("Approval failed due to invalid username (corresponding user)");
            return false;
        }
        People user2 = person2Request.get();
        People user = peopleRequest.get();
        Optional<FriendRequest> requestOption = friendRequestRepository.findBySenderAndReceiver(user, user2);

        if (requestOption.isEmpty()) {
            log.warn("friend request did not exist from: {} to: {}", user.getUsername(), user2.getUsername());
            return false;
        }

        FriendRequest request = requestOption.get();
        request.setStatus(FriendRequestStatus.APPROVED);
        friendRequestRepository.save(request);
        return true;
    }

    /**
     * Approves a given friend request and then updates the database correspondingly
     * @param sessionKey the key of the current session
     * @param username of the user to approve
     */
    public boolean denyFriendRequest(Long sessionKey, String username) {
        Optional<People> peopleRequest = peopleService.getUserFromKey(sessionKey);
        if (peopleRequest.isEmpty()) {
            log.warn("denial failed due to invalid key");
            return false;
        }
        Optional<People> person2Request = peopleService.getUserByUsername(username);
        if (person2Request.isEmpty()) {
            log.warn("denial failed due to invalid username (corresponding user)");
            return false;
        }
        People user2 = person2Request.get();
        People user = peopleRequest.get();
        Optional<FriendRequest> requestOption = friendRequestRepository.findBySenderAndReceiver(user, user2);

        if (requestOption.isEmpty()) {
            log.warn("friend request did not exist from: {} to: {}", user.getUsername(), user2.getUsername());
            return false;
        }

        FriendRequest request = requestOption.get();
        request.setStatus(FriendRequestStatus.DENIED);
        friendRequestRepository.save(request);
        return true;
    }

    public List<People> getFriends(People person) {
        List<FriendRequest> requests = friendRequestRepository.findByReceiverOrSenderAndStatus(person, person, FriendRequestStatus.APPROVED);
        ArrayList<People> friends = new ArrayList<>(requests.size());
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getReceiver() != person) {
                friends.add(requests.get(i).getReceiver());
            }
            else {
                friends.add(requests.get(i).getSender());
            }
        }
        return friends;
    }


    /**
     * Literally just does the repository call
     * @param user_requesting the user requesting the friend
     * @param user_getting_requested the user getting requested
     * @return the cooresponding friend request if it exists in the database
     */
    public Optional<FriendRequest> getFriendRequestFrom(People user_requesting, People user_getting_requested) {
        return friendRequestRepository.findBySenderAndReceiver(user_requesting, user_getting_requested);
    }
}
