package server.services;

import commons.Ingredient;
import commons.IngredientType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuantityNormalizationService {

    private final UnitConversionService unitConversionService;

    public QuantityNormalizationService(UnitConversionService unitConversionService) {
        this.unitConversionService = unitConversionService;
    }

    /**
     * Converts an ingredient amount to grams.
     * @param ingredient the one to convert
     * @return optional grams amount if convertible (else, empty optional)
     */
    public Optional<Double> ingredientToGrams(Ingredient ingredient) {
        if (ingredient == null || ingredient.amount == null || ingredient.unit == null) {
            return Optional.empty();
        }

        if (unitConversionService.isInformal(ingredient.unit)) {
            return Optional.empty();
        }

        // mass to grams
        var gramsOpt = unitConversionService.toGrams(ingredient.unit, ingredient.amount);
        if (gramsOpt.isPresent()) {
            return gramsOpt;
        }

        // volume to ml to grams
        var mlOpt = unitConversionService.toMl(ingredient.unit, ingredient.amount);
        if (mlOpt.isEmpty()) {
            return Optional.empty();
        }

        IngredientType type = ingredient.ingredientType;
        if (type == null || type.density == null || type.density <= 0) {
            return Optional.empty();
        }

        return Optional.of(mlOpt.get() * type.density);
    }
}
