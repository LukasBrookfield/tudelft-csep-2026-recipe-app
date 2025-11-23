package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * @return List<Ingredient>
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
}
