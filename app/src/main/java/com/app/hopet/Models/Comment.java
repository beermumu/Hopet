package com.app.hopet.Models;

public class Comment {
    private User user;
    private Animal animal;
    private String text;

    public Comment(User user, Animal animal, String text) {
        this.user = user;
        this.animal = animal;
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public Animal getAnimal() {
        return this.animal;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
