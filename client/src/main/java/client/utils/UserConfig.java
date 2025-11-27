package client.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserConfig {
    private static final String FILE_NAME = "UserConfig.json";

    public UserConfig() {
        // create new user config file if no file exists
        if (! Files.exists(Paths.get(FILE_NAME))) {
            try {
                new File(FILE_NAME).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
