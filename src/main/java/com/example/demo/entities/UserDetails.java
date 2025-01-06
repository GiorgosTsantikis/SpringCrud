package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="user_details")
public class UserDetails {

    @Id
    @Column(name="user_details_id")
    private String id;

    @Column(name="country")
    private String country;

    @Column(name="state")
    private String state;

    @Column(name="address")
    private String address;

    @Column(name="profile_pic")
    private String profilePic;




    public UserDetails(String id, String country, String state, String address) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.address = address;
    }

    public UserDetails() {
    }

    public UserDetails(String id){
        this.id=id;
    }

    public UserDetails(String country, String state, String address) {
        this.country = country;
        this.state = state;
        this.address = address;

    }

    public UserDetails(UserDetails userDetails,String id){
        this.id=id;
       this.country=userDetails.country;
       this.state=userDetails.state;
       this.address=userDetails.address;
    }



    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "UserDetails{" +
                "id='" + id + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", address='" + address + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}
