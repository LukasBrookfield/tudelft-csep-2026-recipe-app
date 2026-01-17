package commons;


public record RecipeNutrition(
            double totalKcal,
            double totalGrams,
            double kcalPer100g,
            double proteinG,
            double fatG,
            double carbsG,
            int ignoredIngredients,
            char nutriScore
) {}
