package commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Ingredient> ingredients;

    @ElementCollection
    private List<String> steps;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    protected Recipe() {}

    /**
     * Constructs a Recipe object
     * @param name The name of the recipe
     * @param ingredients A list of ingredients needed for the recipe
     * @param steps A list of steps that you need to follow
     */
    @JsonCreator
    public Recipe(@JsonProperty("name") String name,
                  @JsonProperty("ingredients") List<Ingredient> ingredients,
                  @JsonProperty("steps") List<String> steps) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
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
