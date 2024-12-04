package com.example.demo.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="listing")
public class Listing {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="name")
    private String name;
    @Column(name="city")
    private String city;
    @Column(name="state")
    private String state;
    @Column(name="photo")
    private String photo;
    @Column(name="laundry")
    private boolean laundry;
    @Column(name="wifi")
    private boolean wifi;
    @Column(name="available_units")
    private int availableUnits;
    @Column(name="price")
    private int price;
    @Column(name="rooms")
    private int rooms;

    public Listing(){}


    public Listing(String name, String city, String state, String photo, boolean laundry, boolean wifi,  int availableUnits,int price,int rooms) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.photo = photo;
        this.laundry = laundry;
        this.wifi = wifi;

        this.availableUnits = availableUnits;
        this.price=price;
        this.rooms=rooms;
    }

    public Listing(Listing listing){
        this.name = listing.name;
        this.city = listing.city;
        this.state = listing.state;
        this.photo = listing.photo;
        this.laundry = listing.laundry;
        this.wifi = listing.wifi;
        this.availableUnits = listing.availableUnits;
        this.price=listing.price;
        this.rooms=listing.rooms;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isLaundry() {
        return laundry;
    }

    public void setLaundry(boolean laundry) {
        this.laundry = laundry;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }
}
