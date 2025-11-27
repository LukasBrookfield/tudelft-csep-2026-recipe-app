package client.utils;

import commons.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserConfig {
    private static final String FILE_NAME = "UserConfig.json";
    private User user;

    /**
     * Constructs a new UserConfig object.
     * Also creates a new UserConfig.json file if there is not one already in the local directory
     */
    public UserConfig() {
        // create new user config file if no file exists
        if (! Files.exists(Paths.get(FILE_NAME))) {
            try {
                new File(FILE_NAME).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user = readUser();
    }

    public User readUser() {
        return null;
    }

    public void saveUser() {

    }
}
