package client.utils;

import commons.User;

public class TestUserStorage implements UserStorage {
    private User user;

    public TestUserStorage(User user) {
        this.user = user;
    }

    @Override
    public User load() {
        return user;
    }

    @Override
    public void save(User user) {
        this.user = user;
    }
}
