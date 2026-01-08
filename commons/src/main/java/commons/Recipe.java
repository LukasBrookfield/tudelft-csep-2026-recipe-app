package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(nullable = false)
    public String name;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Ingredient> ingredients;

    @ElementCollection
    public List<String> steps;

    public int servings;

    /**
     * Constructs a Recipe object with only a name
     * @param name The name of the recipe
     */
    public Recipe(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.servings = 1;
    }

    /**
     * Constructs a Recipe object
     * @param name The name of the recipe
     * @param ingredients A list of ingredients needed for the recipe
     * @param steps A list of steps that you need to follow
     * @param servings The number of servings the recipe provides
     */
    public Recipe(String name, List<Ingredient> ingredients, List<String> steps, int servings) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }

    private Recipe() {}  // for object mapper

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
        return name;
    }
}
