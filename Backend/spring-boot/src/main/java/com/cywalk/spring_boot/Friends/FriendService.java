package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendService {

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
        Optional<FriendRequest> fr = friendRequestRepository.findFriendRequestByUser_getting_requestedAndUser_requesting(userGettingRequested, userRequesting);
        if (fr.isPresent()) {
            return false;
        }
        FriendRequest fr2 = new FriendRequest(userRequesting, userGettingRequested);

        userGettingRequested.addFriendRequest(friendRequestRepository.save(fr2));

        return true;
    }

    /**
     * Approves a given friend request and then updates the database correspondingly
     * @param fr the friend request
     */
    public void approveFriendRequest(FriendRequest fr) {
        // assume it exists
        People userGettingRequested = fr.getUser_getting_requested();
        userGettingRequested.clearFriendRequest(fr);
        People userRequesting = fr.getUser_requesting();
        userRequesting.addFriend(userGettingRequested);
        userGettingRequested.addFriend(userRequesting);
        friendRequestRepository.delete(fr);
        peopleService.saveUser(userGettingRequested);
        peopleService.saveUser(userRequesting);
    }

    /**
     * Denys a friend request updates People and FrienRequest tables correspondingly
     * @param fr the friend request
     */
    public void denyFriendRequest(FriendRequest fr) {
        People userGettingRequested = fr.getUser_requesting();
        userGettingRequested.clearFriendRequest(fr);
        friendRequestRepository.delete(fr);
        peopleService.saveUser(userGettingRequested);
    }
}
