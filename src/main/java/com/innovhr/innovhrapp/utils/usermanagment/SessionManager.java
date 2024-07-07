package com.innovhr.innovhrapp.utils.usermanagment;

import com.innovhr.innovhrapp.models.User;

public class SessionManager {

    private static SessionManager instance;
    private User loggedInUser;
    private User.AccessLevel dynamicAccessLevel;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        this.dynamicAccessLevel = user.getAccessLevel();
    }

    public User.AccessLevel getDynamicAccessLevel() {
        return dynamicAccessLevel;
    }

    public void setDynamicAccessLevel(User.AccessLevel dynamicAccessLevel) {
        this.dynamicAccessLevel = dynamicAccessLevel;
    }

    public void resetDynamicAccessLevel() {
        if (loggedInUser != null) {
            this.dynamicAccessLevel = loggedInUser.getAccessLevel();
        }
    }
}
