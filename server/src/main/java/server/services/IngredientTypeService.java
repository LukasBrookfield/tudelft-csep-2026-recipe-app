package server.services;

import commons.IngredientType;
import org.springframework.stereotype.Service;
import server.database.IngredientTypeRepository;

@Service
public class IngredientTypeService {
    private final IngredientTypeRepository repo;

    /**
     * Constructor for the IngredientTypeService class
     * @param repo the ingredient type repository
     */
    public IngredientTypeService(IngredientTypeRepository repo) {
        this.repo = repo;
    }

    /**
     * Checks whether an ingredient type is valid
     * @param ingredientType the ingredient type object to be checked
     * @return true if the ingredient type is valid, false if not
     */
    public boolean validateIngredientType(IngredientType ingredientType) {
        if (ingredientType == null || ingredientType.name == null
                || ingredientType.name.isBlank()) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether an ingredient type update is valid
     * @param id the id of the ingredient type that should be updated
     * @param updatedIngredientType the object that should replace the already existing
     *                      ingredient type at {id}
     * @return true if the ingredient type update is valid, false if not
     */
    public boolean validateUpdatedIngredientType(long id,
                                                 IngredientType updatedIngredientType) {
        if (!validateIngredientType(updatedIngredientType)
                || id < 0 || !repo.existsById(id)) {
            return false;
        }
        return true;
    }

    /**
     * Transfers all fields from {fromIngredientType} to {toIngredientType}
     * @param fromIngredientType the ingredient type where the fields should be
     *                           copied from
     * @param toIngredientType the ingredient type where the fields should be
     *                         pasted to
     */
    public void transferFields(IngredientType fromIngredientType,
                               IngredientType toIngredientType) {
        toIngredientType.name = fromIngredientType.name;
        toIngredientType.nutrition = fromIngredientType.nutrition;
        toIngredientType.density = fromIngredientType.density;
    }
}
