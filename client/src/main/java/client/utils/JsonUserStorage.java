package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUserStorage implements UserStorage {
    private final String FILE_PATH;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JsonUserStorage object for storing and retrieving user data in json format
     * @param FILE_PATH The file path of the json file
     * @param objectMapper The object mapper to use for reading/writing
     */
    public JsonUserStorage(String FILE_PATH, ObjectMapper objectMapper) {
        this.FILE_PATH = FILE_PATH;
        this.objectMapper = objectMapper;
        System.out.println("UserConfig path = " + new File(FILE_PATH).getAbsolutePath());

    }

    @Override
    public User load() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                User newUser = new User();
                save(newUser);
                return newUser;
            }
            return objectMapper.readValue(new File(FILE_PATH), User.class);
        } catch (IOException e) {
            User newUser = new User();
            save(newUser);
            return newUser;
        }
    }

    @Override
    public void save(User user) {
        if (user == null) return;
        try {
            objectMapper.writeValue(new File(FILE_PATH), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}