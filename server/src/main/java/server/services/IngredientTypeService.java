package server.services;

import commons.IngredientType;
import org.springframework.stereotype.Service;
import server.database.IngredientTypeRepository;

@Service
public class IngredientTypeService {
    private final IngredientTypeRepository repo;

    public IngredientTypeService(IngredientTypeRepository repo) {
        this.repo = repo;
    }

    public boolean validateIngredientType(IngredientType ingredientType) {
        if (ingredientType == null || ingredientType.name == null
                || ingredientType.name.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean validateUpdatedIngredientType(long id,
                                                 IngredientType updatedIngredientType) {
        if (!validateIngredientType(updatedIngredientType)
                || id < 0 || !repo.existsById(id)) {
            return false;
        }
        return true;
    }

    public void transferFields(IngredientType fromIngredientType,
                               IngredientType toIngredientType) {
        toIngredientType.name = fromIngredientType.name;
        toIngredientType.nutrition = fromIngredientType.nutrition;
        toIngredientType.density = fromIngredientType.density;
    }
}
