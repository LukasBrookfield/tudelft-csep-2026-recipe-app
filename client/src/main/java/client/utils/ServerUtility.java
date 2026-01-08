package client.utils;

import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.RecipeNutrition;
import java.util.List;
import java.util.function.Consumer;

public interface ServerUtility {

    /**
     * Do nothing if client is connected to the chanel
     * or create a websocket client and connect to a session
     */
    public void connectWebSocketIfNeeded();


    /**
     * Establishes a subscription to receive real-time updates whenever the
     * complete list of recipes changes
     *
     * @param listener A functional consumer that accepts the new, complete list of
     *                 Recipe objects whenever an update is broadcast by the server.
     */
    public void subscribeToRecipeList(Consumer<List<Recipe>> listener);

    public RecipeNutrition getRecipeNutrition(long id);

    /**
     * Establishes a subscription to receive real-time updates whenever
     * a specific recipe changes
     *
     * @param id       The id of the recipe
     * @param listener A functional consumer that accepts the updated Recipe
     *                 whenever this specific recipe is broadcast by the server.
     */
    public void subscribeToRecipe(long id, Consumer<Recipe> listener);

    /**
     * Sends a GET request to {.../api/recipes} to retrieve all recipes
     * stored on the server.
     *
     * @return a List of Recipe objects returned by the server
     */
    public List<Recipe> getRecipes();

    /**
     * Sends a POST request to {.../api/recipes} to add a recipe
     * and store it on the server.
     *
     * @param recipe the Recipe object to add
     * @return a Recipe object returned by the server
     */
    public Recipe addRecipe(Recipe recipe);

    /**
     * Sends a PUT request to update an existing recipe at {.../api/recipes/{id}}.
     *
     * @param id            The ID of the recipe to update.
     * @param updatedRecipe The recipe object with updated fields.
     * @return The updated recipe object returned by the server.
     */
    public Recipe updateRecipe(long id, Recipe updatedRecipe);

    /**
     * Sends a DELETE request to remove a recipe at {.../api/recipes/{id}}.
     *
     * @param id The ID of the recipe to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteRecipe(long id);


    /**
     * Sends a GET request to {.../api/ingredients} to retrieve all ingredients
     * stored on the server.
     *
     * @return a List of Ingredient objects returned by the server
     */
    public List<Ingredient> getIngredients();

    /**
     * Sends a POST request to {.../api/ingredients} to add an ingredient
     * and store it on the server.
     *
     * @param ingredient the Ingredient object to add
     * @return an Ingredient object returned by the server
     */
    public Ingredient addIngredient(Ingredient ingredient);

    /**
     * Sends a PUT request to update an existing ingredient at {.../api/ingredients/{id}}.
     *
     * @param id                The ID of the ingredient to update.
     * @param updatedIngredient The Ingredient object with updated fields.
     * @return The updated Ingredient object returned by the server.
     */
    public Ingredient updateIngredient(long id, Ingredient updatedIngredient);

    /**
     * Sends a DELETE request to remove an ingredient at {.../api/ingredients/{id}}.
     *
     * @param id The ID of the ingredient to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteIngredient(long id);

    /**
     * Sends a GET request to {.../api/ingredientTypes} to retrieve all ingredientTypes
     * stored on the server.
     *
     * @return a List of IngredientTypes objects returned by the server
     */
    public List<IngredientType> getIngredientTypes();

    /**
     * Sends a POST request to {.../api/ingredientTypes} to add an ingredientType
     * and store it on the server.
     *
     * @param ingredientType the IngredientType object to add
     * @return an IngredientType object returned by the server
     */
    public IngredientType addIngredientType(IngredientType ingredientType);

    /**
     * Sends a PUT request to update an existing ingredientType at {.../api/ingredientTypes/{id}}.
     *
     * @param id                The ID of the ingredientType to update.
     * @param updatedIngredientType The IngredientType object with updated fields.
     * @return The updated IngredientType object returned by the server.
     */
    public IngredientType updateIngredientType(long id, IngredientType updatedIngredientType);

    /**
     * Sends a DELETE request to remove an ingredientType at {.../api/ingredientTypes/{id}}.
     *
     * @param id The ID of the ingredientType to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteIngredientType(long id);

    /**
     * Checks whether the server is reachable by sending a simple GET request
     * to the root URL. If the connection fails due to a refused or timed-out
     * connection, this method returns false.
     *
     * @return true if the server responds, false if the connection fails
     */
    public boolean isServerAvailable();
}