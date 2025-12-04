package server.api;

import java.util.List;

import commons.IngredientType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.IngredientTypeRepository;

@RestController
@RequestMapping("api/ingredientTypes")
public class IngredientTypeController {
    private final IngredientTypeRepository repo;

    /**
     * IngredientTypeController constructor
     * @param repo The spring IngredientType repository (connects to sql database)
     */
    public IngredientTypeController(IngredientTypeRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns all the ingredients in the database
     * @return List of ingredientTypes
     */
    @GetMapping(path = { "", "/" })
    public List<IngredientType> getAllIngredients() {
        return repo.findAll();
    }

    /**
     * Inputs a numeric id and returns the ingredientType in the database that has that id
     * @param id The id of the ingredientType to get
     * @return The ingredientType with the corresponding id
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientType> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Receives an ingredientType from the client via HTTP and saves it to the database.
     * If any validation fails, a 400 Bad Request response is returned.
     * Otherwise, the ingredientType is saved to the repository and returned with a
     * 200 OK with the saved ingredient.
     * @param ingredientType the Ingredient object provided in the request body
     * @return a ResponseEntity containing the saved ingredient or an
     *         error response if validation fails
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<IngredientType> add(@RequestBody IngredientType ingredientType) {
        if (ingredientType.name == null || ingredientType.name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        IngredientType saved = repo.save(ingredientType);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes an ingredientType in database by its ID.
     * This method validates the incoming ingredientType id to ensure that it exists.
     * If the ID is valid, retrieves the ingredientType from the repository.
     * Then deletes the ingredientType from the repository.
     * Returns the deleted ingredient wrapped in a 200 OK response.
     * @param id the ID of the ingredientType to delete
     * @return ResponseEntity containing the deleted ingredient if successful,
     *         or a 400 Bad Request response if the ID is invalid or does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<IngredientType> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        } else {
            IngredientType deleted = repo.findById(id).get();
            repo.deleteById(id);
            return ResponseEntity.ok(deleted);
        }
    }

    /**
     * Updates an existing ingredientType with the given ID.
     * This method validates the incoming ingredientType id and new ingredientType.
     * If validation fails, or the ID does not exist, returns a 400 Bad Request response.
     * If the ID is valid, retrieves the existing ingredientType from the repository,
     * updates its fields, saves it, and returns the updated ingredient with 200 OK.
     * @param id the ID of the ingredientType to update
     * @param updatedIngredientType the ingredientType object containing the updated values
     * @return ResponseEntity containing the updated ingredient if successful,
     *         or a 400 Bad Request if validation fails or the ID is invalid/non-existent.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientType> update(@PathVariable("id") long id,
                                             @RequestBody IngredientType updatedIngredientType) {
        if (updatedIngredientType.name == null || updatedIngredientType.name.isEmpty()
                || id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        } else {
            IngredientType ingredientTypeToUpdate = repo.findById(id).get();

            // update fields
            ingredientTypeToUpdate.name = updatedIngredientType.name;
            ingredientTypeToUpdate.nutrition = updatedIngredientType.nutrition;
            ingredientTypeToUpdate.ingredients.clear();
            ingredientTypeToUpdate.ingredients.addAll(updatedIngredientType.ingredients);
            ingredientTypeToUpdate.density = updatedIngredientType.density;

            IngredientType updated = repo.save(ingredientTypeToUpdate);
            return ResponseEntity.ok(updated);
        }
    }
}
