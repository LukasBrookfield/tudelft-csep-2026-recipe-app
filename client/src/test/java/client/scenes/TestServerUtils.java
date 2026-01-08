package client.scenes;

import client.utils.ServerUtility;
import client.utils.ServerUtils;
import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestServerUtils implements ServerUtility {

    private final List<IngredientType> ingredientTypes;

    public TestServerUtils() {
        ingredientTypes = new ArrayList<>();
    }

    @Override
    public void connectWebSocketIfNeeded() {

    }

    @Override
    public void subscribeToRecipeList(Consumer<List<Recipe>> listener) {

    }

    @Override
    public void subscribeToRecipe(long id, Consumer<Recipe> listener) {

    }

    @Override
    public List<Recipe> getRecipes() {
        return List.of();
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public Recipe updateRecipe(long id, Recipe updatedRecipe) {
        return null;
    }

    @Override
    public boolean deleteRecipe(long id) {
        return false;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return List.of();
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        return null;
    }

    @Override
    public Ingredient updateIngredient(long id, Ingredient updatedIngredient) {
        return null;
    }

    @Override
    public boolean deleteIngredient(long id) {
        return false;
    }

    @Override
    public List<IngredientType> getIngredientTypes() {
        return ingredientTypes;
    }

    @Override
    public IngredientType addIngredientType(IngredientType ingredientType) {
        ingredientTypes.add(ingredientType);
        return ingredientType;
    }

    @Override
    public IngredientType updateIngredientType(long id, IngredientType updatedIngredientType) {
        return null;
    }

    @Override
    public boolean deleteIngredientType(long id) {
        return false;
    }

    @Override
    public boolean isServerAvailable() {
        return false;
    }
}
