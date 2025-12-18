package server.services;

import commons.Ingredient;
import commons.Nutrition;
import commons.Recipe;
import commons.RecipeNutrition;
import org.springframework.stereotype.Service;

@Service
public class RecipeNutritionService {

    private final QuantityNormalizationService quantityNormalizationService;

    public RecipeNutritionService(QuantityNormalizationService quantityNormalizationService) {
        this.quantityNormalizationService = quantityNormalizationService;
    }

    public RecipeNutrition compute(Recipe recipe) {
        if (recipe == null || recipe.ingredients == null) {
            return new RecipeNutrition(0, 0, 0, 0, 0, 0, 0);
        }

        double totalGrams = 0;
        double proteinG = 0;
        double fatG = 0;
        double carbsG = 0;
        int ignored = 0;

        for (Ingredient ing : recipe.ingredients) {
            var gramsOpt = quantityNormalizationService.ingredientToGrams(ing);
            if (gramsOpt.isEmpty()) {
                ignored++;
                continue;
            }

            Nutrition n = (ing != null && ing.ingredientType != null) ? ing.ingredientType.nutrition : null;
            if (n == null) {
                ignored++;
                continue;
            }

            if (n.protein == null || n.fat == null || n.carbs == null) {
                ignored++;
                continue;
            }

            double grams = gramsOpt.get();

            proteinG += grams * (n.protein / 100.0);
            fatG += grams * (n.fat / 100.0);
            carbsG += grams * (n.carbs / 100.0);
            totalGrams += grams;

        }

        double totalKcal = 4.0 * proteinG + 9.0 * fatG + 4.0 * carbsG;
        double kcalPer100g = totalGrams > 0 ? (totalKcal / totalGrams) * 100.0 : 0.0;

        return new RecipeNutrition(totalKcal, totalGrams, kcalPer100g, proteinG, fatG, carbsG, ignored);

    }
}
