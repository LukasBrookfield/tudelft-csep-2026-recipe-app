package server.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
