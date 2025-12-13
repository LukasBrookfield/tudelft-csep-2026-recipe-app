package client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Ingredient;
import commons.User;
import commons.Recipe;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserConfig {
    private static final String FILE_NAME = "UserConfig.json";
    private static final String FILE_PATH = "client/src/main/java/client/utils/" + FILE_NAME;
    private User user;

    /**
     * Constructs a new UserConfig object.
     */
    public UserConfig() {
        readUser();
    }

    /**
     * Reads all the user data from the UserConfig.json file. If the file doesn't exist, a new
     * file is created and an empty User object is saved. If an error occurs, null is returned.
     * @return A new User object representing the contents of the file read
     */
    public void readUser() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                new File(FILE_PATH).createNewFile();
                user = new User(new ArrayList<>(), new ArrayList<>());
                saveUser();
            }
            user = new ObjectMapper().readValue(new File(FILE_PATH), User.class);
        } catch (IOException e) {
            e.printStackTrace();
            user = null;
        }
    }

    /**
     * Writes all the user data to the UserConfig.json file
     */
    public void saveUser() {
        if (user == null) return;
        try(PrintWriter writer = new PrintWriter(FILE_PATH)) {
            ObjectMapper om = new ObjectMapper();
            writer.write(om.writeValueAsString(user));
        } catch (FileNotFoundException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<Ingredient> getShoppingList() {
        return user.getShoppingList();
    }

    public List<Long> getFavouriteRecipes() {
        return user.getFavouriteRecipes();
    }

    public void setShoppingList(List<Ingredient> shoppingList) {
        user.setShoppingList(shoppingList);
    }

    public boolean isFavouriteRecipe(Recipe recipe) {
        return user.getFavouriteRecipes().stream().anyMatch(x -> x == recipe.id);
    }
}
