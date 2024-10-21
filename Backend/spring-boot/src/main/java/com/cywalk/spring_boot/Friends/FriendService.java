package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Optional<FriendRequest> fr = getFriendRequestFrom(userRequesting, userGettingRequested);
        if (fr.isPresent()) {
            return false;
        }
        FriendRequest fr2 = new FriendRequest(userRequesting, userGettingRequested);
        if (userGettingRequested.getPendingFriendRequests() == null) {
            userGettingRequested.setPendingFriendRequests(new ArrayList<>());
        }
        userGettingRequested.addFriendRequest(friendRequestRepository.save(fr2));

        return true;
    }

    /**
     * Approves a given friend request and then updates the database correspondingly
     * @param fr the friend request
     */
    public void approveFriendRequest(FriendRequest fr) {
        // assume it exists
        People userGettingRequested = fr.getPeopleGettingRequested();
        userGettingRequested.clearFriendRequest(fr);
        People userRequesting = fr.getPeopleRequesting();
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
        People userGettingRequested = fr.getPeopleRequesting();
        userGettingRequested.clearFriendRequest(fr);
        friendRequestRepository.delete(fr);
        peopleService.saveUser(userGettingRequested);
    }

    /**
     * Literally just does the repository call
     * @param user_requesting the user requesting the friend
     * @param user_getting_requested the user getting requested
     * @return the cooresponding friend request if it exists in the database
     */
    public Optional<FriendRequest> getFriendRequestFrom(People user_requesting, People user_getting_requested) {
        return friendRequestRepository.findByPeopleRequestingAndPeopleGettingRequested(user_requesting, user_getting_requested);
    }
}
