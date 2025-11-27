package commons;

import java.util.List;

public class User {
    private List<Integer> favouriteRecipes;
    private List<Ingredient> shoppingList;

    public User(List<Integer> favouriteRecipes, List<Ingredient> shoppingList) {
        this.favouriteRecipes = favouriteRecipes;
        this.shoppingList = shoppingList;
    }

    public List<Integer> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public List<Ingredient> getShoppingList() {
        return shoppingList;
    }
}
