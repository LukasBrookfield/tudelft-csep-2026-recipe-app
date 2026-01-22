package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUserStorage implements UserStorage {
    public static final String FILE_NAME = "UserConfig.json";
    private String filePath;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JsonUserStorage object for storing and retrieving user data in json format
     * @param filePath The file path of the json file
     * @param objectMapper The object mapper to use for reading/writing
     */
    public JsonUserStorage(String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
        System.out.println("UserConfig path = " + new File(filePath).getAbsolutePath());

    }

    @Override
    public User load() {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                User newUser = new User();
                save(newUser);
                return newUser;
            }
            return objectMapper.readValue(new File(filePath), User.class);
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
            objectMapper.writeValue(new File(filePath), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}