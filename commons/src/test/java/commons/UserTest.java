package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {
    static final String RECIPE_A_NAME = "A";
    static final IngredientType ING_TYPE_A = new IngredientType("A", null, null, null);
    static final IngredientType ING_TYPE_B = new IngredientType("B", null, null, null);
    static final IngredientType ING_TYPE_C = new IngredientType("C", null, null, null);

    static final List<Long> FAVOURITE_RECIPES = List.of(123L, 456L);
    static final List<ShoppingListItem> SHOPPING_LIST = createShoppingList("A", ING_TYPE_A, ING_TYPE_B);
    static final List<String> SELECTED_LANGUAGES = List.of("en", "nl", "pt");

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(FAVOURITE_RECIPES,
                SHOPPING_LIST, SELECTED_LANGUAGES);
    }

    @Test
    void testEquals() {
        // exactly equal user
        User user2 = new User(FAVOURITE_RECIPES, SHOPPING_LIST, SELECTED_LANGUAGES);
        assertEquals(testUser, user2);

        // with empty shopping list
        user2 = new User(FAVOURITE_RECIPES, Collections.emptyList(), SELECTED_LANGUAGES);
        assertNotEquals(testUser, user2);

        // with empty favourite recipes
        user2 = new User(Collections.emptyList(), SHOPPING_LIST, SELECTED_LANGUAGES);
        assertNotEquals(testUser, user2);

        // with same ingredients, but different recipe name (absent, to be implemented differently later)

        // with same shopping, but differently ordered (absent, to be implemented differently later)

        // with different shopping list
        List<ShoppingListItem> otherList = createShoppingList(RECIPE_A_NAME, ING_TYPE_A, ING_TYPE_C);
        user2 = new User(FAVOURITE_RECIPES, otherList, SELECTED_LANGUAGES);
        assertNotEquals(testUser, user2);
    }

    @Test
    void testHashCode() {

        User user2 = new User(FAVOURITE_RECIPES, SHOPPING_LIST, SELECTED_LANGUAGES);
        assertEquals(testUser.hashCode(), user2.hashCode());

        // with empty shopping list
        user2 = new User(FAVOURITE_RECIPES, Collections.emptyList(), SELECTED_LANGUAGES);
        assertNotEquals(testUser.hashCode(), user2.hashCode());

        // with empty favourite recipes
        user2 = new User(Collections.emptyList(), SHOPPING_LIST, SELECTED_LANGUAGES);
        assertNotEquals(testUser.hashCode(), user2.hashCode());

        // with same ingredients, but different recipe name (to be implemented later

        // with different shopping list
        List<ShoppingListItem> otherList = createShoppingList(RECIPE_A_NAME, ING_TYPE_A, ING_TYPE_C);
        user2 = new User(FAVOURITE_RECIPES, otherList, SELECTED_LANGUAGES);
        assertNotEquals(testUser.hashCode(), user2.hashCode());
    }

    // We don't technically need this toString() method, and we're also not using it, so...
    @Test
    void testToString() {
        assertEquals("User[\n" +
                "  favouriteRecipes=[123, 456]\n" +
                "  shoppingList=[1 tablespoon of A, 1 tablespoon of B]\n" +
                "]", testUser.toString());
    }


    private static List<ShoppingListItem> createShoppingList(String recipeName, IngredientType type1, IngredientType type2) {
        Recipe recipe = new Recipe(recipeName);
        return List.of(
                new ShoppingListItem(new Ingredient(type1, 1.0, Unit.TBSP, recipe)),
                new ShoppingListItem(new Ingredient(type2, 1.0, Unit.TBSP, recipe)));
    }
}