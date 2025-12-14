package client.utils;

import commons.User;

public interface UserStorage {
    /**
     * Loads the user from the underlying storage
     * @return The User object.
     */
    User load();

    /**
     * Saves the user to the underlying storage
     * @param user The user to save.
     */
    void save(User user);
}
