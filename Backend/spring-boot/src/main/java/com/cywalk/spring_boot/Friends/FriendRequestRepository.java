package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Optional<FriendRequest> findByRequestID(Long id);

    List<FriendRequest> findAllBySender(People peopleRequesting);
    List<FriendRequest> findAllByReceiver(People peopleGettingRequested);

    Optional<FriendRequest> findBySenderAndReceiver(People peopleRequesting, People peopleGettingRequested);
    List<FriendRequest> findByReceiverAndStatus(People receiver, FriendRequestStatus status);

    Optional<FriendRequest> findByReceiverAndSender(People receiver, People requester);
    List<FriendRequest> findByReceiverOrSenderAndStatus(People receiver, People requester, FriendRequestStatus status);

}
