package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name="friend_list")
public class FriendList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "friend_id")
    private String friendId;

    @Column(name = "status")
    private FriendRequestStatus status;


    public FriendList(String userId, String friendId, FriendRequestStatus status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
    }

    public FriendList() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendList{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", friendId='" + friendId + '\'' +
                ", status=" + status +
                '}';
    }
}
