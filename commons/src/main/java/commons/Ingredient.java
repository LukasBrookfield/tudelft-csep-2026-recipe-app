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

    @Column(nullable = false)
    public String name;

    public Double amount;  // can be null

    @Enumerated(EnumType.STRING)
    public Unit unit;  // can be null

    @ManyToMany(cascade = CascadeType.PERSIST)
    public List<Recipe> recipes;  // can be null

    @OneToOne(cascade = CascadeType.ALL)
    public Nutrition nutrition;  // can be null

    /**
     * Constructs an Ingredient object with only a name
     * @param name Name of the ingredient
     */
    public Ingredient(String name) {
        this.name = name;
        this.amount = null;
        this.unit = null;
    }

    /**
     * Constructs an Ingredient object
     * @param name Name of the ingredient
     * @param amount Amount of the ingredient
     * @param unit Unit the ingredient is in e.g. grams
     * @param recipes The recipes this ingredient belongs to
     * @param nutrition Nutritional info of the ingredient
     */
    public Ingredient(String name,
                      Double amount,
                      Unit unit,
                      List<Recipe> recipes,
                      Nutrition nutrition) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.recipes = recipes;
        this.nutrition = nutrition;
    }

    private Ingredient() {}

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "id", "recipes");  // excludes id and recipes
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id", "recipes");  // excludes id and recipes
    }

    /**
     * Turns an ingredient object into a string
     * @return The ingredient in string format
     */
    @Override
    public String toString() {
        if (amount == null && unit == null) {
            return name;
        }
        if (unit == null) {
            return amount + " " + name;
        }

        switch (unit.name()) {
            case "G":
            case "ML":
                return amount + unit.name() + " " + name;
            case "TBSP":
                if (amount == 1) {
                    return "1 tablespoon of " + name;
                } else {
                    return amount + " tablespoons of " + name;
                }
            case "TSP":
                if (amount == 1) {
                    return "1 teaspoon of " + name;
                } else {
                    return amount + " teaspoons of " + name;
                }
            case "PINCH":
                if (amount == 1) {
                    return "A pinch of " + name;
                } else {
                    return amount + " pinches of " + name;
                }
            case "HANDFUL":
                if (amount == 1) {
                    return "A handful of " + name;
                } else {
                    return amount + " handfuls of " + name;
                }
            case "TO_TASTE":
                return name + " to taste";
        }
        return "";
    }
}
