package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.List;

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

    @ManyToMany(cascade = CascadeType.PERSIST)
    public List<Recipe> recipes;  // can be null

    /**
     * Constructs an Ingredient object
     * @param ingredientType Type of ingredient
     * @param amount Amount of the ingredient
     * @param unit Unit the ingredient is in e.g. grams
     * @param recipes The recipes this ingredient belongs to
     */
    public Ingredient(IngredientType ingredientType,
                      Double amount,
                      Unit unit,
                      List<Recipe> recipes) {
        this.ingredientType = ingredientType;
        this.amount = amount;
        this.unit = unit;
        this.recipes = recipes;
    }

    private Ingredient() {}

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj,
                "id", "recipes");  // excludes id and recipes
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,
                "id", "recipes");  // excludes id and recipes
    }

    @Override
    public String toString() {
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
