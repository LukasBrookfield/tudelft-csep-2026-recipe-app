package commons;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {
    static final String HAMBURGER = "hamburger";
    Recipe recipe = new Recipe(HAMBURGER, Collections.emptyList(), Collections.emptyList(), 1);

    @Test
    void testEquals() {
        // tests if equal
        Recipe recipe2 = new Recipe(HAMBURGER, Collections.emptyList(), Collections.emptyList(), 1);
        assertEquals(recipe, recipe2);

        // tests for different names
        recipe2 = new Recipe("gormless", Collections.emptyList(), Collections.emptyList(), 1);
        assertNotEquals(recipe, recipe2);

        // tests for different steps
        List<String> steps = List.of("step1", "step2", "step3");
        recipe2 = new Recipe(HAMBURGER, Collections.emptyList(), steps, 1);
        assertNotEquals(recipe, recipe2);

        // tests for different ingredients
        Ingredient ingredient = new Ingredient(null, 3.0, Unit.TBSP, null);
        recipe2 = new Recipe(HAMBURGER, List.of(ingredient), Collections.emptyList(), 1);
        assertNotEquals(recipe, recipe2);

        // tests for different servings
        recipe2 = new Recipe(HAMBURGER, Collections.emptyList(), Collections.emptyList(), 3);
        assertNotEquals(recipe, recipe2);
    }

    @Test
    void testHashCode() {
        Recipe recipe2 = new Recipe(HAMBURGER, Collections.emptyList(), Collections.emptyList(), 1);
        assertEquals(recipe.hashCode(), recipe2.hashCode());

        recipe2 = new Recipe("gormless", Collections.emptyList(), Collections.emptyList(), 1);
        assertNotEquals(recipe.hashCode(), recipe2.hashCode());

        List<String> steps = List.of("step1", "step2", "step3");
        recipe2 = new Recipe(HAMBURGER, Collections.emptyList(), steps, 1);
        assertNotEquals(recipe.hashCode(), recipe2.hashCode());

        Ingredient ingredient = new Ingredient(null, 3.0, Unit.TBSP, null);
        recipe2 = new Recipe(HAMBURGER, List.of(ingredient), Collections.emptyList(), 1);
        assertNotEquals(recipe.hashCode(), recipe2.hashCode());

        recipe2 = new Recipe(HAMBURGER, Collections.emptyList(), Collections.emptyList(), 3);
        assertNotEquals(recipe.hashCode(), recipe2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(HAMBURGER, recipe.toString());
    }
}