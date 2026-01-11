package server.services;

import commons.Ingredient;
import commons.Unit;
import org.springframework.stereotype.Service;
import server.database.IngredientRepository;

@Service
public class IngredientService {
    private final IngredientRepository repo;

    /**
     * Constructor for the IngredientService class
     * @param repo the ingredient repository
     */
    public IngredientService(IngredientRepository repo) {
        this.repo = repo;
    }

    /**
     * Checks whether an ingredient is valid
     * @param ingredient the ingredient object to be checked
     * @return true if the ingredient is valid, false if not
     */
    public boolean validateIngredient(Ingredient ingredient) {
        if (ingredient == null || ingredient.ingredientType == null) {
            return false;
        }

        // All units other than 'To Taste' need an amount
        if (ingredient.unit != Unit.TO_TASTE && ingredient.amount == null) {
            return false;
        }
        // A 'To Taste' unit cannot have an amount
        if (ingredient.unit == Unit.TO_TASTE && ingredient.amount != null) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether an ingredient update is valid
     * @param id the id of the ingredient that should be updated
     * @param updatedIngredient the object that should replace the already existing
     *                      ingredient at {id}
     * @return true if the ingredient update is valid, false if not
     */
    public boolean validateUpdatedIngredient(long id, Ingredient updatedIngredient) {
        if (!validateIngredient(updatedIngredient)
                || id < 0 || !repo.existsById(id)) {
            return false;
        }
        return true;
    }

    /**
     * Transfers all fields from {fromIngredient} to {toIngredient}
     * @param fromIngredient the ingredient where the fields should be copied from
     * @param toIngredient the ingredient where the fields should be pasted to
     */
    public void transferFields(Ingredient fromIngredient, Ingredient toIngredient) {
        toIngredient.ingredientType = fromIngredient.ingredientType;
        toIngredient.amount = fromIngredient.amount;
        toIngredient.unit = fromIngredient.unit;
        toIngredient.recipe = fromIngredient.recipe;
    }
}
