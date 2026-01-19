package client.utils;

import client.scenes.TestServerUtils;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecipeUtilsTest {
    private RecipeUtils recipeUtils;
    private Nutrition nutrition;
    private IngredientType cucumber;
    private IngredientType water;
    private IngredientType flour;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    private TestServerUtils testServerUtils;

    @BeforeEach
    void setUp() {
        UserStorage inMemoryStorage = new UserStorage() {
            private User user = new User();
            @Override public User load() { return user; }
            @Override public void save(User u) { this.user = u; }
        };
        UserConfig config = new UserConfig(inMemoryStorage);
        config.setLanguageTag("en");

        LanguageService realLanguageService = new LanguageService(config);

        recipeUtils = new RecipeUtils(realLanguageService);
        testServerUtils = new TestServerUtils();
        nutrition = new Nutrition(10.0, 5.0, 2.0);

        cucumber = new IngredientType("Cucumber", nutrition, new ArrayList<>(), null);
        water = new IngredientType("Water", nutrition, new ArrayList<>(), null);
        flour = new IngredientType("Flour", null, new ArrayList<>(), null);

        recipe1 = new Recipe("Test recipe 1");
        recipe2 = new Recipe("Test recipe 2");
        recipe3 = new Recipe("Test recipe 3");

        recipe1.ingredients.add(new Ingredient(cucumber, 100.0, Unit.G, recipe1));
        recipe1.ingredients.add(new Ingredient(flour, 1500.0, Unit.G, recipe1));

        recipe2.ingredients.add(new Ingredient(water, 1000.0, Unit.ML, recipe2));

        recipe3.ingredients.add(new Ingredient(cucumber, 100.0, Unit.G, recipe1));
        recipe3.ingredients.add(new Ingredient(flour, 1500.0, Unit.G, recipe1));
        recipe3.ingredients.add(new Ingredient(water, 1000.0, Unit.ML, recipe2));
        recipe3.ingredients.add(new Ingredient(water, 1000.0, Unit.ML, recipe2));

    }

    @Test
    void getCaloriesPer100gTest() {
        assertEquals(78.0, recipeUtils.getCaloriesPer100g(cucumber));
    }

    @Test
    void getCaloriesPer100gNullNutritionTest() {
        assertEquals(-1.0, recipeUtils.getCaloriesPer100g(flour));
    }

    @Test
    void normalizeIngredientsGtoKGTest() {
        List<Ingredient> normalizedIngredients = new ArrayList<>();
        normalizedIngredients.add(new Ingredient(cucumber, 100.0, Unit.G, recipe1));
        normalizedIngredients.add(new Ingredient(flour, 1.5, Unit.KG, recipe1));
        recipeUtils.normalizeIngredients(recipe1.ingredients);
        assertEquals(normalizedIngredients, recipe1.ingredients);
    }

    @Test
    void normalizeIngredientsMLtoLTest() {
        List<Ingredient> normalizedIngredients = new ArrayList<>();
        normalizedIngredients.add(new Ingredient(water, 1.0, Unit.L, recipe1));
        recipeUtils.normalizeIngredients(recipe2.ingredients);
        assertEquals(normalizedIngredients, recipe2.ingredients);
    }

    @Test
    void commitLocalIngredientTypesTest() {
        recipeUtils.commitLocalIngredientTypes(recipe3,  testServerUtils);
        List<IngredientType> savedIngredientTypes = new ArrayList<>();
        savedIngredientTypes.add(cucumber);
        savedIngredientTypes.add(flour);
        savedIngredientTypes.add(water);
        assertEquals(savedIngredientTypes, testServerUtils.getIngredientTypes());
    }
}