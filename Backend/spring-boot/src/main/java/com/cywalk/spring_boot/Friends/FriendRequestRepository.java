package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Optional<FriendRequest> findByRequestID(Long id);

    List<FriendRequest> findAllByPeopleRequesting(People peopleRequesting);
    List<FriendRequest> findAllByPeopleGettingRequested(People peopleGettingRequested);

    Optional<FriendRequest> findByPeopleRequestingAndPeopleGettingRequested(People peopleRequesting, People peopleGettingRequested);
}
