package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="user_details")
public class UserDetails {

    @Id
    private Long id;

    @Column(name="country")
    private String country;

    @Column(name="state")
    private String state;

    @Column(name="address")
    private String address;

    @Column(name="profile_pic")
    private String profilePic;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name="user_id",referencedColumnName = "id")
    @JsonIgnore
    private User user;

    public UserDetails(Long id, String country, String state, String address, User user) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.address = address;
        this.user = user;
    }

    public UserDetails() {
    }

    public UserDetails(String country, String state, String address, User user) {
        this.country = country;
        this.state = state;
        this.address = address;
        this.user = user;
    }

    public UserDetails(UserDetails userDetails,Long id){
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
