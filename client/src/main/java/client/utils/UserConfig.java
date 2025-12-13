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
import java.util.HashSet;
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
     * file is created and an empty User object is saved. If an error occurs, an empty user is
     * created but not saved.
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
            user = new User(new ArrayList<>(), new ArrayList<>());
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

    /**
     * Returns if the recipe is one of the user's favourite recipes
     * @param recipe The recipe to check
     * @return true if the recipe is a favourite, else false
     */
    public boolean isFavouriteRecipe(Recipe recipe) {
        return user.getFavouriteRecipes().contains(recipe.id);
    }

    /**
     * Adds a recipe to the list of favourite recipes
     * @param recipe The recipe to add
     */
    public void addFavouriteRecipe(Recipe recipe) {
        user.getFavouriteRecipes().add(recipe.id);
    }

    /**
     * Removes a recipe from the list of favourite recipes
     * @param recipe The recipe to remove
     */
    public void removeFavouriteRecipe(Recipe recipe) {
        user.getFavouriteRecipes().remove(recipe.id);
    }

    /**
     * Removes the user's favourite recipes that have been deleted from the database and returns
     * how many have been deleted
     * @param recipes The list of recipes to check
     * @return The number of recipes deleted
     */
    public int removeDeletedRecipes(List<Recipe> recipes) {
        List<Long> idList = recipes.stream().map(x -> x.id).toList();
        int oldSize = user.getFavouriteRecipes().size();
        user.getFavouriteRecipes().retainAll(idList);
        saveUser();
        return oldSize - user.getFavouriteRecipes().size();
    }
}
