package com.cywalk.spring_boot.Friends;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class FriendRequest {
    @Id
    private Long requestID;

    @ManyToOne
    private People peopleRequesting;
    @ManyToOne
    private People peopleGettingRequested;

    public FriendRequest() {
    }

    public FriendRequest(People PeopleRequesting, People peopleGettingRequested) {
        this.peopleRequesting = PeopleRequesting;
        this.peopleGettingRequested = peopleGettingRequested;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public People getPeopleRequesting() {
        return peopleRequesting;
    }

    public void setPeopleRequesting(People user_requesting) {
        this.peopleRequesting = user_requesting;
    }

    public People getPeopleGettingRequested() {
        return peopleGettingRequested;
    }

    public void setPeopleGettingRequested(People user_getting_requested) {
        this.peopleGettingRequested = user_getting_requested;
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
                ", user_requesting=" + peopleRequesting +
                ", user_getting_requested=" + peopleGettingRequested +
                '}';
    }
}
