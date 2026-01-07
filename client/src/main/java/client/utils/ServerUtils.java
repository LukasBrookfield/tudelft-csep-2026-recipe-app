package client.utils;

import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.RecipeNutrition;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.List;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    private final Client client;
    private static final String SERVER = "http://localhost:8080/";


    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    /**
     * Constructor
     *
     * @param client Client Object
     */
    @Inject
    public ServerUtils(Client client) {
        this.client = client;
    }

    /**
     * Do nothing if client is connected to the chanel
     * or create a websocket client and connect to a session
     */
    private void connectWebSocketIfNeeded() {

        if (stompSession != null && stompSession.isConnected()) {
            return;
        }

        try {
            // handles the basic WebSocket protocol handshake, upgrading the connection from HTTP to WebSocket
            StandardWebSocketClient webSocketClient = new StandardWebSocketClient();

            // wraps the raw webSocketClient and adds the logic necessary to frame messages using the STOMP protocol
            stompClient = new WebSocketStompClient(webSocketClient);

            // automatically serialize Java objects into JSON format when sending messages,
            // and deserialize incoming JSON back into Java objects
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            String wsUrl = SERVER.replace("http", "ws") + "websocket";

            // holds the STOMP session
            CompletableFuture<StompSession> connectFuture = stompClient.connectAsync(
                    wsUrl,
                    new StompSessionHandlerAdapter() {
                    }
            );
            stompSession = connectFuture.get();

        } catch (Exception e) {
            throw new RuntimeException("WebSocket connection failed", e);
        }
    }

    /**
     * Establishes a subscription to receive real-time updates whenever the
     * complete list of recipes changes
     *
     * @param listener A functional consumer that accepts the new, complete list of
     *                 Recipe objects whenever an update is broadcast by the server.
     */
    public void subscribeToRecipeList(Consumer<List<Recipe>> listener) {
        connectWebSocketIfNeeded();

        stompSession.subscribe("/topic/recipes/list", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Recipe[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Recipe[] array = (Recipe[]) payload;
                listener.accept(List.of(array));
            }
        });
    }

    public RecipeNutrition getRecipeNutrition(long id) {
        var target = client.target(SERVER).path("api/recipes/" + id + "/nutrition"); // Append this path to the base URL

        System.out.println("[client] GET " + target.getUri());
        try{
            return target    // Start building a request aimed at 'SERVER'
                    .request(new String[]{"application/json"})  // We want JSON back
                    .get(RecipeNutrition.class);    // gets info in JSON and turns into RecipeNutrition object
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while getting recipe nutrition", e);
        }
    }

    /**
     * Establishes a subscription to receive real-time updates whenever
     * a specific recipe changes
     *
     * @param id       The id of the recipe
     * @param listener A functional consumer that accepts the updated Recipe
     *                 whenever this specific recipe is broadcast by the server.
     */
    public void subscribeToRecipe(long id, Consumer<Recipe> listener) {
        connectWebSocketIfNeeded();

        stompSession.subscribe("/topic/recipes/" + id, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Recipe.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                listener.accept((Recipe) payload);
            }
        });
    }

    //recipes requests:

    /**
     * Sends a GET request to {.../api/recipes} to retrieve all recipes
     * stored on the server.
     *
     * @return a List of Recipe objects returned by the server
     */
    public List<Recipe> getRecipes() {
        try {
            return client.target(SERVER).path("api/recipes") //
                    .request(APPLICATION_JSON) //
                    .get(new GenericType<List<Recipe>>() {
                    });
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while loading recipes", e);
        }

    }

    /**
     * Sends a POST request to {.../api/recipes} to add a recipe
     * and store it on the server.
     *
     * @param recipe the Recipe object to add
     * @return a Recipe object returned by the server
     */
    public Recipe addRecipe(Recipe recipe) {
        try {
            return client.target(SERVER).path("api/recipes")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(recipe, APPLICATION_JSON), Recipe.class);
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while adding recipes", e);
        }
    }

    /**
     * Sends a PUT request to update an existing recipe at {.../api/recipes/{id}}.
     *
     * @param id            The ID of the recipe to update.
     * @param updatedRecipe The recipe object with updated fields.
     * @return The updated recipe object returned by the server.
     */
    public Recipe updateRecipe(long id, Recipe updatedRecipe) {
        try {
            return client.target(SERVER).path("api/recipes/" + id)
                    .request(APPLICATION_JSON)
                    .put(Entity.entity(updatedRecipe, APPLICATION_JSON), Recipe.class);
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while updating recipes", e);
        }
    }

    /**
     * Sends a DELETE request to remove a recipe at {.../api/recipes/{id}}.
     *
     * @param id The ID of the recipe to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteRecipe(long id) {
        try {
            Response response = client
                    .target(SERVER).path("api/recipes/" + id)
                    .request(APPLICATION_JSON)
                    .delete();
            return response.getStatus() == 204;
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while deleting recipes", e);
        }
    }

    //ingredients requests:

    /**
     * Sends a GET request to {.../api/ingredients} to retrieve all ingredients
     * stored on the server.
     *
     * @return a List of Ingredient objects returned by the server
     */
    public List<Ingredient> getIngredients() {
        try {
            return client.target(SERVER).path("api/ingredients") //
                    .request(APPLICATION_JSON) //
                    .get(new GenericType<List<Ingredient>>() {
                    });
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while getting ingredients", e);
        }
    }

    /**
     * Sends a POST request to {.../api/ingredients} to add an ingredient
     * and store it on the server.
     *
     * @param ingredient the Ingredient object to add
     * @return an Ingredient object returned by the server
     */
    public Ingredient addIngredient(Ingredient ingredient) {
        try {
            return client.target(SERVER).path("api/ingredients")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(ingredient, APPLICATION_JSON), Ingredient.class);
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while adding ingredient to recipes", e);
        }
    }

    /**
     * Sends a PUT request to update an existing ingredient at {.../api/ingredients/{id}}.
     *
     * @param id                The ID of the ingredient to update.
     * @param updatedIngredient The Ingredient object with updated fields.
     * @return The updated Ingredient object returned by the server.
     */
    public Ingredient updateIngredient(long id, Ingredient updatedIngredient) {
        try {
            return client.target(SERVER).path("api/ingredients/" + id)
                    .request(APPLICATION_JSON)
                    .put(Entity.entity(updatedIngredient, APPLICATION_JSON), Ingredient.class);
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while updating ingredient to recipes", e);
        }
    }

    /**
     * Sends a DELETE request to remove an ingredient at {.../api/ingredients/{id}}.
     *
     * @param id The ID of the ingredient to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteIngredient(long id) {
        try {
            Response response = client
                    .target(SERVER).path("api/ingredients/" + id)
                    .request(APPLICATION_JSON)
                    .delete();
            return response.getStatus() == 204;
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while deleting ingredient to recipes", e);
        }
    }

    //IngredientType requests

    /**
     * Sends a GET request to {.../api/ingredientTypes} to retrieve all ingredientTypes
     * stored on the server.
     *
     * @return a List of IngredientTypes objects returned by the server
     */
    public List<IngredientType> getIngredientTypes() {
        try {
            return client.target(SERVER).path("api/ingredientTypes") //
                    .request(APPLICATION_JSON) //
                    .get(new GenericType<List<IngredientType>>() {
                    });
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while getting ingredientTypes", e);
        }
    }

    /**
     * Sends a POST request to {.../api/ingredientTypes} to add an ingredientType
     * and store it on the server.
     *
     * @param ingredientType the IngredientType object to add
     * @return an IngredientType object returned by the server
     */
    public IngredientType addIngredientType(IngredientType ingredientType) {
        try {
            return client.target(SERVER).path("api/ingredientTypes")
                    .request(APPLICATION_JSON)
                    .post(Entity.entity(ingredientType, APPLICATION_JSON), IngredientType.class);
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while adding ingredientType", e);
        }
    }

    /**
     * Sends a PUT request to update an existing ingredientType at {.../api/ingredientTypes/{id}}.
     *
     * @param id                The ID of the ingredientType to update.
     * @param updatedIngredientType The IngredientType object with updated fields.
     * @return The updated IngredientType object returned by the server.
     */
    public IngredientType updateIngredientType(long id, IngredientType updatedIngredientType) {
        try {
            return client.target(SERVER).path("api/ingredientTypes/" + id)
                    .request(APPLICATION_JSON)
                    .put(Entity.entity(updatedIngredientType, APPLICATION_JSON), IngredientType.class);
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while updating ingredientType", e);
        }
    }

    /**
     * Sends a DELETE request to remove an ingredientType at {.../api/ingredientTypes/{id}}.
     *
     * @param id The ID of the ingredientType to delete.
     * @return boolean corresponding to the server response status
     * {204 -> request was successful and the response body is empty}
     */
    public boolean deleteIngredientType(long id) {
        try {
            Response response = client
                    .target(SERVER).path("api/ingredientTypes/" + id)
                    .request(APPLICATION_JSON)
                    .delete();
            return response.getStatus() == 204;
        } catch (ProcessingException e) {
            throw new RuntimeException("Could not reach server while deleting ingredientType", e);
        }
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
