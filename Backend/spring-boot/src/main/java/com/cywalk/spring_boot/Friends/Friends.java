package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;


@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    // just the friends table
    @ManyToOne
    @JoinColumns()
    People requester;
    @ManyToOne
    People requested;

    @Enumerated(EnumType.ORDINAL)
    FriendRequestStatus status;
}
