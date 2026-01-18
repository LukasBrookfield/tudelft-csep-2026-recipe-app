package server.services;

import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.Unit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuantityNormalizationServiceTest {

    private final UnitConversionService unitConversion = new UnitConversionService();
    private final QuantityNormalizationService controller = new QuantityNormalizationService(unitConversion);

    @Test
    public void ingredientToGrams_massUnit_returnGrams() {
        var type = new IngredientType("Sugar", null, new ArrayList<>(), null);
        var ing = new Ingredient(type, 250.0, Unit.G, new Recipe("x", new ArrayList<>(), new ArrayList<>(), 1, "en"));

        var grams = controller.ingredientToGrams(ing);

        assertTrue(grams.isPresent());
        assertEquals(250.0, grams.get(), 1e-6);
    }

    @Test
    public void ingredientToGrams_informalUnit_returnsEmpty() {
        var type = new IngredientType("Sugar", null, new ArrayList<>(), null);
        var ing = new Ingredient(type, 250.0, Unit.TO_TASTE, new Recipe("x", new ArrayList<>(), new ArrayList<>(), 1, "en"));

        assertTrue(controller.ingredientToGrams(ing).isEmpty());
    }

    @Test
    public void ingredientToGrams_VolumeUnit_requiresDensity() {
        var type = new IngredientType("Olive oil", null, new ArrayList<>(), 0.91);
        var ing = new Ingredient(type, 1.0, Unit.TBSP, null);

        var grams = controller.ingredientToGrams(ing);

        assertTrue(grams.isPresent());

        assertEquals(14.787 * 0.91, grams.get(), 1e-6);
    }

    @Test
    public void ingredientToGrams_VolumeUnit_requiresDensity_returnsEmpty() {
        var type = new IngredientType("Olive oil", null, new ArrayList<>(), null);
        var ing = new Ingredient(type, 1.0, Unit.TBSP, null);

        assertTrue(controller.ingredientToGrams(ing).isEmpty());
    }

}
