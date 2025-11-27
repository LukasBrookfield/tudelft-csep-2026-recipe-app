package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

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

    protected Ingredient() {}

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
     */
    public Ingredient(String name, Double amount, Unit unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        // this equals method doesn't compare the id so that ingredients from
        // different recipes can be compared
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equals(that.name) && Objects.equals(amount, that.amount) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

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
