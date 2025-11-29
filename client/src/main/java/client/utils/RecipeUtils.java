package client.utils;

import commons.Ingredient;
import commons.Unit;

public class RecipeUtils {
    public static final double CAL_PER_100G_CARB = 4.0;
    public static final double CAL_PER_100G_PROTEIN = 4.0;
    public static final double CAL_PER_100G_FAT = 9.0;

    /**
     * Uses the carbs, protein and fat content to calculate the calories per 100g.
     * If the unit isn't grams, return null.
     * @param i The ingredient to measure
     * @return The amount of calories per 100g in the ingredient
     */
    public double getCaloriesPer100g(Ingredient i) {
        if (i.unit != Unit.G || i.nutrition == null) return 0.0;
        double calories = 0.0;
        if (i.nutrition.carbs != null) calories += CAL_PER_100G_CARB * i.nutrition.carbs;
        if (i.nutrition.protein != null) calories += CAL_PER_100G_PROTEIN * i.nutrition.protein;
        if (i.nutrition.fat != null) calories += CAL_PER_100G_FAT * i.nutrition.fat;
        return calories;
    }
}
