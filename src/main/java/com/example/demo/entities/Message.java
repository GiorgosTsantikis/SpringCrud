package com.example.demo.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="sender_id")
    private String senderId;

    @Column(name="receiver_id")
    private String receiverId;

    @Column(name="content")
    private String content;

    @Column(name="sentAt")
    private Date sentAt;

    public Message(){}

    public Message(String senderId, String receiverId, String content, Date sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
