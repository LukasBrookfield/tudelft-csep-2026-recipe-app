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
     * Receives a ingredient from the client via HTTP and saves it to the database.
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
                || ingredient.unit.isEmpty() || ingredient.amount <= 0){
            return ResponseEntity.badRequest().build();
        }

        Ingredient saved = repo.save(ingredient);
        return ResponseEntity.ok(saved);
    }
}
