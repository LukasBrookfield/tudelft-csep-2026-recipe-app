package client.utils;

import commons.Ingredient;
import commons.Recipe;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.net.ConnectException;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    private final Client client;
    private static final String SERVER = "http://localhost:8080/";

    @Inject
    public ServerUtils(Client client) {
        this.client = client;
    }
        //recipes requests:

    /**
     * Sends a GET request to {.../api/recipes} to retrieve all recipes
     * stored on the server.
     *
     * @return a List of Recipe objects returned by the server
     */
    public List<Recipe> getRecipes() {
        return client.target(SERVER).path("api/recipes") //
                .request(APPLICATION_JSON) //
                .get(new GenericType<List<Recipe>>() {});
    }

    /**
     * Sends a POST request to {.../api/recipes} to add a recipe
     * and store it on the server.
     *
     * @param recipe the Recipe object to add
     * @return a Recipe object returned by the server
     */
    public Recipe addRecipe(Recipe recipe) {
        return client.target(SERVER).path("api/recipes")
                .request(APPLICATION_JSON)
                .post(Entity.entity(recipe, APPLICATION_JSON), Recipe.class);
    }

    /**
     * Sends a PUT request to update an existing recipe at {.../api/recipes/{id}}.
     *
     * @param id The ID of the recipe to update.
     * @param updatedRecipe The recipe object with updated fields.
     * @return The updated recipe object returned by the server.
     */
    public Recipe updateRecipe(long id, Recipe updatedRecipe) {
        return client.target(SERVER).path("api/recipes/" + id)
                .request(APPLICATION_JSON)
                .put(Entity.entity(updatedRecipe, APPLICATION_JSON), Recipe.class);
    }

    /**
     * Sends a DELETE request to remove a recipe at {.../api/recipes/{id}}.
     *
     * @param id The ID of the recipe to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteRecipe(long id) {
            Response response = client
                    .target(SERVER).path("api/recipes/" + id)
                    .request(APPLICATION_JSON)
                    .delete();
            return response.getStatus() == 204;
    }

        //ingredients requests:
    /**
     * Sends a GET request to {.../api/ingredients} to retrieve all ingredients
     * stored on the server.
     *
     * @return a List of Ingredient objects returned by the server
     */
    public List<Ingredient> getIngredients() {
        return client.target(SERVER).path("api/ingredients") //
                .request(APPLICATION_JSON) //
                .get(new GenericType<List<Ingredient>>() {});
    }

    /**
     * Sends a POST request to {.../api/ingredients} to add an ingredient
     * and store it on the server.
     *
     * @param ingredient the Ingredient object to add
     * @return an Ingredient object returned by the server
     */
    public Ingredient addIngredient(Ingredient ingredient) {
        return client.target(SERVER).path("api/ingredients")
                .request(APPLICATION_JSON)
                .post(Entity.entity(ingredient, APPLICATION_JSON), Ingredient.class);
    }

    /**
     * Sends a PUT request to update an existing ingredient at {.../api/ingredients/{id}}.
     *
     * @param id The ID of the ingredient to update.
     * @param updatedIngredient The Ingredient object with updated fields.
     * @return The updated Ingredient object returned by the server.
     */
    public Ingredient updateIngredient(long id, Ingredient updatedIngredient) {
        return client.target(SERVER).path("api/ingredients/" + id)
                .request(APPLICATION_JSON)
                .put(Entity.entity(updatedIngredient, APPLICATION_JSON), Ingredient.class);
    }

    /**
     * Sends a DELETE request to remove an ingredient at {.../api/ingredients/{id}}.
     *
     * @param id The ID of the ingredient to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteIngredient(long id) {
        Response response = client
                .target(SERVER).path("api/ingredients/" + id)
                .request(APPLICATION_JSON)
                .delete();
        return response.getStatus() == 204;
    }

        //server availability request:

    /**
     * Checks whether the server is reachable by sending a simple GET request
     * to the root URL. If the connection fails due to a refused or timed-out
     * connection, this method returns false.
     *
     * @return true if the server responds, false if the connection fails
     */
    public boolean isServerAvailable() {
        try {
            client.target(SERVER)
                    .request(APPLICATION_JSON)
                    .get();
        } catch (ProcessingException e) {
            if (e.getCause() instanceof ConnectException) {
                return false;
            }
        }
        return true;
    }

}
