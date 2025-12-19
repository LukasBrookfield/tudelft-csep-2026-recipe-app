package commons;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    IngredientType type3 = new IngredientType("chicken", null, Collections.emptyList(), null);
    Ingredient ingredient = new Ingredient(type3, 5.0, Unit.TSP, null);

    @Test
    void testEquals() {
        // tests for equal objects
        Ingredient ingredient2 = new Ingredient(type3, 5.0, Unit.TSP, null);
        assertEquals(ingredient, ingredient2);

        // tests for different ingredient types
        IngredientType type4 = new IngredientType("pork", null, Collections.emptyList(), 3.0);
        ingredient2 = new Ingredient(type4, 5.0, Unit.TSP, null);
        assertNotEquals(ingredient, ingredient2);

        // tests for different amounts
        ingredient2 = new Ingredient(type3, 7.0, Unit.TSP, null);
        assertNotEquals(ingredient, ingredient2);

        // tests for different units
        ingredient2 = new Ingredient(type3, 5.0, Unit.KG, null);
        assertNotEquals(ingredient, ingredient2);
    }

    @Test
    void testHashCode() {
        // tests for equal objects
        Ingredient ingredient2 = new Ingredient(type3, 5.0, Unit.TSP, null);
        assertEquals(ingredient.hashCode(), ingredient2.hashCode());

        // tests for different ingredient types
        IngredientType type4 = new IngredientType("pork", null, Collections.emptyList(), 3.0);
        ingredient2 = new Ingredient(type4, 5.0, Unit.TSP, null);
        assertNotEquals(ingredient.hashCode(), ingredient2.hashCode());

        // tests for different amounts
        ingredient2 = new Ingredient(type3, 7.0, Unit.TSP, null);
        assertNotEquals(ingredient.hashCode(), ingredient2.hashCode());

        // tests for different units
        ingredient2 = new Ingredient(type3, 5.0, Unit.KG, null);
        assertNotEquals(ingredient.hashCode(), ingredient2.hashCode());
    }

    @Test
    void testToString() {
        ingredient = new Ingredient(null, null, null, null);
        assertEquals("New ingredient", ingredient.toString());

        ingredient = new Ingredient(type3, null, null, null);
        assertEquals("chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 3.0, null, null);
        assertEquals("3.0 chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 1.0, Unit.G, null);
        assertEquals("1.0G chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 3.0, Unit.ML, null);
        assertEquals("3.0ML chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 2.0, Unit.KG, null);
        assertEquals("2.0KG chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 1.0, Unit.L, null);
        assertEquals("1.0L chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 1.0, Unit.TBSP, null);
        assertEquals("1 tablespoon of chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 3.0, Unit.TBSP, null);
        assertEquals("3.0 tablespoons of chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 1.0, Unit.TSP, null);
        assertEquals("1 teaspoon of chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 3.0, Unit.TSP, null);
        assertEquals("3.0 teaspoons of chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 1.0, Unit.PINCH, null);
        assertEquals("A pinch of chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 3.0, Unit.PINCH, null);
        assertEquals("3.0 pinches of chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 1.0, Unit.HANDFUL, null);
        assertEquals("A handful of chicken", ingredient.toString());
        ingredient = new Ingredient(type3, 3.0, Unit.HANDFUL, null);
        assertEquals("3.0 handfuls of chicken", ingredient.toString());

        ingredient = new Ingredient(type3, 1.0, Unit.TO_TASTE, null);
        assertEquals("chicken to taste", ingredient.toString());
        ingredient = new Ingredient(type3, 3.0, Unit.TO_TASTE, null);
        assertEquals("chicken to taste", ingredient.toString());
    }
}