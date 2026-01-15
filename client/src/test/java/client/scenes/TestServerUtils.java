package client.scenes;

import client.utils.ServerUtility;
import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.RecipeNutrition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestServerUtils implements ServerUtility {

    private final List<IngredientType> ingredientTypes;
    private final List<Recipe> recipes;

    public TestServerUtils() {
        ingredientTypes = new ArrayList<>();
        recipes = new ArrayList<>();
    }

    @Override
    public void connectWebSocketIfNeeded() {

    }

    @Override
    public void subscribeToRecipeList(Consumer<List<Recipe>> listener) {

    }

    @Override
    public RecipeNutrition getRecipeNutrition(long id) {
        return new RecipeNutrition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0);
    }

    @Override
    public void subscribeToRecipe(long id, Consumer<Recipe> listener) {

    }

    @Override
    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        if (recipe == null) return null;
        recipe.id = recipes.size() + 1;
        recipes.add(recipe);
        return recipe;
    }

    @Override
    public Recipe updateRecipe(long id, Recipe updatedRecipe) {
        if (updatedRecipe == null) return null;
        for (Recipe recipe : recipes) {
            if (recipe.id == id) {
                recipe.name = updatedRecipe.name;
                recipe.ingredients.clear();
                recipe.ingredients.addAll(updatedRecipe.ingredients);
                recipe.steps.clear();
                recipe.steps.addAll(updatedRecipe.steps);
                recipe.servings = updatedRecipe.servings;
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean deleteRecipe(long id) {
        for (Recipe recipe : recipes) {
            if (recipe.id == id) {
                recipes.remove(recipe);
                return true;
            }
        }
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
        if (ingredientType == null) return null;
        ingredientType.id = recipes.size() + 1;
        ingredientTypes.add(ingredientType);
        return ingredientType;
    }

    @Override
    public IngredientType updateIngredientType(long id, IngredientType updatedIngredientType) {
        if (updatedIngredientType == null) return null;
        for (IngredientType ingredientType : ingredientTypes) {
            if (ingredientType.id == id) {
                ingredientType.name = updatedIngredientType.name;
                ingredientType.nutrition = updatedIngredientType.nutrition;
                ingredientType.density = updatedIngredientType.density;
                return ingredientType;
            }
        }
        return null;
    }

    @Override
    public boolean deleteIngredientType(long id) {
        for (IngredientType ingredientType : ingredientTypes) {
            if (ingredientType.id == id) {
                ingredientTypes.remove(ingredientType);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isServerAvailable() {
        return false;
    }

    @Override
    public void subscribeToIngredientType(long id, Consumer<IngredientType> listener) {

    }

    @Override
    public void subscribeToIngredientTypeList(Consumer<List<IngredientType>> listener) {

    }
}
