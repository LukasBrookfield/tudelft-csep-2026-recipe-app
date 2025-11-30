package client.scenes;

import client.utils.ServerUtils;
import client.utils.UserConfig;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.Unit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ShoppingListCtrl {

    private final ServerUtils server;

    private UserConfig user;

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private TextField editIngredientNameField;

    @FXML
    private TextField editIngredientAmountField;

    @FXML
    private ChoiceBox<String> editUnitBox;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button editIngredientButton;

    @FXML
    private Button cancelEditIngredientButton;

    @FXML
    private Button doneEditIngredientButton;

    @Inject
    public ShoppingListCtrl(ServerUtils server, UserConfig user) {
        this.server = server;
        this.user = user;
    }

    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed
     */
    @FXML
    private void onRefresh() {
        ingredientListView.refresh();
        if(ingredientListView.getSelectionModel().getSelectedItem() != null) {
            editIngredientButton.setVisible(true);
            removeIngredientButton.setVisible(true);
        }else{
            editIngredientButton.setVisible(false);
            removeIngredientButton.setVisible(false);
        }
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     * @param value false vor viewing mode, true for editing mode
     */
    private void changeIngredientViewEditMode(boolean value) {
        removeIngredientButton.setVisible(!value);
        addIngredientButton.setVisible(!value);
        editIngredientButton.setVisible(!value);
        editIngredientNameField.setVisible(value);
        editIngredientAmountField.setVisible(value);
        editUnitBox.setVisible(value);
        cancelEditIngredientButton.setVisible(value);
        doneEditIngredientButton.setVisible(value);

        removeIngredientButton.getParent().setMouseTransparent(value);
    }

    /**
     * Initializes the shopping list screen with default values
     */
    @FXML
    private void initialize() {
        changeIngredientViewEditMode(false);
        editIngredientButton.setVisible(false);
        removeIngredientButton.setVisible(false);
        editUnitBox.getItems().addAll("", "G", "ML", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        ingredientListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldIngredient, newIngredient) -> onRefresh()
        );

        // Makes it so that the 'Add Recipe' button is selected when the app gets
        // started
        Platform.runLater(() -> addIngredientButton.requestFocus());
    }

    /**
     * On action method for the Remove Ingredient Button
     * Removes the currently selected ingredient (if any), note that a change like
     * this only affects the recipe if the user presses 'Done' later
     */
    @FXML
    private void onRemoveIngredientButton() {
        if(ingredientListView.getItems().isEmpty()){
            System.out.println("There is no ingredient to remove.");
            return;
        }
        if (ingredientListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no ingredient selected.");
            return;
        }
        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        ingredientListView.getItems().remove(index);
    }

    /**
     * On action method for the Add Ingredient button
     * Adds a new ingredient to the ListView with a default name. Amount and Unit are
     * set to null. It then selects this ingredient and enables edite mode.
     */
    @FXML
    private void onAddIngredientButton() {
        ingredientListView.getItems().add(new Ingredient("New ingredient"));
        ingredientListView.getSelectionModel().select(
                ingredientListView.getItems().size() - 1
        );

        //if we create a new ingredient we go to edit mode
        onEditIngredientButton();
    }

    /**
     * On action method for the Edit Ingredient button
     * Changes the scene to Edit Ingredient Mode
     */
    @FXML
    private void onEditIngredientButton() {
        if (ingredientListView.getItems().isEmpty()) {
            System.out.println("There is no ingredient to edit.");
            return;
        }
        if (ingredientListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no ingredient selected.");
            return;
        }
        changeIngredientViewEditMode(true);

        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        editIngredientNameField.setText(ingredient.name);
        if (ingredient.amount != null) {
            editIngredientAmountField.setText(String.valueOf(ingredient.amount));
        } else {
            editIngredientAmountField.setText("");
        }
        if (ingredient.unit != null) {
            editUnitBox.setValue(ingredient.unit.name());
        } else {
            editUnitBox.setValue("");
        }
    }

    /**
     * On action method for the Cancel Edit Ingredient button
     */
    @FXML
    private void onCancelEditIngredientButton() {
        changeIngredientViewEditMode(false);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() {
        if(editIngredientNameField.getText().isEmpty()){
            System.out.println("A name is required.");
            return;
        }

        if (!editUnitBox.getValue().equals("TO_TASTE")
                && editIngredientAmountField.getText().isEmpty()) {
            System.out.println("This unit needs an amount.");
            return;
        }
        if (editUnitBox.getValue().equals("TO_TASTE")
                && !editIngredientAmountField.getText().isEmpty()) {
            System.out.println("This unit cannot have an amount.");
            return;
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = ingredientListView.getItems().get(index);

        ingredient.name = editIngredientNameField.getText();
        if (!editIngredientAmountField.getText().isEmpty()) {
            ingredient.amount = Double.parseDouble(editIngredientAmountField.getText());
        } else {
            ingredient.amount = null;
        }
        if (!editUnitBox.getValue().isEmpty()) {
            ingredient.unit = Unit.valueOf(editUnitBox.getValue());
        } else {
            ingredient.unit = null;
        }
        ingredientListView.getItems().set(index, ingredient);

        changeIngredientViewEditMode(false);
    }
}
