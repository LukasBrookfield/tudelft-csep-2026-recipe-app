package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutritionTest {
    Nutrition nutrition = new Nutrition(1.0, 2.0, 3.0);

    @Test
    void testEquals() {
        // tests if values are the same
        Nutrition nutrition2 = new Nutrition(1.0, 2.0, 3.0);
        assertEquals(nutrition, nutrition2);

        // tests if they are the same with different id
        // (id is excluded from comparisons and does not change therefore.)
        nutrition2.id = nutrition2.id + 1;
        assertEquals(nutrition, nutrition2);

        // tests if values are different
        nutrition2 = new Nutrition(2.0, 3.0, 4.0);
        assertNotEquals(nutrition, nutrition2);
    }

    @Test
    void testHashCode() {
        // tests if values are the same
        Nutrition nutrition2 = new Nutrition(1.0, 2.0, 3.0);
        assertEquals(nutrition.hashCode(), nutrition2.hashCode());

        // tests if they are the same with different id
        // (id is excluded from comparisons and does not change therefore.)
        nutrition2.id = nutrition2.id + 1;
        assertEquals(nutrition.hashCode(), nutrition2.hashCode());

        // tests if values are different
        nutrition2 = new Nutrition(2.0, 3.0, 4.0);
        assertNotEquals(nutrition.hashCode(), nutrition2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Nutrition[\n" +
                "  id=0\n" +
                "  carbs=1,0\n" +
                "  protein=2,0\n" +
                "  fat=3,0\n]", nutrition.toString());
    }
}