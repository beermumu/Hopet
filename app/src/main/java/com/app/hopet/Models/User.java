package com.app.hopet.Models;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private ArrayList<Animal> animals;
    private ArrayList<Comment> comments;
    private String photo;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String phoneNumber, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getPhoto() {
        return photo;
    }

}
