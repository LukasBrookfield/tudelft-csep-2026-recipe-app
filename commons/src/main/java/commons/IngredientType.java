package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.List;

@Entity
public class IngredientType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(nullable = false)
    public String name;

    @OneToOne(cascade = CascadeType.ALL)
    public Nutrition nutrition;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Ingredient> ingredients;

    public Double density;  // unit = g/mL

    /**
     * Constructs a new IngredientType object
     * @param name The name of the ingredient e.g. tomato
     * @param nutrition The nutritional information of the ingredient type
     * @param ingredients A list of recipe ingredients that use this ingredient type
     * @param density The density of the ingredient in g/mL
     */
    public IngredientType(String name, Nutrition nutrition, List<Ingredient> ingredients, Double density) {
        this.name = name;
        this.nutrition = nutrition;
        this.ingredients = ingredients;
        this.density = density;
    }

    private IngredientType() {}  // for object mapper

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj,
                "id", "ingredients");  // excludes id and ingredients
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,
                "id", "ingredients");  // excludes id and ingredients
    }

    @Override
    public String toString() {
        return name;
    }
}
