package commons;

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
    public long id;

    public String name;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Ingredient> ingredients;

    @ElementCollection
    public List<String> steps;

    protected Recipe() {}

    /**
     * Recipe constructor
     * @param name The name of the recipe
     * @param ingredients A list of ingredients needed for the recipe
     * @param steps A list of steps that you need to follow
     */
    public Recipe(String name, List<Ingredient> ingredients, List<String> steps) {
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
