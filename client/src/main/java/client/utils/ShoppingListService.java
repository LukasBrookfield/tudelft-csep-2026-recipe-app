package client.utils;

import commons.Ingredient;
import commons.Recipe;
import commons.Unit;

import java.util.List;

public class ShoppingListService {


    public static void addIngredientsToListView(Recipe recipe, List<Ingredient> list){
        list.addAll(recipe.ingredients.stream().
                map(Ingredient::copy).toList());
    }

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

    public static void confirmAddingIngredients(List<Ingredient> ingredientListView, Recipe recipe, UserConfig user){
        ingredientListView.forEach(ingredient -> {
            ingredient.ingredientType.name = ingredient.ingredientType.name + " (" + recipe.name + ")";
            user.addShoppingListItem(ingredient);
        });
        user.saveUser();
    }
}
