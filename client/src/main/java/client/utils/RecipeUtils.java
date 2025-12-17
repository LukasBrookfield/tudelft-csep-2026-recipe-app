package client.utils;

import commons.Ingredient;
import commons.IngredientType;
import commons.Unit;

import java.util.List;

public class RecipeUtils {
    public static final double CAL_PER_GRAM_CARB = 4.0;
    public static final double CAL_PER_GRAM_PROTEIN = 4.0;
    public static final double CAL_PER_GRAM_FAT = 9.0;

    /**
     * Uses the carbs, protein and fat content to calculate the calories per 100g.
     * If the unit isn't grams or there is no nutritional info, return 0.
     * @param i The ingredient type to measure
     * @return The amount of calories per 100g in the ingredient
     */
    public double getCaloriesPer100g(IngredientType i) {
        if (i.nutrition == null) return 0.0;
        double calories = 0.0;
        if (i.nutrition.carbs != null) calories += CAL_PER_GRAM_CARB * i.nutrition.carbs;
        if (i.nutrition.protein != null) calories += CAL_PER_GRAM_PROTEIN * i.nutrition.protein;
        if (i.nutrition.fat != null) calories += CAL_PER_GRAM_FAT * i.nutrition.fat;
        return calories;
    }

    /**
     * Normalize the amount and the unit of ingredients exceeding 1000 units for G and ML
     * @param ingredients The list of ingredients
     */
    public void normalizeIngredients(List<Ingredient> ingredients) {
        for (Ingredient ing : ingredients) {
            if (!(ing == null || ing.amount == null || ing.unit == null)) {
                if (ing.amount >= 1000) {
                    if (ing.unit == Unit.ML) {
                        ing.amount /= 1000.0;
                        ing.unit = Unit.L;
                    } else if (ing.unit == Unit.G) {
                        ing.amount /= 1000.0;
                        ing.unit = Unit.KG;
                    }
                }
            }
        }
    }
}
