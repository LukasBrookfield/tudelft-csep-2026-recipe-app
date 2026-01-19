package server.services;

import commons.Ingredient;
import commons.Recipe;
import org.springframework.stereotype.Service;
import server.database.RecipeRepository;

@Service
public class RecipeService {
    private final RecipeRepository repo;
    private final IngredientService ingredientService;

    /**
     * Constructor for the RecipeService class
     * @param repo the recipe repository
     */
    public RecipeService(RecipeRepository repo,
                         IngredientService ingredientService) {
        this.repo = repo;
        this.ingredientService = ingredientService;
    }

    /**
     * Checks whether a recipe is valid
     * @param recipe the recipe object to be checked
     * @return true if the recipe is valid, false if not
     */
    public boolean validateRecipe(Recipe recipe) {
        if (recipe == null || recipe.name == null || recipe.name.isBlank()
                || recipe.ingredients == null || recipe.steps == null) {
            return false;
        }

        // Check whether all the recipe's ingredients are valid
        for (Ingredient ingredient : recipe.ingredients) {
            if (!ingredientService.validateIngredient(ingredient)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether a recipe update is valid
     * @param id the id of the recipe that should be updated
     * @param updatedRecipe the object that should replace the already existing
     *                      recipe at {id}
     * @return true if the recipe update is valid, false if not
     */
    public boolean validateUpdatedRecipe(long id, Recipe updatedRecipe) {
        if (!validateRecipe(updatedRecipe) || id < 0 || !repo.existsById(id)) {
            return false;
        }
        return true;
    }

    /**
     * Transfers all fields from {fromRecipe} to {toRecipe}
     * @param fromRecipe the recipe where the fields should be copied from
     * @param toRecipe the recipe where the fields should be pasted to
     */
    public void transferFields(Recipe fromRecipe, Recipe toRecipe) {
        toRecipe.name = fromRecipe.name;

        // Replace collections safely
        toRecipe.ingredients.clear();
        toRecipe.ingredients.addAll(fromRecipe.ingredients);

        toRecipe.steps.clear();
        toRecipe.steps.addAll(fromRecipe.steps);

        toRecipe.servings = fromRecipe.servings;
        toRecipe.language = fromRecipe.language;
    }
}
