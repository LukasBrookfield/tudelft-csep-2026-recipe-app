package client.utils;

import com.google.inject.Inject;
import commons.Ingredient;
import commons.ShoppingListItem;
import commons.User;
import commons.Recipe;
import java.io.*;
import java.util.List;

public class UserConfig {
    private UserStorage userStorage;
    private User user;

    /**
     * Constructs a UserConfig object using an injected UserStorage object
     * @param userStorage The class used for storing user objects
     */
    @Inject
    public UserConfig(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.user = userStorage.load();
    }

    /**
     * Manually construct a UserConfig object. Used only for testing
     */
    public UserConfig(UserStorage userStorage, User user) {
        this.userStorage = userStorage;
        this.user = user;
    }

    /**
     * Writes all the user data to the UserConfig.json file
     */
    public void saveUser() {
        userStorage.save(user);
    }

    public List<ShoppingListItem> getShoppingList() {
        return user.getShoppingList();
    }

    public List<Long> getFavouriteRecipes() {
        return user.getFavouriteRecipes();
    }

    public void setShoppingList(List<ShoppingListItem> shoppingList) {
        user.setShoppingList(shoppingList);
    }

    public void setFavouriteRecipes(List<Long> favouriteRecipes) {
        user.setFavouriteRecipes(favouriteRecipes);
    }

    /**
     * Adds an ingredient to shopping list
     * @param ingredient The ingredient
     */
    public void addShoppingListItem(ShoppingListItem ingredient){
        user.addShoppingListItem(ingredient);
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

    public String getLanguageTag() {
        return user.getLanguageTag();
    }

    public void setLanguageTag(String languageTag) {
        user.setLanguageTag(languageTag);
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
        return oldSize - user.getFavouriteRecipes().size();
    }
}
