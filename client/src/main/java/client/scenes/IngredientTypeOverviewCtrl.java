package client.scenes;

import client.utils.RecipeUtils;
import client.utils.ServerUtils;
import client.utils.UserConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.IngredientType;
import commons.Nutrition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class IngredientTypeOverviewCtrl {

    private final ServerUtils server;

    private final RecipeUtils recipeUtils;

    private UserConfig user;

    private final MainCtrl mainCtrl;

    // Left sidebar

    @FXML
    private TextField ingredientTypeSearchField;

    @FXML
    private ListView<IngredientType> ingredientTypeListView;

    @FXML
    private Button removeIngredientTypeButton;

    @FXML
    private Button addIngredientTypeButton;

    // Top right

    @FXML
    private Button toggleOverviewButton;

    // Ingredient title row

    @FXML
    private Label ingredientTypeTitleLabel;

    @FXML
    private Button editIngredientTypeButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button doneEditButton;

    @FXML
    private Separator mainSeparator;

    // Details

    @FXML
    private Label nameLabel;

    // Edit details section

    @FXML
    private Button editDetailsButton;

    @FXML
    private TextField editNameField;

    @FXML
    private Button cancelEditDetailsButton;

    @FXML
    private Button doneEditDetailsButton;

    // edit density section

    @FXML
    private Button editDensityButton;

    @FXML
    private TextField editDensityField;

    @FXML
    private Button cancelEditDensityButton;

    @FXML
    private Button doneEditDensityButton;

    @FXML
    private Label densityLabel;

    // Nutritional values

    @FXML
    private Label proteinLabel;

    @FXML
    private Label fatLabel;

    @FXML
    private Label carbsLabel;

    @FXML
    private Label kcalLabel;

    @FXML
    private TextField proteinTextField;

    @FXML
    private TextField fatTextField;

    @FXML
    private TextField carbsTextField;

    // Edit nutritional values section

    @FXML
    private Button editNutritionButton;

    @FXML
    private Button cancelEditNutritionButton;

    @FXML
    private Button doneEditNutritionButton;

    // Bottom

    @FXML
    private Label usedInRecipesLabel;

    // General
    @Inject
    public IngredientTypeOverviewCtrl(ServerUtils server,
                                      RecipeUtils recipeUtils,
                                      UserConfig user,
                                      MainCtrl mainCtrl) {
        this.server = server;
        this.recipeUtils = recipeUtils;
        this.user = user;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Changes the scene between viewing and editing an ingredient type
     * @param value false for viewing mode, true for editing mode
     */
    @FXML
    private void changeViewEditMode(boolean value) {
        editIngredientTypeButton.setVisible(!value);
        cancelEditButton.getParent().setVisible(value);
        editDetailsButton.getParent().getParent().getParent().setVisible(value);

        editDensityButton.setVisible(value);
        editNutritionButton.getParent().getParent().getParent().setVisible(value);

        // While in edit mode, the user can't change to a different ingredient
        ingredientTypeSearchField.setDisable(value);
        ingredientTypeListView.setDisable(value);

        // While in edit mode, the user can't remove the current ingredient
        // or add a new one
        addIngredientTypeButton.setVisible(!value);
        removeIngredientTypeButton.setVisible(!value);
    }

    /**
     * Changes the scene between viewing and editing the ingredient type details
     * @param value false for viewing mode, true for editing mode
     */
    @FXML
    private void changeDetailsViewEditMode(boolean value) {
        editDetailsButton.setVisible(!value);

        // When the lower layer is active, we need to let clicks through the
        // upper layer
        editDetailsButton.getParent().setMouseTransparent(value);

        editNameField.getParent().setVisible(value);

        cancelEditButton.setVisible(!value);
        doneEditButton.setVisible(!value);
        editNutritionButton.setVisible(!value);
        editDensityButton.setVisible(!value);
    }

    /**
     * Changes the scene between viewing and editing the nutritional values
     * @param value false for viewing mode, true for editing mode
     */
    @FXML
    private void changeNutritionViewEditMode(boolean value) {
        editNutritionButton.setVisible(!value);

        // When the lower layer is active, we need to let clicks through the
        // upper layer
        editNutritionButton.getParent().setMouseTransparent(value);

        cancelEditNutritionButton.getParent().setVisible(value);
        proteinTextField.getParent().setVisible(value);

        cancelEditButton.setVisible(!value);
        doneEditButton.setVisible(!value);
        editDetailsButton.setVisible(!value);
        editDensityButton.setVisible(!value);
    }

    /**
     * Sets all labels based on which ingredient type is selected
     */
    private void setLabelsAndFields() {
        IngredientType ingredientType = ingredientTypeListView
                .getSelectionModel().getSelectedItem();
        if (ingredientType == null) {
            return;
        }

        ingredientTypeTitleLabel.setText(ingredientType.name);
        nameLabel.setText(ingredientType.name);
        if (ingredientType.density != null) {
            densityLabel.setText(String.valueOf(ingredientType.density));
        } else {
            densityLabel.setText("-");
        }
        kcalLabel.setText(String.valueOf(recipeUtils
                .getCaloriesPer100g(ingredientType)));

        proteinLabel.setText("-");
        fatLabel.setText("-");
        carbsLabel.setText("-");

        if (ingredientType.nutrition == null) {
            return;
        }
        if (ingredientType.nutrition.protein != null) {
            proteinLabel.setText(String.valueOf(ingredientType.nutrition.protein) + "g");
        }
        if (ingredientType.nutrition.fat != null) {
            fatLabel.setText(String.valueOf(ingredientType.nutrition.fat) + "g");
        }
        if (ingredientType.nutrition.carbs != null) {
            carbsLabel.setText(String.valueOf(ingredientType.nutrition.carbs) + "g");
        }

        int usedInRecipes = 0;
        if (ingredientType.ingredients != null) {
            usedInRecipes = ingredientType.ingredients.size();
        }
        usedInRecipesLabel.setText("This ingredient type is used in "
        + usedInRecipes + " recipe" + (usedInRecipes == 1 ? "" : "s"));
    }

    /**
     * Refreshes the list of ingredient types. When there is no ingredient type,
     * the right side doesn't show. When there is an ingredient type, it sets
     * the labels based on that ingredient type
     */
    @FXML
    public void onRefresh() {
        ingredientTypeListView.getItems().setAll(server.getIngredientTypes());

        boolean empty = ingredientTypeListView.getItems().isEmpty();
        mainSeparator.getParent().setVisible(!empty);

        if (ingredientTypeListView.getSelectionModel().getSelectedIndex() == -1) {
            ingredientTypeListView.getSelectionModel().select(0);
        }

        setLabelsAndFields();
    }

    /**
     * Changes between viewing and editing density
     * @param value true for edit mode, false for viewing mode
     */
    public void changeDensityViewEditMode(boolean value) {
        editDensityButton.setVisible(!value);
        doneEditDensityButton.setVisible(value);
        cancelEditDensityButton.setVisible(value);
        editDensityField.getParent().setVisible(value);

        cancelEditButton.setVisible(!value);
        doneEditButton.setVisible(!value);
        editDetailsButton.setVisible(!value);
        editNutritionButton.setVisible(!value);

        editDensityButton.getParent().setMouseTransparent(value);
    }

    /**
     * Initializes the ingredient type overview with default values
     */
    @FXML
    private void initialize() {
        changeDetailsViewEditMode(false);
        changeDensityViewEditMode(false);
        changeNutritionViewEditMode(false);
        changeViewEditMode(false);
        onRefresh();

        ingredientTypeListView.getSelectionModel().selectedItemProperty().addListener(
                (observable,
                 oldIngredientType, newIngredientType) -> {
                    onRefresh();
                }
        );
    }

    // Left sidebar

    /**
     * On action method for the Remove Ingredient Type button
     * It removes the currently selected ingredient type (if any)
     */
    @FXML
    private void onRemoveIngredientTypeButton() {
        IngredientType ingredientType = ingredientTypeListView
                .getSelectionModel().getSelectedItem();
        server.deleteIngredientType(ingredientType.id);
        onRefresh();
    }

    /**
     * On action method for the Add Ingredient Type button
     * It creates a new ingredient type and then selects this ingredient
     */
    @FXML
    private void onAddIngredientTypeButton() throws JsonProcessingException {
        IngredientType ingredientType = new IngredientType(
                "New ingredient", null, new ArrayList<>(), null);

        System.out.println(new ObjectMapper().writeValueAsString(ingredientType));

        server.addIngredientType(ingredientType);
        onRefresh();
        ingredientTypeListView.getSelectionModel().select(
                ingredientTypeListView.getItems().size() - 1
        );

        onEditIngredientTypeButton();
        nameLabel.setText("-");
        editDetailsButton.requestFocus();
    }

    // Top right

    /**
     * Changes the scene to Recipe Overview
     */
    @FXML
    private void onToggleOverviewButton() {
        mainCtrl.showRecipeOverview();
    }

    // Ingredient title row

    /**
     * On action method for the Edit Ingredient Type button
     * Changes the scene to edit mode
     */
    @FXML
    private void onEditIngredientTypeButton() {
        changeViewEditMode(true);
    }

    /**
     * On action method for the Cancel Edit button
     * The original ingredient is not changed
     */
    @FXML
    private void onCancelEditButton() {
        onRefresh();
        changeViewEditMode(false);
    }

    /**
     * On action method for the Done Edit button
     * All the changes are added to the selected ingredient type
     */
    @FXML
    private void onDoneEditButton() {
        if (nameLabel.getText().equals("-")) {
            System.out.println("The ingredient needs a name.");
            return;
        }

        IngredientType ingredientType = ingredientTypeListView
                .getSelectionModel().getSelectedItem();
        ingredientType.name = nameLabel.getText();

        String densityText = densityLabel.getText();
        if (densityText.isBlank() || densityText.equals("-")) {
            ingredientType.density = null;
        } else {
            ingredientType.density = Double.parseDouble(densityText);
        }

        if (!proteinLabel.getText().equals("-") || !fatLabel.getText().equals("-")
                || !carbsLabel.getText().equals("-")) {
            if (ingredientType.nutrition == null) {
                ingredientType.nutrition = new Nutrition(null,
                        null, null);
            }

            if (!proteinLabel.getText().equals("-")) {
                ingredientType.nutrition.protein = Double.parseDouble(proteinLabel.getText()
                        .substring(0, proteinLabel.getText().length() - 1));
            }
            if (!fatLabel.getText().equals("-")) {
                ingredientType.nutrition.fat = Double.parseDouble(fatLabel.getText()
                        .substring(0, fatLabel.getText().length() - 1));
            }
            if (!carbsLabel.getText().equals("-")) {
                ingredientType.nutrition.carbs = Double.parseDouble(carbsLabel.getText()
                        .substring(0, carbsLabel.getText().length() - 1));
            }
        }

        server.updateIngredientType(ingredientType.id, ingredientType);

        onRefresh();
        changeViewEditMode(false);
    }

    // Edit details section

    /**
     * On action method for the Edit Details button
     * Changes the scene to Edit Details mode
     */
    @FXML
    private void onEditDetailsButton() {
        changeDetailsViewEditMode(true);

        if (!nameLabel.getText().equals("-")) {
            editNameField.setText(nameLabel.getText());
        } else {
            editNameField.setText("");
        }
    }

    /**
     * On action method for the Cancel Edit Details button
     */
    @FXML
    private void onCancelEditDetailsButton() {
        if (editNameField.getText().isEmpty()) {
            nameLabel.setText("New ingredient");
        }

        changeDetailsViewEditMode(false);
    }

    /**
     * On action method for the Done Edit Details button
     * Note that this only changes the original ingredient type if the user presses
     * 'Done' later
     */
    @FXML
    private void onDoneEditDetailsButton() {
        if (editNameField.getText().isBlank()) {
            System.out.println("The ingredient type needs a name.");
            return;
        }

        nameLabel.setText(editNameField.getText());

        changeDetailsViewEditMode(false);
    }

    /**
     * On action method for edit density button
     */
    @FXML
    private void onEditDensityButton() {
        changeDensityViewEditMode(true);
    }

    /**
     * On action method for cancel edit density button
     */
    @FXML
    private void onCancelEditDensityButton() {
        changeDensityViewEditMode(false);
    }

    /**
     * On action method for done edit density button
     */
    @FXML
    private void onDoneEditDensityButton() {
        if (editDensityField.getText().isBlank()) {
            densityLabel.setText("");
            changeDensityViewEditMode(false);
            return;
        }
        double density;
        try {
            density = Double.parseDouble(editDensityField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Density must be a double");
            return;
        }
        densityLabel.setText(String.valueOf(density));
        changeDensityViewEditMode(false);
    }

    // Edit nutrition section

    /**
     * On action method for the Edit Nutrition button
     * Changes the scene to edit nutrition mode
     */
    @FXML
    private void onEditNutritionButton() {
        changeNutritionViewEditMode(true);

        proteinTextField.setText(proteinLabel.getText()
                .substring(0, proteinLabel.getText().length() - 1));
        fatTextField.setText(fatLabel.getText()
                .substring(0, fatLabel.getText().length() - 1));
        carbsTextField.setText(carbsLabel.getText()
                .substring(0, carbsLabel.getText().length() - 1));
    }

    /**
     * On action method for the Cancel Edit Nutrition button
     */
    @FXML
    private void onCancelEditNutritionButton() {
        changeNutritionViewEditMode(false);
    }

    /**
     * On action method for the Done Edit Nutrition button
     * All changes are added onto the selected ingredient type
     */
    @FXML
    private void onDoneEditNutritionButton() {
        if (!proteinTextField.getText().isEmpty()) {
            proteinLabel.setText(proteinTextField.getText() + "g");
        } else {
            proteinLabel.setText("-");
        }
        if (!fatTextField.getText().isEmpty()) {
            fatLabel.setText(fatTextField.getText() + "g");
        } else {
            fatLabel.setText("-");
        }
        if (!carbsTextField.getText().isEmpty()) {
            carbsLabel.setText(carbsTextField.getText() + "g");
        } else {
            carbsLabel.setText("-");
        }

        kcalLabel.setText("-");
        changeNutritionViewEditMode(false);
    }
}
