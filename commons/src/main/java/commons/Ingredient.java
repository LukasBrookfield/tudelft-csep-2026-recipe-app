package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    public IngredientType ingredientType;

    public Double amount;  // can be null

    @Enumerated(EnumType.STRING)
    public Unit unit;  // can be null

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
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

    @Override
    public String toString() {
        if (ingredientType == null) {
            return "New ingredient";
        }
        if (amount == null && unit == null) {
            return ingredientType.name;
        }
        if (unit == null) {
            return amount + " " + ingredientType.name;
        }

        switch (unit.name()) {
            case "G":
            case "ML":
                return amount + unit.name() + " " + ingredientType.name;
            case "TBSP":
                if (amount == 1) {
                    return "1 tablespoon of " + ingredientType.name;
                } else {
                    return amount + " tablespoons of " + ingredientType.name;
                }
            case "TSP":
                if (amount == 1) {
                    return "1 teaspoon of " + ingredientType.name;
                } else {
                    return amount + " teaspoons of " + ingredientType.name;
                }
            case "PINCH":
                if (amount == 1) {
                    return "A pinch of " + ingredientType.name;
                } else {
                    return amount + " pinches of " + ingredientType.name;
                }
            case "HANDFUL":
                if (amount == 1) {
                    return "A handful of " + ingredientType.name;
                } else {
                    return amount + " handfuls of " + ingredientType.name;
                }
            case "TO_TASTE":
                return ingredientType.name + " to taste";
        }
        return "";
    }
}
