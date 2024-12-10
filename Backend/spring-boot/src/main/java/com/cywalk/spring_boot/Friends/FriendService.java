    package com.cywalk.spring_boot.Friends;

    import com.cywalk.spring_boot.Leaderboard.LeaderboardEntry;
    import com.cywalk.spring_boot.Organizations.Organization;
    import com.cywalk.spring_boot.Users.People;
    import com.cywalk.spring_boot.Users.PeopleService;
    import jakarta.transaction.Transactional;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.*;

    import com.cywalk.spring_boot.Leaderboard.LeaderboardService;

    @Service
    public class FriendService {


        private static final Logger log = LoggerFactory.getLogger(FriendService.class);
        @Autowired
        FriendRequestRepository friendRequestRepository;

        @Autowired
        LeaderboardService leaderboardService; // Add dependency

        @Autowired
        PeopleService peopleService;

        /**
         * Request to friend someone
         * @param userRequesting user requesting to friend the other
         * @param userGettingRequested user getting requested to be added
         * @return true if the request was sent. false if it already existed
         */
        @Transactional
        public boolean requestToFriend(People userRequesting, People userGettingRequested) {
            Optional<FriendRequest> fr = getFriendRequestFrom(userRequesting, userGettingRequested);
            if (fr.isPresent()) {
                return false;
            }
            FriendRequest fr2 = new FriendRequest(userRequesting, userGettingRequested, FriendRequestStatus.PENDING);
            friendRequestRepository.save(fr2);
            return true;
        }

        @Transactional
        Optional<List<FriendRequest>> getPendingFriendRequests(Long sessionKey) {
            Optional<People> peopleRequest = peopleService.getUserFromKey(sessionKey);
            if (peopleRequest.isEmpty()) {
                log.warn("Could not fetch pending requests for user as there is no user with the provided key");
                return Optional.empty();
            }
            return Optional.of(friendRequestRepository.findByReceiverAndStatus(peopleRequest.get(), FriendRequestStatus.PENDING));
        }

        public List<FriendRequest> getAllRequests() {
            return friendRequestRepository.findAll();
        }



        /**
         * Approves a given friend request and then updates the database correspondingly
         * @param sessionKey the key of the current session
         * @param username of the user to approve
         */
        @Transactional
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
            Optional<FriendRequest> requestOption = friendRequestRepository.findBySenderAndReceiver(user2, user);

            if (requestOption.isEmpty()) {
                log.warn("friend request did not exist from: {} to: {}", user2.getUsername(), user.getUsername());
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
        @Transactional
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
            Optional<FriendRequest> requestOption = friendRequestRepository.findBySenderAndReceiver(user2, user);

            if (requestOption.isEmpty()) {
                log.warn("friend request did not exist from: {} to: {}", user2.getUsername(), user.getUsername());
                return false;
            }

            FriendRequest request = requestOption.get();
            request.setStatus(FriendRequestStatus.DENIED);
            friendRequestRepository.save(request);
            return true;
        }

        @Transactional
        public List<People> getFriends(People person) {
            List<FriendRequest> requests = friendRequestRepository.findByReceiverOrSender(person, person);
            ArrayList<People> friends = new ArrayList<>(requests.size());
            for (int i = 0; i < requests.size(); i++) {
                if (requests.get(i).getStatus() == FriendRequestStatus.APPROVED) {
                    if (requests.get(i).getReceiver() != person) {
                        friends.add(requests.get(i).getReceiver());
                    }
                    else {
                        friends.add(requests.get(i).getSender());
                    }
                }
            }
            return friends;
        }




        @Transactional
        public Optional<List<LeaderboardEntry>> getFriendLeaderboard(People person) {
            List<People> friendsList = getFriends(person);
            Set<People> friends = new HashSet<>(friendsList);
            friends.add(person);
            if (!friends.isEmpty()) {
                List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard(friends);
                return Optional.of(leaderboard);
            }
            return Optional.empty();
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

        /**
         * Makes and returns a list of suggested friends for a given user
         * @param people the people to check against
         * @return the suggested friends
         */
        @Transactional
        public List<String> getSuggestedFriends(People people) {
            HashMap<People, Long> friendConnections = new HashMap<>();
            List<People> friends = getFriends(people);
            for (People p : friends) {
                List<People> friendsOfFriends = getFriends(p);
                for (People fof : friendsOfFriends) {
                    if (fof.getUsername().equals(people.getUsername())) {
                        continue;
                    }
                    if (!friendConnections.containsKey(fof)) {
                        friendConnections.put(fof, 1L);
                    }
                    else {
                        friendConnections.put(fof, friendConnections.get(fof) + 1);
                    }
                }
            }
            List<String> suggestedFriends = new ArrayList<>();
            // now we put the top 5 friends into the list
            for (int i = 0; i < 5; i++) {
                long max = 0;
                People maxPerson = null;
                for (People p : friendConnections.keySet()) {
                    if (friendConnections.get(p) > max) {
                        max = friendConnections.get(p);
                        maxPerson = p;
                    }
                }
                if (maxPerson != null) {
                    suggestedFriends.add(maxPerson.getUsername());
                    friendConnections.remove(maxPerson);
                }
            }
            return suggestedFriends;
        }
    }
