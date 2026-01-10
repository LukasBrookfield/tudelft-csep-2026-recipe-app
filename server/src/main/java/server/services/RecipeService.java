package server.services;

import commons.Recipe;
import org.springframework.stereotype.Service;
import server.database.RecipeRepository;

@Service
public class RecipeService {
    private final RecipeRepository repo;

    public RecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    public boolean validateRecipe(Recipe recipe) {
        if (recipe == null || recipe.name == null || recipe.name.isBlank()
                || recipe.ingredients == null || recipe.steps == null) {
            return false;
        }
        return true;
    }

    public boolean validateUpdatedRecipe(long id, Recipe updatedRecipe) {
        if (!validateRecipe(updatedRecipe) || id < 0 || !repo.existsById(id)) {
            return false;
        }
        return true;
    }

    public void transferFields(Recipe fromRecipe, Recipe toRecipe) {
        toRecipe.name = fromRecipe.name;

        // Replace collections safely
        toRecipe.ingredients.clear();
        toRecipe.ingredients.addAll(fromRecipe.ingredients);

        toRecipe.steps.clear();
        toRecipe.steps.addAll(fromRecipe.steps);

        toRecipe.servings = fromRecipe.servings;
    }
}
