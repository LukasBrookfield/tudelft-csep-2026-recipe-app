package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ShoppingListItem {
    private final Ingredient ingredient;
    private String recipeName;

    public ShoppingListItem(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.recipeName = null;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals (this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        if(recipeName == null) {
            return ingredient.toString();
        }
        return ingredient.toString() + " (" + recipeName + ")";
    }
}
