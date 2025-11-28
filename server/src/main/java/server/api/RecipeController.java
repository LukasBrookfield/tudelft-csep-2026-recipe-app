package server.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Recipe;
import server.database.RecipeRepository;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeRepository repo;

    /**
     * RecipeController constructor
     * @param repo The spring recipe repository (connects to sql database)
     */
    public RecipeController(RecipeRepository repo) {
        this.repo = repo;
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
     * Receives a recipe from the client via HTTP and saves it to the database.
     *
     * This method validates the incoming recipe to ensure that:
     *
     *   The recipe name is not null or empty.
     *   The ingredient list is not null or empty.
     *   The steps list is not null or empty.
     *
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

        //lists and name are not null and not empty
        if (recipe.name == null || recipe.name.isEmpty() || recipe.ingredients == null ||
                recipe.ingredients.isEmpty() || recipe.steps == null || recipe.steps.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Recipe saved = repo.save(recipe);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a recipe in database by its ID.
     *
     * This method validates the incoming recipe id to ensure that it exists.
     *
     * If the ID is valid, retrieves the recipe from the repository.
     * Then deletes the recipe from the repository.
     * Returns the deleted recipe wrapped in a 200 OK response.
     *
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
            return ResponseEntity.ok(result);
        }
    }

    /**
     * Updates an existing recipe with the given ID.
     *
     * This method validates the incoming recipe id and new recipe.
     *
     *             Name must not be null or empty
     *             Ingredients list must not be null or empty
     *             Steps list must not be null or empty
     *
     *     Checks that a recipe with the given ID exists in the repository.
     *     If validation fails or the ID is invalid/non-existent,
     *     returns a 400 Bad Request response.
     *     If the ID is valid, retrieves the existing recipe,
     *     updates its fields with the new values,
     *     saves it in the repository, and returns the updated recipe in the response with 200 OK.
     *
     * @param id the ID of the recipe to update
     * @param updatedRecipe the recipe object containing updated values
     * @return ResponseEntity containing the updated recipe if successful,
     *         or a 400 Bad Request response if validation fails or the ID is invalid/non-existent
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> update(@PathVariable("id") long id,
                                         @RequestBody Recipe updatedRecipe) {
        //lists and name are not null and not empty
        if (updatedRecipe.name == null || updatedRecipe.name.isEmpty() ||
                updatedRecipe.ingredients == null || updatedRecipe.ingredients.isEmpty() ||
                updatedRecipe.steps == null || updatedRecipe.steps.isEmpty() ||
                id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }else{
            Recipe recipeToUpdate = repo.findById(id).get();
            recipeToUpdate.name = updatedRecipe.name;

            // Replace collections safely
            recipeToUpdate.ingredients.clear();
            recipeToUpdate.ingredients.addAll(updatedRecipe.ingredients);

            recipeToUpdate.steps.clear();
            recipeToUpdate.steps.addAll(updatedRecipe.steps);

            Recipe updated = repo.save(recipeToUpdate);
            return ResponseEntity.ok(updated);
        }
    }
}
