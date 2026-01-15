package commons;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTypeTest {
    Nutrition n1 = new Nutrition(1.0, 2.0, 3.0);

    //    List<Ingredient> ingredients = createIngredientList();
    IngredientType typeA = new IngredientType("A", n1, Collections.emptyList(), 1.0);

    @Test
    void testEquals() {
        // tests for equal objects
        IngredientType typeB = new IngredientType("A", n1, Collections.emptyList(), 1.0);
        assertEquals(typeA, typeB);

        // tests for different nutrition
        Nutrition n2 = new Nutrition(2.0, 4.0, 6.0);
        typeB = new IngredientType("A", n2, Collections.emptyList(), 1.0);
        assertNotEquals(typeA, typeB);

        // tests for different name
        typeB = new IngredientType("b", n1, Collections.emptyList(), 1.0);
        assertNotEquals(typeA, typeB);

        // tests for different density
        typeB = new IngredientType("A", n2, Collections.emptyList(), 4.0);
        assertNotEquals(typeA, typeB);
    }

    @Test
    void testHashCode() {
        IngredientType typeB = new IngredientType("A", n1, Collections.emptyList(), 1.0);
        assertEquals(typeA.hashCode(), typeB.hashCode());

        // tests for different nutrition
        Nutrition n2 = new Nutrition(2.0, 4.0, 6.0);
        typeB = new IngredientType("A", n2, Collections.emptyList(), 1.0);
        assertNotEquals(typeA.hashCode(), typeB.hashCode());

        // tests for different name
        typeB = new IngredientType("b", n1, Collections.emptyList(), 1.0);
        assertNotEquals(typeA.hashCode(), typeB.hashCode());

        // tests for different density
        typeB = new IngredientType("A", n2, Collections.emptyList(), 4.0);
        assertNotEquals(typeA.hashCode(), typeB.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("A", typeA.toString());
    }
}