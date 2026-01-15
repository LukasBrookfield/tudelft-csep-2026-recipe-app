package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.IngredientType;
import commons.ShoppingListItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShoppingListCtrl {

    private final ServerUtility server;

    private final UserConfig user;

    private final MainCtrl mainCtrl;

    @FXML
    public ChoiceBox<String> sceneBox;

    private final ShoppingListUtils shoppingListUtils;

    // Root

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<ShoppingListItem> ingredientListView;

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

    @FXML
    private StackPane editPane;

    @FXML
    private HBox editIngredientBox;

    @FXML
    private HBox editIngredientTypeBox;

    @FXML
    private ChoiceBox<IngredientType> editIngredientChoiceBox;

    @FXML
    private Button nextButton;

    @FXML
    private Label shoppingListHeaderLabel;

    @FXML
    private Button backEditIngredientButton;

    private final LanguageService languages;

    private static final String CREATE_NEW_INGREDIENT_TYPE = "Create new ingredient type";
    private boolean newIngredientType = false;

    //the listener for ingredient type choice box so the fields change
    private final ChangeListener<IngredientType> ingredientListener =
            (observable, oldValue, newValue) -> {
                if (newValue != null &&
                        CREATE_NEW_INGREDIENT_TYPE.equals(newValue.name)) {

                    editIngredientNameField.setDisable(false);
                    editIngredientNameField.clear();
                } else if (newValue != null) {
                    editIngredientNameField.setDisable(true);
                    editIngredientNameField.setText(newValue.name);
                }
            };

    @Inject
    public ShoppingListCtrl(ServerUtility server,
                            UserConfig user,
                            MainCtrl mainCtrl,
                            LanguageService languages,
                            ShoppingListUtils shoppingListUtils) {
        this.server = server;
        this.user = user;
        this.mainCtrl = mainCtrl;
        this.languages = languages;
        this.shoppingListUtils = shoppingListUtils;
    }

    public ShoppingListCtrl(ServerUtility server,
                            UserConfig user,
                            MainCtrl mainCtrl,
                            ShoppingListUtils shoppingListUtils) {
        this(server, user, mainCtrl, LanguageService.defaultService(), shoppingListUtils);
    }

    private void setTooltip(Control c, String key) {
        ResourceBundle b = languages.bundle();
        Tooltip t = c.getTooltip();
        if (t == null) {
            t = new Tooltip();
            c.setTooltip(t);
        }
        t.setText(b.getString(key));
    }

    public void applyTranslations() {
        ResourceBundle b = languages.bundle();

        shoppingListHeaderLabel.setText(b.getString("shopping.title"));

        editIngredientNameField.setPromptText(b.getString("common.field.ingredientType.prompt"));
        editIngredientAmountField.setPromptText(b.getString("common.field.amount.prompt"));

        nextButton.setText(b.getString("common.btn.next"));
        backEditIngredientButton.setText(b.getString("common.btn.back"));
        cancelEditIngredientButton.setText(b.getString("common.btn.cancel"));
        doneEditIngredientButton.setText(b.getString("common.btn.done"));

        setTooltip(removeIngredientButton, "common.tooltip.removeIngredient");
        setTooltip(addIngredientButton, "common.tooltip.addIngredient");
        setTooltip(editIngredientButton, "common.tooltip.editIngredient");

        editIngredientChoiceBox.setConverter(new StringConverter<>() {
            @Override public String toString(IngredientType it) {
                if (it == null) return "";
                if (CREATE_NEW_INGREDIENT_TYPE.equals(it.name)) return b.getString("common.ingredientType.createNew");
                return it.name;
            }
            @Override public IngredientType fromString(String s) { return null; }
        });
    }

    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed
     */
    @FXML
    private void onRefresh() {
        editIngredientAmountField.clear();
        editIngredientNameField.clear();

        ingredientListView.refresh();
    }

    /**
     * Public method activated each time when shopping list is accessed.
     * It updates the shopping list and sets the fields
     */
    @FXML
    public void set(){
        ingredientListView.getItems().clear();
        ingredientListView.getItems().addAll(new ArrayList<>(user.getShoppingList()));
        onRefresh();
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     * @param value false for viewing mode, true for editing mode
     */
    private void changeIngredientViewEditMode(boolean value) {
        removeIngredientButton.setVisible(!value);
        addIngredientButton.setVisible(!value);
        editIngredientButton.setVisible(!value);
        editPane.setVisible(value);
        removeIngredientButton.getParent().setMouseTransparent(value);
        changeIngredientTypeViewEditMode(false);
        ingredientListView.setDisable(value);
        sceneBox.getParent().setDisable(value);
    }

    /**
     * Changes the scene between editing ingredient and ingredient type
     * @param value false for type mode, true for ingredient mode
     */
    private void changeIngredientTypeViewEditMode(boolean value) {
        editIngredientTypeBox.setVisible(!value);
        editIngredientBox.setVisible(value);
        editIngredientTypeBox.setManaged(value);
    }

    /**
     * Initializes the shopping list screen with default values
     */
    @FXML
    private void initialize() {
        changeIngredientViewEditMode(false);

        sceneBox.getItems().addAll("Home", "Recipe overview", "Ingredient overview",
                "Shopping list");
        editUnitBox.getItems().addAll("Select a unit", "G", "KG", "ML", "L", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        ingredientListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldIngredient, newIngredient) -> onRefresh()
        );

        // Makes it so that the 'Add Recipe' button is selected when the app gets
        // started
        Platform.runLater(() -> addIngredientButton.requestFocus());

        ingredientListView.getItems().addAll(user.getShoppingList());
        applyTranslations();

        sceneBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> {
                    mainCtrl.showScene(newValue);
                });

        ingredientListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ShoppingListItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(shoppingListUtils.shoppingListItemString(item));
                }
            }
        });
    }

    /**
     * Download button:
     * Asks the user where to save the PDF, then writes the shopping list PDF there.
     */
    @FXML
    private void onDownloadButton() {
        FileChooser chooser = new FileChooser();
        shoppingListUtils.setFileChooser(chooser);
        // Show the dialog
        File file = chooser.showSaveDialog(rootPane.getScene().getWindow());
        shoppingListUtils.saveListToFile(file, user.getShoppingList());
    }

    /**
     * Print button:
     * Creates a temporary PDF file and sends it to the OS printer.
     */
    @FXML
    private void onPrintButton() {
        shoppingListUtils.printShoppingList(new  ArrayList<>(ingredientListView.getItems()));
    }

    /**
     * Resets the shopping list
     */
    @FXML
    private void onResetButton(){
        ingredientListView.getItems().clear();
        user.getShoppingList().clear();
        user.saveUser();
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

        user.setShoppingList(new ArrayList<>(ingredientListView.getItems()));
        user.saveUser();
    }

    /**
     * On action method for the Add Ingredient button
     * Adds a new ingredient to the ListView with a default name. Amount and Unit are
     * set to null. It then selects this ingredient and enables edite mode.
     */
    @FXML
    private void onAddIngredientButton() {
        newIngredientType = true;
        ingredientListView.getItems().add(new ShoppingListItem(new Ingredient(null, null, null, null)));
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
        //we remove the listener so there would be no errors while updating the ingredient type choice box
        editIngredientChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .removeListener(ingredientListener);

        //sets the values to the ingredient type choice box
        editIngredientChoiceBox.getItems().setAll(
                new IngredientType(CREATE_NEW_INGREDIENT_TYPE, null,
                        null, null)
        );
        editIngredientChoiceBox.getSelectionModel().select(0);
        editIngredientChoiceBox.getItems().addAll(server.getIngredientTypes());

        if(ingredientListView.getItems().isEmpty()){
            System.out.println("There is no ingredient to edit.");
            return;
        }
        if(ingredientListView.getSelectionModel().getSelectedItem() == null){
            System.out.println("There is no ingredient selected.");
            return;
        }
        changeIngredientViewEditMode(true);

        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem().getIngredient();

        if (ingredient.ingredientType != null) {
            editIngredientChoiceBox.setValue(ingredient.ingredientType);
            editIngredientNameField.setText(ingredient.ingredientType.name);
        } else {
            editIngredientChoiceBox.getSelectionModel().select(0);
            editIngredientNameField.setText("");
        }

        //adds the listener to the ingredient choice box
        editIngredientChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(ingredientListener);

        // Force focus into the IngredientType name box
        Platform.runLater(() -> editIngredientChoiceBox.requestFocus());
    }

    /**
     * On action method for the Cancel Edit Ingredient button
     */
    @FXML
    private void onCancelEditIngredientButton() {
        if(newIngredientType){
            onRemoveIngredientButton();
        }
        newIngredientType = false;
        changeIngredientViewEditMode(false);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() {
        newIngredientType = false;
        if(!shoppingListUtils.ingredientValidation(editIngredientNameField.getText(),
                editIngredientAmountField.getText(),
                editUnitBox.getValue())){
            return;
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = ingredientListView.getItems().get(index).getIngredient();

        //we create a new ingredient type
        if (editIngredientChoiceBox.getValue().name.equals(CREATE_NEW_INGREDIENT_TYPE)) {
            ingredient.ingredientType = server.addIngredientType(
                    new IngredientType(editIngredientNameField.getText(),
                            null, new ArrayList<>(), null)
            );
        } else {
            ingredient.ingredientType = editIngredientChoiceBox.getValue();
        }

        shoppingListUtils.applyEditsToIngredient(ingredient,
                editIngredientNameField.getText(),
                editIngredientAmountField.getText(),
                editUnitBox.getValue());

        changeIngredientViewEditMode(false);

        user.setShoppingList(new ArrayList<>(ingredientListView.getItems()));
        user.saveUser();
        onRefresh();
    }

    /**
     * On action method for the Next Edit Ingredient button
     * Switches the scene to edit ingredient part 2 (which is where the user inputs
     * the unit and amount of the ingredient)
     */
    @FXML
    private void onNext(){
        if (editIngredientNameField.getText().isEmpty()) {
            System.out.println("The ingredient type needs a name.");
            return;
        }

        changeIngredientTypeViewEditMode(true);

        Ingredient ingredient = ingredientListView.getSelectionModel()
                .getSelectedItem().getIngredient();
        if (ingredient.amount != null) {
            editIngredientAmountField.setText(String.valueOf(ingredient.amount));
        }
        if (ingredient.unit != null) {
            editUnitBox.setValue(ingredient.unit.name());
        }
        if (editUnitBox.getValue() == null || editUnitBox.getValue().isEmpty()) {
            editUnitBox.getSelectionModel().select(0);
        }
    }

    /**
     * On action method for the Back Edit Ingredient button
     * Switches the scene back to edit ingredient part 1 (which is where the user
     * enters the name of the ingredient type
     */
    @FXML
    private void onBackEditIngredientButton() {
        changeIngredientTypeViewEditMode(false);
    }
}
