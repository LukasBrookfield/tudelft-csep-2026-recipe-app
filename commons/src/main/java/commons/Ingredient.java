package commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private long id;

    @Column(nullable = false)
    private String name;

    private Double amount;  // can be null

    @Enumerated(EnumType.STRING)
    private Unit unit;  // can be null

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Recipe> recipes;  // can be null

    @OneToOne(cascade = CascadeType.ALL)
    private Nutrition nutrition;  // can be null

    /**
     * Constructs an Ingredient object
     * @param name Name of the ingredient
     * @param amount Amount of the ingredient
     * @param unit Unit the ingredient is in e.g. grams
     * @param recipes The recipes this ingredient belongs to
     * @param nutrition Nutritional info of the ingredient
     */
    @JsonCreator
    public Ingredient(@JsonProperty("name") String name,
                      @JsonProperty("amount") Double amount,
                      @JsonProperty("unit") Unit unit,
                      @JsonProperty("recipes") List<Recipe> recipes,
                      @JsonProperty("nutrition") Nutrition nutrition) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }
}
