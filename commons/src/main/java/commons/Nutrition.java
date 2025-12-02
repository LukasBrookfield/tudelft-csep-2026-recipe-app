package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    // grams of carbs per 100g of the ingredient
    public Double carbs;  // can be null

    // grams of protein per 100g of the ingredient
    public Double protein;  // can be null

    // grams of fat per 100g of the ingredient
    public Double fat;  // can be null

    /**
     * Constructs a Nutrition object
     * @param carbs Grams of carbs in 100g of the ingredient
     * @param protein Grams of protein in 100g of the ingredient
     * @param fat Grams of fat in 100g of the ingredient
     */
    public Nutrition(Double carbs, Double protein, Double fat) {
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    private Nutrition() {}  // for object mapper

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
