package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ShoppingListItemTest {

    static final IngredientType ING_TYPE_A = new IngredientType("A", null, null, null);
    static final Ingredient ING_A = new Ingredient(ING_TYPE_A, null, null, null);

    @Test
    public void testEquals() {
        // exactly equal item
        ShoppingListItem item1 = new ShoppingListItem(ING_A);
        assertEquals(new ShoppingListItem(ING_A), item1);

        //with empty ingredient
        assertNotEquals(new ShoppingListItem(null), item1);

        //with empty name
        item1.setRecipeName("Name");
        assertNotEquals(new ShoppingListItem(ING_A), item1);
    }

    @Test
    public void testHashCode() {
        // exactly equal item
        ShoppingListItem item1 = new ShoppingListItem(ING_A);
        assertEquals(new ShoppingListItem(ING_A).hashCode(), item1.hashCode());

        //with empty ingredient
        assertNotEquals(new ShoppingListItem(null).hashCode(), item1.hashCode());

        //with empty name
        item1.setRecipeName("Name");
        assertNotEquals(new ShoppingListItem(ING_A).hashCode(), item1.hashCode());
    }

    @Test
    public void testToString() {
        ShoppingListItem item1 = new ShoppingListItem(ING_A);

        //null name
        assertEquals("A", item1.toString());

        item1.setRecipeName("Name");
        assertEquals("A (Name)", item1.toString());
    }
}
