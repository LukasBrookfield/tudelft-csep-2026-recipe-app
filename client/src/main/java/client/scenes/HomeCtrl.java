package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    // Root
    @FXML
    private AnchorPane rootPane;

    // Top bar
    @FXML
    private Button refreshButton;

    // Sidebar
    @FXML
    private TextField recipeSearchField;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Button removeRecipeButton;

    @FXML
    private Button addRecipeButton;

    // Title row
    @FXML
    private Label recipeTitleLabel;

    @FXML
    private TextField recipeTitleField;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Separator mainSeparator;

    // Ingredients
    @FXML
    private Label ingredientsHeaderLabel;

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button editIngredientButton;

    @FXML
    private TextField editIngredientNameField;

    @FXML
    private TextField editIngredientAmountField;

    @FXML
    private ChoiceBox<String> editUnitBox;

    @FXML
    private Button cancelEditIngredientButton;

    @FXML
    private Button doneEditIngredientButton;

    // Preparation
    @FXML
    private Label preparationHeaderLabel;

    @FXML
    private ListView<String> preparationStepListView;

    @FXML
    private Button removeStepButton;

    @FXML
    private Button addStepButton;

    @FXML
    private Button editStepButton;

    @FXML
    private TextField editStepField;

    @FXML
    private Button cancelEditStepButton;

    @FXML
    private Button doneEditStepButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button doneEditButton;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Changes the scene between viewing and editing a recipe
     * @param value false for viewing mode, true for editing mode
     */
    private void changeViewEditMode(boolean value) {
        recipeTitleLabel.setVisible(!value);
        recipeTitleField.setVisible(value);
        editRecipeButton.setVisible(!value);

        removeIngredientButton.getParent().setVisible(value);
        removeStepButton.getParent().setVisible(value);

        cancelEditButton.setVisible(value);
        doneEditButton.setVisible(value);

        addRecipeButton.setVisible(!value);
        removeRecipeButton.setVisible(!value);

        recipeSearchField.setDisable(value);
        recipeListView.setDisable(value);
    }

    /**
     * Changes the scene between viewing and editing the preparation steps
     * @param value false for viewing mode, true for editing mode
     */
    private void changeStepViewEditMode(boolean value) {
        removeStepButton.getParent().setVisible(!value);
        editStepField.getParent().setVisible(value);
        removeStepButton.getParent().setMouseTransparent(value);
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     * @param value false vor viewing mode, true for editing mode
     */
    private void changeIngredientViewEditMode(boolean value) {
        removeIngredientButton.getParent().setVisible(!value);
        editIngredientNameField.getParent().setVisible(value);
        removeIngredientButton.getParent().setMouseTransparent(value);
    }

    /**
     * Sets the title, ingredient list and preparation step list based on which
     * recipe is selected
     */
    private void setLabelsAndFields() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        if (recipe != null) {
            recipeTitleLabel.setText(recipe.name);

            List<Ingredient> ingredients = new ArrayList<>(recipe.ingredients);
            ingredientListView.setItems(FXCollections.observableList(ingredients));

            List<String> steps = new ArrayList<>(recipe.steps);
            preparationStepListView.setItems(FXCollections.observableList(steps));
        }
    }

    /**
     * Initializes the home screen with default values
     */
    @FXML
    private void initialize() {
        changeIngredientViewEditMode(false);
        changeStepViewEditMode(false);
        changeViewEditMode(false);
        recipeTitleField.setVisible(false);

        editUnitBox.getItems().addAll("", "G", "ML", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        onRefresh();

        recipeListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldRecipe, newRecipe) -> {
                    onRefresh();
                }
        );
    }

    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed (button will be
     * removed later)
     */
    @FXML
    private void onRefresh() {
        recipeListView.refresh();

        boolean empty = recipeListView.getItems().isEmpty();
        mainSeparator.getParent().setVisible(!empty);

        setLabelsAndFields();
    }

    /**
     * On action method for the Add Recipe Button
     * Adds an empty recipe (default name, ingredient and step lists are null) and
     * then selects this recipe
     */
    @FXML
    private void onAddRecipe() {
        Recipe recipe = new Recipe("New recipe");

        recipeListView.getItems().add(recipe);
        recipeListView.getSelectionModel().select(
                recipeListView.getItems().size() - 1
        );
    }

    /**
     * On action method for the Remove Recipe button
     * It removes the currently selected recipe
     */
    @FXML
    private void onRemoveRecipe() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        recipeListView.getItems().remove(recipe);
        onRefresh();
    }

    // MAYBE KEEP SOMETHING LIKE THIS FROM THE PROJECT TEMPLATE:
//    public void keyPressed(KeyEvent e) {
//        switch (e.getCode()) {
//            case ENTER:
//                ok();
//                break;
//            case ESCAPE:
//                cancel();
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * On action method for the Edit Recipe button
     * Changes the scene to Edit Mode
     */
    @FXML
    private void onEditRecipeButton() {
        changeViewEditMode(true);
        recipeTitleField.setText(recipeTitleLabel.getText());
    }

    /**
     * On action method for the Remove Ingredient Button
     * Removes the currently selected ingredient (if any), note that a change like
     * this only affects the recipe if the user presses 'Done' later
     */
    @FXML
    private void onRemoveIngredientButton() {
        if (ingredientListView.getItems().isEmpty()) {
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
     * set to null. It then selects this ingredient
     */
    @FXML
    private void onAddIngredientButton() {
        ingredientListView.getItems().add(new Ingredient("New ingredient"));
        ingredientListView.getSelectionModel().select(
                ingredientListView.getItems().size() - 1
        );
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

        cancelEditButton.setVisible(false);
        doneEditButton.setVisible(false);
    }

    /**
     * On action method for the Cancel Edit Ingredient button
     */
    @FXML
    private void onCancelEditIngredientButton() {
        changeIngredientViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() {
        if (editIngredientNameField.getText().isEmpty()) {
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
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Remove Step button
     * Removes the currently selected step (if any)
     */
    @FXML
    private void onRemoveStepButton() {
        if (preparationStepListView.getItems().isEmpty()) {
            System.out.println("There is no preparation step to remove.");
            return;
        }
        if (preparationStepListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no preparation step selected.");
            return;
        }
        int index = preparationStepListView.getSelectionModel().getSelectedIndex();
        preparationStepListView.getItems().remove(index);
    }

    /**
     * On action method for the Add Step button
     * Adds a new step to the ListView with a default name
     */
    @FXML
    private void onAddStepButton() {
        preparationStepListView.getItems().add("New step");
        preparationStepListView.getSelectionModel().select(
                preparationStepListView.getItems().size() - 1
        );
    }

    /**
     * On action method for the Edit Step button
     */
    @FXML
    private void onEditStepButton() {
        if (preparationStepListView.getItems().isEmpty()) {
            System.out.println("There is no preparation step to edit.");
            return;
        }
        if (preparationStepListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no preparation step selected.");
            return;
        }
        changeStepViewEditMode(true);

        String step = preparationStepListView.getSelectionModel().getSelectedItem();
        editStepField.setText(step);

        cancelEditButton.setVisible(false);
        doneEditButton.setVisible(false);
    }

    /**
     * On action method for the Cancel Edit Step button
     */
    @FXML
    private void onCancelEditStepButton() {
        changeStepViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Done Edit Step button
     */
    @FXML
    private void onDoneEditStepButton() {
        if (editStepField.getText().isEmpty()) {
            System.out.println("The step cannot be empty.");
            return;
        }
        int index = preparationStepListView.getSelectionModel().getSelectedIndex();
        preparationStepListView.getItems().set(index, editStepField.getText());

        changeStepViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Cancel Edit Button
     * The original recipe is not changed
     */
    @FXML
    private void onCancelEditButton() {
        onRefresh();
        changeViewEditMode(false);
    }

    /**
     * On action method for the Done Edit button
     * All the changes are added to the selected recipe
     */
    @FXML
    private void onDoneEditButton() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        recipe.name = recipeTitleField.getText();
        recipe.ingredients = ingredientListView.getItems();
        recipe.steps = preparationStepListView.getItems();
        onRefresh();
        changeViewEditMode(false);
    }
}