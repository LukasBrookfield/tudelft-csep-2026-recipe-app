package client.scenes;

import client.utils.ServerUtils;
import client.utils.UserConfig;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.Unit;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class IngredientOverviewCtrl {

    private final ServerUtils server;

    private UserConfig user;

    private final MainCtrl mainCtrl;

    // Left sidebar

    @FXML
    private TextField ingredientSearchField;

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button addIngredientButton;

    // Top right

    @FXML
    private Button toggleOverviewButton;

    // Ingredient title row

    @FXML
    private Label ingredientTitleLabel;

    @FXML
    private Button editIngredientButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button doneEditButton;

    @FXML
    private Separator mainSeparator;

    // Details

    @FXML
    private Label nameLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label unitLabel;

    // Edit details section

    @FXML
    private Button editDetailsButton;

    @FXML
    private TextField editNameField;

    @FXML
    private TextField editAmountField;

    @FXML
    private ChoiceBox<String> editUnitBox;

    @FXML
    private Button cancelEditDetailsButton;

    @FXML
    private Button doneEditDetailsButton;

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
    public IngredientOverviewCtrl(ServerUtils server, UserConfig user, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.user = user;
    }

    /**
     * Changes the scene between viewing and editing an ingredient
     * @param value false for viewing mode, true for editing mode
     */
    @FXML
    private void changeViewEditMode(boolean value) {
        editIngredientButton.setVisible(!value);
        cancelEditButton.getParent().setVisible(value);
        editDetailsButton.getParent().getParent().getParent().setVisible(value);
        editNutritionButton.getParent().getParent().getParent().setVisible(value);

        // While in edit mode, the user can't change to a different ingredient
        ingredientSearchField.setDisable(value);
        ingredientListView.setDisable(value);

        // While in edit mode, the user can't remove the current ingredient
        // or add a new one
        addIngredientButton.setVisible(!value);
        removeIngredientButton.setVisible(!value);
    }

    /**
     * Changes the scene between viewing and editing the ingredient details
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
    }

    /**
     * Sets all labels based on which ingredient is selected
     */
    private void setLabelsAndFields() {
        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        if (ingredient != null) {
            ingredientTitleLabel.setText(ingredient.toString());

            if (ingredient.ingredientType != null) {
                nameLabel.setText(ingredient.ingredientType.name);
            } else {
                nameLabel.setText("-");
            }

            if (ingredient.amount != null) {
                amountLabel.setText(String.valueOf(ingredient.amount));
            } else {
                amountLabel.setText("-");
            }

            if (ingredient.unit != null) {
                unitLabel.setText(ingredient.unit.name());
            } else {
                unitLabel.setText("-");
            }
        }
    }

    /**
     * Refreshes the list of ingredients. When there is no ingredient, the right
     * side doesn't show. When there is an ingredient, it sets the labels based
     * on that ingredient
     */
    @FXML
    private void onRefresh() {
        ingredientListView.refresh();

        boolean empty = ingredientListView.getItems().isEmpty();
        mainSeparator.getParent().setVisible(!empty);

        setLabelsAndFields();
    }

    /**
     * Initializes the ingredient overview with default values
     */
    @FXML
    private void initialize() {
        changeDetailsViewEditMode(false);
        changeNutritionViewEditMode(false);
        changeViewEditMode(false);

        editUnitBox.getItems().addAll("", "G", "ML", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        onRefresh();

        ingredientListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldRecipe, newRecipe) -> {
                    onRefresh();
                }
        );
    }

    // Left sidebar

    /**
     * On action method for the Remove Ingredient button
     * It removes the currently selected ingredient (if any)
     */
    @FXML
    private void onRemoveIngredientButton() {
        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        ingredientListView.getItems().remove(ingredient);
        onRefresh();
    }

    /**
     * On action method for the Add Ingredient button
     * It creates a new ingredient and then selects this ingredient
     */
    @FXML
    private void onAddIngredientButton() {
        Ingredient ingredient = new Ingredient(null,
                null, null, null);

        ingredientListView.getItems().add(ingredient);
        ingredientListView.getSelectionModel().select(
                ingredientListView.getItems().size() - 1
        );

        onEditIngredientButton();
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
     * On action method for the Edit Ingredient button
     * Changes the scene to edit mode
     */
    @FXML
    private void onEditIngredientButton() {
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
     * All the changes are added to the selected ingredient
     */
    @FXML
    private void onDoneEditButton() {
        if (nameLabel.getText().equals("-")) {
            System.out.println("The ingredient needs a name.");
            return;
        }

        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();

        ingredient.ingredientType = new IngredientType(nameLabel.getText(),
                null, null);

        if (!amountLabel.getText().equals("-")) {
            ingredient.amount = Double.parseDouble(amountLabel.getText());
        } else {
            ingredient.amount = null;
        }

        if (!unitLabel.getText().equals("-")) {
            ingredient.unit = Unit.valueOf(unitLabel.getText());
        } else {
            ingredient.unit = null;
        }

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

        if (!amountLabel.getText().equals("-")) {
            editAmountField.setText(amountLabel.getText());
        } else {
            editAmountField.setText("");
        }

        if (!unitLabel.getText().equals("-")) {
            editUnitBox.setValue(unitLabel.getText());
        } else {
            editUnitBox.setValue("");
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
     * Note that this only changes the original ingredient if the user presses
     * 'Done' later
     */
    @FXML
    private void onDoneEditDetailsButton() {
        if (editNameField.getText().isEmpty()) {
            System.out.println("The ingredient needs a name.");
            return;
        }
        if (!editUnitBox.getValue().equals("TO_TASTE")
                && editAmountField.getText().isEmpty()) {
            System.out.println("This unit needs an amount.");
            return;
        }
        if (editUnitBox.getValue().equals("TO_TASTE")
                && !editAmountField.getText().isEmpty()) {
            System.out.println("This unit cannot have an amount.");
            return;
        }

        nameLabel.setText(editNameField.getText());
        if (!editAmountField.getText().isEmpty()) {
            amountLabel.setText(editAmountField.getText());
        } else {
            amountLabel.setText("-");
        }
        if (!editUnitBox.getValue().isEmpty()) {
            unitLabel.setText(editUnitBox.getValue());
        } else {
            unitLabel.setText("-");
        }

        changeDetailsViewEditMode(false);
    }

    // Edit nutrition section

    @FXML
    private void onEditNutritionButton() {
        changeNutritionViewEditMode(true);
    }

    @FXML
    private void onCancelEditNutritionButton() {
        changeNutritionViewEditMode(false);
    }

    @FXML
    private void onDoneEditNutritionButton() {
        changeNutritionViewEditMode(false);
    }
}
