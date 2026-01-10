package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Long> favouriteRecipes;
    private List<Ingredient> shoppingList;

    private String languageTag;

    /**
     * Constructs a new User object
     * @param favouriteRecipes A list of the ids of the user's favourite recipes
     * @param shoppingList A list of ingredients part of the user's shopping list
     */
    public User(List<Long> favouriteRecipes, List<Ingredient> shoppingList) {
        this.favouriteRecipes = favouriteRecipes;
        this.shoppingList = shoppingList;

        this.languageTag = "en";    // default language - english
    }

    /**
     * Constructs an empty User object
     */
    public User() {
        this.favouriteRecipes = new ArrayList<>();
        this.shoppingList = new ArrayList<>();
        this.languageTag = "en";
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

    /**
     * Adds an ingredient to shopping list
     * @param ingredient The ingredient
     */
    public void addShoppingListItem(Ingredient ingredient){
        if(shoppingList == null){
            shoppingList = new ArrayList<>();
        }
        shoppingList.add(ingredient);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals (this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    @Override
    public String toString() {
        return String.format("User[\n  favouriteRecipes=%s\n  shoppingList=%s\n languageTag%s\n]",
                favouriteRecipes, shoppingList, languageTag);
    }
}
