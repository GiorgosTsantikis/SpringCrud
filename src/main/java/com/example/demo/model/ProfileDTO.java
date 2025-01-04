package com.example.demo.model;

import com.example.demo.entities.UserDetails;

public class ProfileDTO {

    private String username,email;
    private UserDetails userDetails;

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
}
