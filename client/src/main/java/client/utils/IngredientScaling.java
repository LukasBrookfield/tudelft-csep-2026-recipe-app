package client.utils;

import commons.Ingredient;
import commons.Unit;

public class IngredientScaling {

    private final QuantityFormatter num = new QuantityFormatter();

    public String format(Ingredient ing, double scaleFactor){

        if (ing.ingredientType == null || ing.ingredientType.name == null) return "New ingredient";

        String name = ing.ingredientType.name;

        // if no amount unit or no amount, just name
        if (ing.amount == null && ing.unit == null) return name;

        // do not convert informal units
        if (ing.unit == Unit.TO_TASTE) return name + " to taste";
        if (ing.unit == Unit.PINCH) return name + "A pinch of " + name;
        if (ing.unit == Unit.HANDFUL) return name + "A handful of " + name;

        if (ing.amount == null) return name;

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
            case G -> num.formatInteger(scaled) + "G " + name;
            case ML -> num.formatInteger(scaled) + "ML " + name;

            // 1 decimal
            case KG -> num.formatDecimal(scaled) + "KG " + name;
            case L -> num.formatDecimal(scaled) + "L " + name;

            // prefer fractions for spoons
            case TBSP -> spoonText("tbsp", scaled, name);
            case TSP -> spoonText("tsp", scaled, name);

            default -> name;
        };
    }

    private String spoonText(String unitWord, double amount, String name){
        String amountStr = num.turnIntoNearestFraction(amount);

        // just change text based on whether its singular or plural
        if ("1".equals(amountStr)) {
            return "1 " + unitWord + " of " + name;
        }
        return amountStr + " " + unitWord + "s of " + name;
    }

}
