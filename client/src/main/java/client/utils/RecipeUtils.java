package client.utils;
import commons.Recipe;
import commons.Ingredient;
import commons.IngredientType;
import commons.Unit;
import jakarta.inject.Inject;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;
import javafx.stage.Modality;
import java.util.*;

public class RecipeUtils {
    public static final double CAL_PER_GRAM_CARB = 4.0;
    public static final double CAL_PER_GRAM_PROTEIN = 4.0;
    public static final double CAL_PER_GRAM_FAT = 9.0;
    private final LanguageService languages;

    @Inject
    public RecipeUtils(LanguageService languages) {
        this.languages = languages;
    }
    /**
     * Uses the carbs, protein and fat content to calculate the calories per 100g.
     * If the unit isn't grams or there is no nutritional info, return 0.
     * @param i The ingredient type to measure
     * @return The amount of calories per 100g in the ingredient
     */
    public double getCaloriesPer100g(IngredientType i) {
        if (i.nutrition == null) return -1.0;
        double calories = 0.0;
        if (i.nutrition.carbs != null) calories += CAL_PER_GRAM_CARB * i.nutrition.carbs;
        if (i.nutrition.protein != null) calories += CAL_PER_GRAM_PROTEIN * i.nutrition.protein;
        if (i.nutrition.fat != null) calories += CAL_PER_GRAM_FAT * i.nutrition.fat;
        return calories;
    }

    /**
     * Normalize the amount and the unit of ingredients exceeding 1000 units for G and ML
     * @param ingredients The list of ingredients
     */
    public void normalizeIngredients(List<Ingredient> ingredients) {
        for (Ingredient ing : ingredients) {
            if (!(ing == null || ing.amount == null || ing.unit == null)) {
                if (ing.amount >= 1000) {
                    if (ing.unit == Unit.ML) {
                        ing.amount /= 1000.0;
                        ing.unit = Unit.L;
                    } else if (ing.unit == Unit.G) {
                        ing.amount /= 1000.0;
                        ing.unit = Unit.KG;
                    }
                }
            }
        }
    }

    /**
     * Adds all ingredient types from the recipe to the Database
     * @param recipe The recipe being edited
     * @param server ServerUtils object for http requests
     */
    public void commitLocalIngredientTypes(Recipe recipe, ServerUtility server) {
        Map<String, IngredientType> savedTypes = new HashMap<>();

        for (Ingredient ing : recipe.ingredients) {
            if (ing == null || ing.ingredientType == null) continue;

            IngredientType type = ing.ingredientType;

            if (savedTypes.containsKey(type.name)) {
                ing.ingredientType = savedTypes.get(type.name);
                continue;
            }

            if (type.id == 0) {
                IngredientType saved = server.addIngredientType(type);
                savedTypes.put(saved.name, saved);
                ing.ingredientType = saved;
            } else {
                savedTypes.put(type.name, type);
            }
        }
    }

    /**
     * Display an alert indicating that the user input is invalid
     * Reset the text fields of the FXML elements
     * @param warningMessage Key to translated warning message
     * @param fields FXML fields to reset
     */
    public void displayAlertInputWarning(String warningMessage, List<TextInputControl> fields) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);

        ResourceBundle b = languages.bundle();
        alert.setTitle(b.getString("recipe.warning.invalid.user.input"));
        alert.setHeaderText("Wooops...");
        alert.setContentText(b.getString(warningMessage));

        alert.getDialogPane().applyCss();

        Node header = alert.getDialogPane().lookup(".header-panel .label");
        if (header != null) {
            header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        }

        Node content = alert.getDialogPane().lookup(".content.label");
        if (content != null) {
            content.setStyle("-fx-font-size: 12px;");
        }

        if(fields != null && !fields.isEmpty()) {
            clearFields(fields);
        }

        alert.show();
    }

    /**
     * Clear any number of provided fields
     * @param fields One or more TextFields/TextAreas to clear
     */
    public void clearFields(List<TextInputControl> fields) {
        for (TextInputControl field : fields) {
            if (field != null) {
                field.clear();
                field.setText("");
            }
        }
    }
}
