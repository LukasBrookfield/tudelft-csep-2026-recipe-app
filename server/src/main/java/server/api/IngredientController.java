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
     * Takes an ingredient and posts it
     * @param ingredient The ingredient
     * @return The ingredient which has been saved
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Ingredient> add(@RequestBody Ingredient ingredient) {

        //no attribute is null and the name is not empty
        if (ingredient.name == null || ingredient.name.isEmpty()
                || ingredient.amount == null || ingredient.unit == null){
            return ResponseEntity.badRequest().build();
        }

        Ingredient saved = repo.save(ingredient);
        return ResponseEntity.ok(saved);
    }
}
