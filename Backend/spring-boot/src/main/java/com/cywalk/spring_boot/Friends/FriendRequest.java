package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "requestID")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestID;

    @ManyToOne
    @JoinColumn(name = "people_username")
    private People sender;
    @ManyToOne
    @JoinColumn(name = "receiver_username")
    private People receiver;


    @Enumerated(EnumType.ORDINAL)
    FriendRequestStatus status;

    public FriendRequest() {
    }

    public FriendRequest(Long requestID, People sender, People receiver, FriendRequestStatus status) {
        this.requestID = requestID;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public FriendRequest(People sender, People receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public FriendRequest(People userRequesting, People userGettingRequested, FriendRequestStatus friendRequestStatus) {
        this.sender = userRequesting;
        this.receiver = userGettingRequested;
        this.status = friendRequestStatus;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public People getSender() {
        return sender;
    }

    public void setSender(People user_requesting) {
        this.sender = user_requesting;
    }

    public People getReceiver() {
        return receiver;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public void setReceiver(People user_getting_requested) {
        this.receiver = user_getting_requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(requestID, that.requestID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestID);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "requestID=" + requestID +
                ", user_requesting=" + sender +
                ", user_getting_requested=" + receiver +
                '}';
    }
}
