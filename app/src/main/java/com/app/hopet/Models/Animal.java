package com.app.hopet.Models;

public class Animal {
    private User user;
    private boolean status;
    private String topic;
    private String type;
    private String breed;
    private String gender;
    private String age;
    private String description;
    private double latitude, longitude;
    private String photoOne, photoTwo, photoThree;

    public Animal() {
    }

    public Animal(User user, boolean status, String topic, String type, String breed, String gender, String age, String description, double latitude, double longitude, String photoOne, String photoTwo, String photoThree) {
        this.user = user;
        this.status = status;
        this.topic = topic;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoOne = photoOne;
        this.photoTwo = photoTwo;
        this.photoThree = photoThree;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhotoOne() {
        return photoOne;
    }

    public void setPhotoOne(String photoOne) {
        this.photoOne = photoOne;
    }

    public String getPhotoTwo() {
        return photoTwo;
    }

    public void setPhotoTwo(String photoTwo) {
        this.photoTwo = photoTwo;
    }

    public String getPhotoThree() {
        return photoThree;
    }

    public void setPhotoThree(String photoThree) {
        this.photoThree = photoThree;
    }
}