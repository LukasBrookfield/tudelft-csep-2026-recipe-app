package client.utils;

import client.scenes.ShoppingListCtrl;
import commons.Ingredient;
import commons.Recipe;
import commons.ShoppingListItem;
import commons.Unit;

import java.util.List;

public class ShoppingListService {

    /**
     * Creates a deep copy of ingredients in recipe and saves them to provided list
     * @param recipe The recipe
     * @param list The list
     */
    public static void addIngredientsToListView(Recipe recipe, List<ShoppingListItem> list){
        list.addAll(recipe.ingredients.stream().
                map(Ingredient::copy).map(ShoppingListItem::new).toList());
    }

    /**
     * Checks if the name, amount and unit are valid for ingredient.
     * If not the methods prints warning to the console and returns false
     * @param name The name
     * @param amount The amount
     * @param unit The unit
     * @return true if valid and false if invalid
     */
    public static boolean ingredientValidation(String name, String amount, String unit){
        if(name.isEmpty()){
            System.out.println("A name is required.");
            return false;
        }

        if (!unit.equals("TO_TASTE")
                && amount.isEmpty()) {
            System.out.println("This unit needs an amount.");
            return false;
        }
        if (unit.equals("TO_TASTE")
                && !amount.isEmpty()) {
            System.out.println("This unit cannot have an amount.");
            return false;
        }
        return true;
    }

    /**
     * Sets name, amount and unit for provided ingredient
     * @param ingredient The ingredient
     * @param name The name as String
     * @param amount The amount as String
     * @param unit The unit as String
     */
    public static void applyEditsToIngredient(Ingredient ingredient, String name, String amount, String unit){
        ingredient.ingredientType.name = name;
        if (!amount.isEmpty()) {
            ingredient.amount = Double.parseDouble(amount);
        } else {
            ingredient.amount = null;
        }
        if (!unit.isEmpty()) {
            ingredient.unit = Unit.valueOf(unit);
        } else {
            ingredient.unit = null;
        }
    }

    /**
     * Saves the list of ingredients to the user. Adds a recipes name to each ingredient.
     * @param ingredientListView The list of ingredients
     * @param recipe The recipe from which ingredients comes from
     * @param user The user
     */
    public static void confirmAddingIngredients(List<ShoppingListItem> ingredientListView, Recipe recipe, UserConfig user){
        ingredientListView.forEach(ingredient -> {
            ingredient.setRecipeName(recipe.name);
            user.addShoppingListItem(ingredient);
        });
        user.saveUser();
    }
}
