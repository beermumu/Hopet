package com.app.hopet.Models;

public class Comment {
    private String user;
    private String text;
    private String key;
    private String time;

    public Comment() {
    }

    public Comment(String key , String user, String text, String time) {
        this.key = key;
        this.user = user;
        this.text = text;
        this.time = time;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
