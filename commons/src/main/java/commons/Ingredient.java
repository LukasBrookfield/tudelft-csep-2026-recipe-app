package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String name;

    public double amount;

    @Enumerated(EnumType.STRING)
    public Unit unit;

    protected Ingredient() {}

    /**
     * Ingredient constructor
     * @param name Name of the ingredient
     * @param amount Amount of the ingredient
     * @param unit Unit the ingredient is in e.g. grams
     */
    public Ingredient(String name, double amount, Unit unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        // this equals method doesn't compare the ids so that ingredients from
        // different recipes can be compared
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ingredient that = (Ingredient) obj;
        return name.equals(that.name) && amount == that.amount && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
