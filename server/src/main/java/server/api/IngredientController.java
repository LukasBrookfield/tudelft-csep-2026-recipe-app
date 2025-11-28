package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.IngredientRepository;

import commons.Ingredient;

@RestController
@RequestMapping("api/ingredients")
public class IngredientController {
    private final IngredientRepository repo;

    /**
     * IngredientController constructor
     * @param repo The spring ingredient repository (connects to sql database)
     */
    public IngredientController(IngredientRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns all the ingredients in the database
     * @return List of ingredients
     */
    @GetMapping(path = { "", "/" })
    public List<Ingredient> getAllIngredients() {
        return repo.findAll();
    }

    /**
     * Inputs a numeric id and returns the ingredient in the database that has that id
     * @param id The id of the ingredient to get
     * @return The ingredient with the corresponding id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Receives an ingredient from the client via HTTP and saves it to the database.
     *
     * This method validates the incoming ingredient to ensure that:
     *
     *   The name is not null or empty.
     *   The amount is not null and greater than 0.
     *   The unit is not null or empty.
     *
     * If any validation fails, a 400 Bad Request response is returned.
     * Otherwise, the ingredient is saved to the repository and returned with a
     * 200 OK with the saved ingredient.
     *
     * @param ingredient the Ingredient object provided in the request body
     * @return a ResponseEntity containing the saved ingredient or an
     *         error response if validation fails
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Ingredient> add(@RequestBody Ingredient ingredient) {

        //no attribute is null and the name is not empty
        //the amount of the ingredient is larger than 0
        if (ingredient.name == null || ingredient.name.isEmpty()
                || ingredient.amount == null || ingredient.unit == null
                || ingredient.amount <= 0){
            return ResponseEntity.badRequest().build();
        }

        Ingredient saved = repo.save(ingredient);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes an ingredient in database by its ID.
     *
     * This method validates the incoming ingredient id to ensure that it exists.
     *
     * If the ID is valid, retrieves the ingredient from the repository.
     * Then deletes the ingredient from the repository.
     * Returns the deleted ingredient wrapped in a 200 OK response.
     *
     * @param id the ID of the ingredient to delete
     * @return ResponseEntity containing the deleted ingredient if successful,
     *         or a 400 Bad Request response if the ID is invalid or does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Ingredient> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }else{
            Ingredient deleted = repo.findById(id).get();
            repo.deleteById(id);
            return ResponseEntity.ok(deleted);
        }
    }

    /**
     * Updates an existing ingredient with the given ID.
     *
     * This method validates the incoming ingredient id and new ingredient.
     *
     *             Name must not be null or empty
     *             Amount must not be null and must be greater than 0
     *             Unit must not be null
     *             ID must be non-negative and exist in the repository
     *
     *  If validation fails, or the ID does not exist, returns a 400 Bad Request response.
     *  If the ID is valid, retrieves the existing ingredient from the repository,
     *  updates its fields, saves it, and returns the updated ingredient with 200 OK.
     *
     * @param id the ID of the ingredient to update
     * @param updatedIngredient the ingredient object containing the updated values
     * @return ResponseEntity containing the updated ingredient if successful,
     *         or a 400 Bad Request if validation fails or the ID is invalid/non-existent.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> update(@PathVariable("id") long id,
                                             @RequestBody Ingredient updatedIngredient) {
        //no attribute is null and the name is not empty
        //the amount of the ingredient is larger than 0
        if (updatedIngredient.name == null || updatedIngredient.name.isEmpty()
                || updatedIngredient.amount == null || updatedIngredient.unit == null
                || updatedIngredient.amount <= 0 || id < 0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }else{
            Ingredient ingredientToUpdated = repo.findById(id).get();
            ingredientToUpdated.name = updatedIngredient.name;
            ingredientToUpdated.amount = updatedIngredient.amount;
            ingredientToUpdated.unit = updatedIngredient.unit;
            Ingredient updated = repo.save(ingredientToUpdated);
            return ResponseEntity.ok(updated);
        }
    }
}
