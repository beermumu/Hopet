package com.app.hopet.Models;

public class Comment {
    private User user;
    private String text;
    private String key;
    private String time;

    public Comment() {
    }

    public Comment(User user ,String key, String text, String time) {
        this.user = user;
        this.key = key;
        this.text = text;
        this.time = time;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
