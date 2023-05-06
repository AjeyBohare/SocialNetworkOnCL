package com.example.Models;

import java.util.Date;

public class User {
     int userId;
     String username;
     String handleId;
     String email;
     String password;
     Date creationTime;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getHandleId() {
        return handleId;
    }
    public void setHandleId(String handleId) {
        this.handleId = handleId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

     
    
}
