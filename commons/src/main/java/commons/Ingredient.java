package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ResourceBundle;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @ManyToOne(optional = false)
    public IngredientType ingredientType;

    public Double amount;  // can be null

    @Enumerated(EnumType.STRING)
    public Unit unit;  // can be null

    @JsonBackReference
    @ManyToOne(optional = false)
    public Recipe recipe;

    /**
     * Constructs an Ingredient object
     * @param ingredientType Type of ingredient
     * @param amount Amount of the ingredient
     * @param unit Unit the ingredient is in e.g. grams
     * @param recipe The recipe this ingredient belongs to
     */
    public Ingredient(IngredientType ingredientType,
                      Double amount,
                      Unit unit,
                      Recipe recipe) {
        this.ingredientType = ingredientType;
        this.amount = amount;
        this.unit = unit;
        this.recipe = recipe;
    }

    private Ingredient() {}  // for object mapper

    /**
     * Copy an ingredient
     * @return a copy of ingredient
     */
    public Ingredient copy() {
        return new Ingredient(ingredientType.copy(), amount, unit, recipe);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj,
                "id", "recipe");  // excludes id and recipes
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,
                "id", "recipe");  // excludes id and recipes
    }

    public String toString(ResourceBundle b) {
        if (ingredientType == null) {
            return b.getString("newIngredient");
        }
        if (amount == null && unit == null) {
            return ingredientType.name;
        }
        if (unit == null) {
            return amount + " " + ingredientType.name;
        }

        switch (unit) {
            case G:
            case ML:
            case KG:
            case L:
                return amount + unit.name().toLowerCase() + " " + ingredientType.name;
            case TBSP:
                if (amount == 1) {
                    return "1 " + b.getString("unit.tablespoon.singular") + " " + ingredientType.name;
                } else {
                    return amount + " " + b.getString("unit.tablespoon.plural") + " " + ingredientType.name;
                }
            case TSP:
                if (amount == 1) {
                    return "1 "+ b.getString("unit.teaspoon.singular") +  " " + ingredientType.name;
                } else {
                    return amount + " " + b.getString("unit.teaspoon.plural") + " " + ingredientType.name;
                }
            case PINCH:
                if (amount == 1) {
                    return b.getString("unit.pinch.singular") + " " + ingredientType.name;
                } else {
                    return amount + " " + b.getString("unit.pinch.plural") + " " + ingredientType.name;
                }
            case HANDFUL:
                if (amount == 1) {
                    return b.getString("unit.handful.singular") + " " + ingredientType.name;
                } else {
                    return amount + " " + b.getString("unit.handful.plural") + " " + ingredientType.name;
                }
            case TO_TASTE:
                return ingredientType.name + " " + b.getString("unit.toTaste");
        }
        return "";
    }
}
