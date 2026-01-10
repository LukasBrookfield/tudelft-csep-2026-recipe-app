package server.services;

import commons.Ingredient;
import org.springframework.stereotype.Service;
import server.database.IngredientRepository;

@Service
public class IngredientService {
    private final IngredientRepository repo;

    public IngredientService(IngredientRepository repo) {
        this.repo = repo;
    }

    public boolean validateIngredient(Ingredient ingredient) {
        if (ingredient == null || ingredient.ingredientType == null) {
            return false;
        }
        return true;
    }

    public boolean validateUpdatedIngredient(long id, Ingredient updatedIngredient) {
        if (!validateIngredient(updatedIngredient)
                || id < 0 || !repo.existsById(id)) {
            return false;
        }
        return true;
    }

    public void transferFields(Ingredient fromIngredient, Ingredient toIngredient) {
        toIngredient.ingredientType = fromIngredient.ingredientType;
        toIngredient.amount = fromIngredient.amount;
        toIngredient.unit = fromIngredient.unit;
        toIngredient.recipe = fromIngredient.recipe;
    }
}
