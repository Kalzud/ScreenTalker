package com.eou.screentalker.Models;

import java.io.Serializable;

public class UserModel implements Serializable {
    //for users
   private String username;
    private String email;
    private String password;
    private String pImage_url;
    private String bio;
    private String dob;
    private String fcmToken;
    private String id;

    public UserModel(){
        //default constructor
    }

    public UserModel(String username, String email, String password, String pImage_url, String bio, String dob, String fcmToken, String id) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pImage_url = pImage_url;
        this.bio = bio;
        this.dob = dob;
        this.fcmToken = fcmToken;
        this.id = id;
    }

    //getters ans setters

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getpImage_url() {
        return pImage_url;
    }

    public void setpImage_url(String pImage_url) {
        this.pImage_url = pImage_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
