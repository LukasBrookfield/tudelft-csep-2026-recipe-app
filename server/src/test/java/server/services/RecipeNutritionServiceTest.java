package server.services;

import commons.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeNutritionServiceTest {

    private final UnitConversionService unitConversion = new UnitConversionService();
    private final QuantityNormalizationService normalizer = new QuantityNormalizationService(unitConversion);
    private final RecipeNutritionService controller = new RecipeNutritionService(normalizer);

    @Test
    public void compute_withOneConvertibleIngredient_returnsKcalPer100g() {

        Nutrition nutrition = new Nutrition(10.0, 20.0, 5.0);
        IngredientType chicken = new IngredientType("Chicken", nutrition, new ArrayList<>(), null);

        Recipe r = new Recipe("Chicken bowl", new ArrayList<>(), new ArrayList<>(List.of("add chicken")), 1);

        Ingredient ing = new Ingredient(chicken, 100.0, Unit.G, r);
        r.ingredients.add(ing);

        RecipeNutrition n = controller.compute(r);

        assertTrue(n.totalGrams() > 0);
        assertEquals(100.0, n.totalGrams(), 1e-6);

        assertEquals(165.0, n.totalKcal(), 1e-6);

        assertEquals(0, n.ignoredIngredients());
    }

    @Test
    public void compute_ignoresInformalAmounts() {
        Nutrition nutrition = new Nutrition(100.0, 0.0, 0.0);
        IngredientType sugar = new IngredientType("Sugar", nutrition, new ArrayList<>(), null);

        Recipe r = new Recipe("Tea", new ArrayList<>(), new ArrayList<>(List.of("add sugar")), 1);
        r.ingredients.add(new Ingredient(sugar, 1.0, Unit.TO_TASTE, r));

        RecipeNutrition n = controller.compute(r);

        assertEquals(0.0, n.totalGrams(), 1e-6);
        assertEquals(0.0, n.kcalPer100g(), 1e-6);
        assertEquals(1, n.ignoredIngredients());
    }

    @Test
    public void compute_ignoresIngredientsWithoutNutrition() {
        IngredientType unkown = new IngredientType("Unknown", null, new ArrayList<>(), null);
        Recipe r = new Recipe("Mistery soup", new ArrayList<>(), new ArrayList<>(), 1);
        r.ingredients.add(new Ingredient(unkown, 1.0, Unit.TO_TASTE, r));

        RecipeNutrition n = controller.compute(r);

        assertEquals(0.0, n.totalGrams(), 1e-6);
        assertEquals(1, n.ignoredIngredients());
    }

}
