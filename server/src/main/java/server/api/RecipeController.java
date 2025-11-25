package server.api;

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
}
