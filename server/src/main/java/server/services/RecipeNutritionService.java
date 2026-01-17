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
            return new RecipeNutrition(0, 0, 0, 0, 0, 0, 0, ' ');
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

        return new RecipeNutrition(
                totalKcal,
                totalGrams,
                kcalPer100g,
                proteinG,
                fatG,
                carbsG,
                ignored,
                calculateNutriScore(kcalPer100g, proteinG, fatG, carbsG));
    }

    /**
     * Calculates a nutri-score from A-E based on the protein, fat and carbs in the recipe
     * @param kcalPer100g energy density of recipe
     * @param proteinG grams of protein in recipe
     * @param fatG grams of fat in recipe
     * @param carbsG grams of carbs in recipe
     * @return A single character from {A, B, C, D, E}, or ' ' if there is no nutritional info
     */
    private char calculateNutriScore(double kcalPer100g,
                                     double proteinG,
                                     double fatG,
                                     double carbsG) {
        if (kcalPer100g == 0.0) return ' ';
        double score = (2.0 * proteinG) - carbsG - (1.5 * fatG) - (0.02 * kcalPer100g);
        double scoreRestricted = Math.max(0, Math.min(100, score));
        if (scoreRestricted >= 80) return 'A';
        if (scoreRestricted >= 65) return 'B';
        if (scoreRestricted >= 50) return 'C';
        if (scoreRestricted >= 35) return 'D';
        return 'E';
    }
}
