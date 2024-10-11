package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Optional<FriendRequest> findByRequestID(Long id);
    Optional<FriendRequest> findFriendRequestByUser_getting_requested(People user_getting_requested);
    Optional<FriendRequest> findByUser_requesting(People user_requesting);

    List<FriendRequest> findAllByUser_requesting(People userRequesting);
    List<FriendRequest> findAllByUser_getting_requested(People user_getting_requested);
    Optional<FriendRequest> findFriendRequestByUser_getting_requestedAndUser_requesting(People user_getting_requested, People user_requesting);
}
