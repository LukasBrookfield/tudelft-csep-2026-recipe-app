package server.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import commons.Recipe;
import server.database.RecipeRepository;
import server.services.RecipeNutritionService;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeRepository repo;
    private final SimpMessagingTemplate messagingTemplate;
    private final RecipeNutritionService recipeNutritionService;

    /**
     * RecipeController constructor
     * @param repo The spring recipe repository (connects to sql database)
     * @param messagingTemplate The messaging template
     * sending data to the URL which the clients are subscribed to
     */
    public RecipeController(RecipeRepository repo, SimpMessagingTemplate messagingTemplate, RecipeNutritionService recipeNutritionService) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
        this.recipeNutritionService = recipeNutritionService;
    }

    /**
     * Returns all the recipes in the database
     * @return List of recipes
     */
    @GetMapping(path = { "", "/" })
    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }

    /**
     * Inputs a numeric id and returns the recipe in the database that has that id
     * @param id The id of the recipe to get
     * @return The recipe with the corresponding id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Inputs a numeric id and returns the recipe in the database that has that id
     * @param id The id of the recipe to get
     * @return The recipe with the corresponding id
     */
    @GetMapping("/{id}/Nutrition")
    public ResponseEntity<commons.RecipeNutrition> getNutrition(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        var recipe = repo.findById(id).get();
        return ResponseEntity.ok(recipeNutritionService.compute(recipe));
    }

    /**
     * Receives a recipe from the client via HTTP and saves it to the database.
     * This method validates the incoming recipe to ensure that:
     *     The recipe name is not null or empty.
     *     The ingredient list is not null.
     *     The steps list is not null.
     * If any of these validation checks fail, a 400 Bad Request response
     * is returned. Otherwise, the recipe is saved to the repository and returned
     * with a 200 OK response.
     *
     * @param recipe the Recipe provided in the request body
     * @return a ResponseEntity containing the saved recipe or an error
     *         response if validation fails
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Recipe> add(@RequestBody Recipe recipe) {
        if (recipe.name == null
                || recipe.name.isEmpty()
                || recipe.ingredients == null
                || recipe.steps == null) {
            return ResponseEntity.badRequest().build();
        }

        Recipe saved = repo.save(recipe);

        broadcastList(); // Subscribed clients get the new updated list
        broadcastSingle(saved); // Subscribed clients get the updated recipe
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a recipe in database by its ID.
     * This method validates the incoming recipe id to ensure that it exists.
     * If the ID is valid, retrieves the recipe from the repository.
     * Then deletes the recipe from the repository.
     * Returns the deleted recipe wrapped in a 200 OK response.
     * @param id the ID of the recipe to delete
     * @return ResponseEntity containing the deleted recipe if successful,
     *         or a 400 Bad Request response if the ID is invalid or does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Recipe> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }else{
            var result = repo.findById(id).get();
            repo.deleteById(id);

            broadcastList(); // Subscribed clients get the new updated list

            return ResponseEntity.ok(result);
        }
    }

    /**
     * Updates an existing recipe with the given ID.
     * This method validates the incoming recipe id and new recipe.
     *     Name must not be null or empty
     *     Ingredients list must not be null
     *     Steps list must not be null
     *     Checks that a recipe with the given ID exists in the repository.
     * If validation fails or the ID is invalid/non-existent,
     * returns a 400 Bad Request response.
     * If the ID is valid, retrieves the existing recipe,
     * updates its fields with the new values,
     * saves it in the repository, and returns the updated recipe in the response with 200 OK.
     *
     * @param id the ID of the recipe to update
     * @param updatedRecipe the recipe object containing updated values
     * @return ResponseEntity containing the updated recipe if successful,
     *         or a 400 Bad Request response if validation fails or the ID is invalid/non-existent
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> update(@PathVariable("id") long id,
                                         @RequestBody Recipe updatedRecipe) {
        if (updatedRecipe == null
                || updatedRecipe.name == null
                || updatedRecipe.name.isEmpty()
                || updatedRecipe.ingredients == null
                || updatedRecipe.steps == null
                || id < 0
                || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        } else {
            Recipe recipeToUpdate = repo.findById(id).get();
            recipeToUpdate.name = updatedRecipe.name;

            // Replace collections safely
            recipeToUpdate.ingredients.clear();
            recipeToUpdate.ingredients.addAll(updatedRecipe.ingredients);

            recipeToUpdate.steps.clear();
            recipeToUpdate.steps.addAll(updatedRecipe.steps);

            recipeToUpdate.servings = updatedRecipe.servings;

            Recipe updated = repo.save(recipeToUpdate);

            broadcastList(); // Subscribed clients get the new updated list
            broadcastSingle(recipeToUpdate); // Subscribed clients get the new updated recipe

            return ResponseEntity.ok(updated);
        }
    }

    /**
     * Gets all the recipes from the DB,
     * serializes the objects into a JSON format,
     * wraps the converted payload in a STOMP MESSAGE frame and
     * forwards it to the broker for distribution to the list URL
     */
    private void broadcastList() {
        List<Recipe> all = repo.findAll();
        // Every client that is subscribed to "/topic/recipes/list" gets the new full list
        messagingTemplate.convertAndSend("/topic/recipes/list", all);
    }
    /**
     * Uses the passed recipe,
     * serializes the object into a JSON format,
     * wraps the converted payload in a STOMP MESSAGE frame and
     * forwards it to the broker for distribution to the id URL
     */
    private void broadcastSingle(Recipe recipe) {
        if(recipe !=null && recipe.id > 0) {
            // Every client that is subscribed to "/topic/recipes/{id}" gets this updated recipe
            messagingTemplate.convertAndSend("/topic/recipes/" + recipe.id, recipe);
        }
    }
}
