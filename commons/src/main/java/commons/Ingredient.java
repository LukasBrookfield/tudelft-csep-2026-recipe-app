package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

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

    protected Ingredient() {}

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "id");  // excludes id
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id");  // excludes id
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
