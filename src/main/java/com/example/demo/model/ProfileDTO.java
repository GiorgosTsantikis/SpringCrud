package com.example.demo.model;

import com.example.demo.entities.FriendList;
import com.example.demo.entities.UserDetails;

import java.util.List;

public class ProfileDTO {

    private String username,email;
    private UserDetails userDetails;
    private List<FriendList> friends;

    public ProfileDTO(String username, String email, UserDetails userDetails) {
        this.username = username;
        this.email = email;
        this.userDetails = userDetails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public List<FriendList> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendList> friends) {
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userDetails=" + userDetails +
                ", friends=" + friends +
                '}';
    }
}
