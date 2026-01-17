package client.utils;

import commons.Ingredient;
import commons.Unit;

import java.util.ResourceBundle;

public class IngredientScaling {

    private final QuantityFormatter num = new QuantityFormatter();

    public String format(Ingredient ing, double scaleFactor, ResourceBundle b){

        if (ing.ingredientType == null || ing.ingredientType.name == null) return b.getString("newIngredient");

        String name = ing.ingredientType.name;

        // do not convert informal units
        if (ing.unit == Unit.TO_TASTE || ing.unit == Unit.PINCH || ing.unit == Unit.HANDFUL) {
            return ing.toString(b);
        }

        double f = (scaleFactor < 0) ? 1.0 : scaleFactor;
        double scaled = ing.amount * f;
        Unit unit = ing.unit;

        // Normalize
        if (unit == Unit.G && scaled >= 1000){
            scaled /= 1000.0;
            unit = Unit.KG;
        } else if (unit == Unit.ML && scaled >= 1000){
            scaled /= 1000.0;
            unit = Unit.L;
        }

        return switch (unit) {
            // Integer only
            case G -> num.formatInteger(scaled) + "g " + name;
            case ML -> num.formatInteger(scaled) + "mL " + name;

            // 1 decimal
            case KG -> num.formatDecimal(scaled) + "kg " + name;
            case L -> num.formatDecimal(scaled) + "L " + name;

            // prefer fractions for spoons
            case TBSP -> spoonText("tbsp", scaled, name, b);
            case TSP -> spoonText("tsp", scaled, name, b);

            default -> name;
        };
    }

    private String spoonText(String unitWord, double amount, String name, ResourceBundle b){
        String amountStr = num.turnIntoNearestFraction(amount);

        // just change text based on whether its singular or plural
        if ("1".equals(amountStr)) {
            if (unitWord.equals("tbsp")) {
                return "1 " + b.getString("unit.tablespoon.singular") + " " + name;
            } else {
                return "1 " +  b.getString("unit.teaspoon.singular") + " " + name;
            }
        }
        if (unitWord.equals("tbsp")) {
            return amountStr + " " + b.getString("unit.tablespoon.plural") + " " + name;
        } else {
            return amountStr + " " + b.getString("unit.teaspoon.plural") + " " + name;
        }
    }

}
