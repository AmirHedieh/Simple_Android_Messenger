package com.example.amas.messenger;

import java.io.Serializable;

public class User {

    public String uid;
    public String username;
    public String profilePhotoUrl;

    public User(String uid, String username, String profileUrl){
        this.uid = uid;
        this.username = username;
        this.profilePhotoUrl = profileUrl;
    }
}
