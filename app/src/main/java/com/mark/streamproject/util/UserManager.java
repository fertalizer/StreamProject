package com.mark.streamproject.util;

import com.mark.streamproject.data.User;

public class UserManager {
    private User mUser;

    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    private UserManager() {

    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

}
