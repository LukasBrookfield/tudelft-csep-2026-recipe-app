package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Long> favouriteRecipes;
    private List<ShoppingListItem> shoppingList;
    private List<String> selectedLanguages;

    private String languageTag;

    /**
     * Constructs a new User object
     * @param favouriteRecipes A list of the ids of the user's favourite recipes
     * @param shoppingList A list of ingredients part of the user's shopping list
     */
    public User(List<Long> favouriteRecipes,
                List<ShoppingListItem> shoppingList,
                List<String> selectedLanguages) {
        this.favouriteRecipes = favouriteRecipes;
        this.shoppingList = shoppingList;
        this.selectedLanguages = selectedLanguages;
    }

    /**
     * Constructs an empty User object
     */
    public User() {
        this.favouriteRecipes = new ArrayList<>();
        this.shoppingList = new ArrayList<>();
        this.selectedLanguages = new ArrayList<>(List.of("en", "nl", "pt"));
    }

    public List<Long> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public List<ShoppingListItem> getShoppingList() {
        return shoppingList;
    }

    public void setFavouriteRecipes(List<Long> favouriteRecipes) {
        this.favouriteRecipes = favouriteRecipes;
    }

    public void setShoppingList(List<ShoppingListItem> shoppingList) {
        this.shoppingList = shoppingList;
    }

    /**
     * Adds an ingredient to shopping list
     * @param shoppingListItem The ingredient
     */
    public void addShoppingListItem(ShoppingListItem shoppingListItem) {
        if(shoppingList == null){
            shoppingList = new ArrayList<>();
        }
        shoppingList.add(shoppingListItem);
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<String> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
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
//        return String.format("User[\n  favouriteRecipes=%s\n  shoppingList=%s\n]",
//                favouriteRecipes, shoppingList);
        return "User[\n" +
                "  favouriteRecipes=" + favouriteRecipes + "\n" +
                "  shoppingList=" + shoppingList + "\n" +
                "]";
    }
}
