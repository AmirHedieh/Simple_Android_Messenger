package com.example.amas.messenger;

import java.io.Serializable;

public class User implements Serializable {

    public String uid;
    public String username;
    public String profilePhotoUrl;
    public String email;

    public User(){

    }

    public User(String uid, String username, String profileUrl, String email){
        this.uid = uid;
        this.username = username;
        this.profilePhotoUrl = profileUrl;
        this.email = email;
    }
}
