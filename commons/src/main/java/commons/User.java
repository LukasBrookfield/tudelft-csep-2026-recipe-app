package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.List;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class User {
    private List<Long> favouriteRecipes;
    private List<Ingredient> shoppingList;

    private User() {}  // for object mapper

    /**
     * Constructs a new User object
     * @param favouriteRecipes A list of the ids of the user's favourite recipes
     * @param shoppingList A list of ingredients part of the user's shopping list
     */
    public User(List<Long> favouriteRecipes, List<Ingredient> shoppingList) {
        this.favouriteRecipes = favouriteRecipes;
        this.shoppingList = shoppingList;
    }

    public List<Long> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public List<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public void setFavouriteRecipes(List<Long> favouriteRecipes) {
        this.favouriteRecipes = favouriteRecipes;
    }

    public void setShoppingList(List<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals (this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString (this, MULTI_LINE_STYLE);
    }
}
