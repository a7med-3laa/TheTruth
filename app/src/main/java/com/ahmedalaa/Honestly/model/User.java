package com.ahmedalaa.Honestly.model;

import org.parceler.Parcel;

/**
 * Created by ahmed on 30/01/2018.
 */
@Parcel
public class User {
    String id;
    String name;
    String email;
    String userName;
    String photoURL;
    String token;

    public User() {
    }

    public User(String id, String name, String email, String photoURL, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userName = email.substring(0, email.indexOf('@'));
        this.photoURL = photoURL;
        this.token = token;
    }

    public User(String id, String name, String email, String photoURL) {
        this.id = id;
        this.name = name;

        this.userName = email.substring(0, email.indexOf('@'));
        this.email = email;
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

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}

