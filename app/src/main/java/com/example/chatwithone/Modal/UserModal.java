package com.example.chatwithone.Modal;

import com.google.firebase.Timestamp;

public class UserModal {
    private String phone;
    private String username;

    private Timestamp timestamp;

    private String userId;
     private String fcmToken;
    public UserModal() {
    }

    public UserModal(String phone, String username, Timestamp timestamp,String userId) {
        this.phone = phone;
        this.username = username;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
