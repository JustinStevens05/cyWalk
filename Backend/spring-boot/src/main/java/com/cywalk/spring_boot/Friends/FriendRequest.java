package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class FriendRequest {
    @Id
    private Long requestID;

    @ManyToOne
    private People user_requesting;
    @ManyToOne
    private People user_getting_requested;

    public FriendRequest() {
    }

    public FriendRequest(People user_requesting, People user_getting_requested) {
        this.user_requesting = user_requesting;
        this.user_getting_requested = user_getting_requested;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public People getUser_requesting() {
        return user_requesting;
    }

    public void setUser_requesting(People user_requesting) {
        this.user_requesting = user_requesting;
    }

    public People getUser_getting_requested() {
        return user_getting_requested;
    }

    public void setUser_getting_requested(People user_getting_requested) {
        this.user_getting_requested = user_getting_requested;
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
                ", user_requesting=" + user_requesting +
                ", user_getting_requested=" + user_getting_requested +
                '}';
    }
}
