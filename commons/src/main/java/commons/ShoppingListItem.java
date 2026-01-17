package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ResourceBundle;

public class ShoppingListItem {
    private Ingredient ingredient;
    private String recipeName;

    public ShoppingListItem(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.recipeName = null;
    }

    public ShoppingListItem() { } //for object mapper

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals (this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString(ResourceBundle b) {
        if(recipeName == null) {
            return ingredient.toString();
        }
        return ingredient.toString(b) + " (" + recipeName + ")";
    }
}
