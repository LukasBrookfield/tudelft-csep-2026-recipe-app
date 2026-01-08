package client.utils;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserConfigTest {
    private UserConfig userConfig;
    private List<Recipe> allRecipes;
    private List<Ingredient> shoppingList;
    private List<Long> favouriteRecipes;
    private User user;
    private UserStorage userStorage;
    private Ingredient ingredient;

    @BeforeEach
    public void setUp() {
        allRecipes = new ArrayList<>(List.of(new Recipe("Fish and chips"),
                new Recipe("Beans on toast"),
                new Recipe("Shepherd's pie")));
        for (int i = 0; i < allRecipes.size(); i++) {
            allRecipes.get(i).id = i + 1;
        }
        IngredientType t = new IngredientType("Potato", null, null, null);
        ingredient = new Ingredient(t, 1.0, Unit.TO_TASTE, null);
        shoppingList = new ArrayList<>(List.of(
            new Ingredient(t, 1.0, Unit.G, allRecipes.get(0)),
            new Ingredient(t, 2.0, Unit.ML, allRecipes.get(1)),
            new Ingredient(t, 3.0, Unit.HANDFUL, allRecipes.get(2))
        ));
        favouriteRecipes = new ArrayList<>(List.of(1L, 2L));
        user = new User(favouriteRecipes, shoppingList);
        userStorage = new TestUserStorage(user);
        userConfig = new UserConfig(userStorage, user);
    }

    @Test
    public void autoLoadTest() {
        UserConfig userConfig = new UserConfig(userStorage);
        assertEquals(userStorage.load().getFavouriteRecipes(), userConfig.getFavouriteRecipes());
        assertEquals(userStorage.load().getShoppingList(), userConfig.getShoppingList());
    }

    @Test
    public void saveUserTest() {
        userConfig.setShoppingList(new ArrayList<>());
        userConfig.setFavouriteRecipes(new ArrayList<>());
        userConfig.saveUser();
        assertEquals(new User(), userStorage.load());
    }

    @Test
    public void isFavouriteRecipeTest() {
        assertTrue(userConfig.isFavouriteRecipe(allRecipes.get(0)));
    }

    @Test
    public void isFavouriteRecipeFalseTest() {
        assertFalse(userConfig.isFavouriteRecipe(allRecipes.get(2)));
    }

    @Test
    public void addFavouriteRecipeTest() {
        userConfig.addFavouriteRecipe(allRecipes.get(2));
        assertTrue(userConfig.isFavouriteRecipe(allRecipes.get(2)));
    }

    @Test
    public void removeFavouriteRecipeTest() {
        userConfig.removeFavouriteRecipe(allRecipes.get(0));
        assertFalse(userConfig.isFavouriteRecipe(allRecipes.get(0)));
    }

    @Test
    public void removeDeletedRecipesTest() {
        allRecipes.remove(0);
        allRecipes.remove(0);
        int count = userConfig.removeDeletedRecipes(allRecipes);
        assertEquals(2, count);
        assertTrue(userConfig.getFavouriteRecipes().isEmpty());
    }

    @Test
    public void getShoppingListTest() {
        assertEquals(shoppingList, userConfig.getShoppingList());
    }

    @Test
    public void getFavouriteRecipesTest() {
        assertEquals(favouriteRecipes, userConfig.getFavouriteRecipes());
    }

    @Test
    public void setShoppingListTest() {
        List<Ingredient> shoppingList = List.of();
        userConfig.setShoppingList(shoppingList);
        assertEquals(shoppingList, userConfig.getShoppingList());
    }

    @Test
    public void addToShoppingListTest(){
        int count = userConfig.getShoppingList().size();
        userConfig.addShoppingListItem(ingredient);
        assertEquals(count + 1, userConfig.getShoppingList().size());
        assertEquals(ingredient, userConfig.getShoppingList().get(count));
    }
}
