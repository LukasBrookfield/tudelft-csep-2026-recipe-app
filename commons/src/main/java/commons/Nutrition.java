package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Nutrition {
    @Id
    @OneToOne
    private Ingredient ingredient;

    private Double carbs;  // grams per 100g

    private Double protein;  // grams per 100g

    private Double fat;  // grams per 100g

    protected Nutrition() {}

    /**
     * Constructs a Nutrition object
     * @param ingredient The ingredient that has this nutrition
     * @param carbs Grams of carbs in 100g of the ingredient
     * @param protein Grams of protein in 100g of the ingredient
     * @param fat Grams of fat in 100g of the ingredient
     */
    public Nutrition(Ingredient ingredient, double carbs, double protein, double fat) {
        this.ingredient = ingredient;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    /**
     * Uses the carbs, protein and fat content to calculate the calories per 100g
     * @return The amount of calories per 100g in the ingredient
     */
    public double getCaloriesPer100g() {
        return (4.0 * carbs) + (4.0 * protein) + (9.0 * fat);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "id");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
