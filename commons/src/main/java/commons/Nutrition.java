package commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Nutrition {
    @Id
    @OneToOne
    public Ingredient ingredient;

    public Double carbs;  // grams per 100g, can be null

    public Double protein;  // grams per 100g, can be null

    public Double fat;  // grams per 100g, can be null

    /**
     * Constructs a Nutrition object
     * @param ingredient The ingredient that has this nutrition
     * @param carbs Grams of carbs in 100g of the ingredient
     * @param protein Grams of protein in 100g of the ingredient
     * @param fat Grams of fat in 100g of the ingredient
     */
    public Nutrition(Ingredient ingredient, Double carbs, Double protein, Double fat) {
        this.ingredient = ingredient;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    protected Nutrition() {}

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
