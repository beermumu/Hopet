package com.app.hopet.Utilities;

import com.app.hopet.Models.User;

public class UserManager {

    private static User user;

    public UserManager(User user) {
        this.user = user;
    }

    public static User getUser() {
        return user;
    }
}
