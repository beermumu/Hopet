package com.app.hopet.Models;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private String photo;

    public User() {
    }

    public User(String name, String email, String photo) {
        this.name = name;
        this.email = email;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
