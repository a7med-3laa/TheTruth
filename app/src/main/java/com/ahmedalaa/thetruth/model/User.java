package com.ahmedalaa.thetruth.model;

/**
 * Created by ahmed on 30/01/2018.
 */

public class User {
    String id;
    String name;
    String email;
    String userName;
    String photoURL;

    public User() {
    }

    public User(String ID, String name, String email, String userName, String photoURL) {
        this.id = ID;
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.photoURL = photoURL;
    }

    public String getID() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }


    public String getPhotoURL() {
        return photoURL;
    }
}

