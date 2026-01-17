package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.ShoppingListItem;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;


public class AddToShoppingListCtrl {
    private static final String UNIT_PLACEHOLDER = "__SELECT_UNIT__";

    private final ServerUtils server;
    private final UserConfig user;
    private final MainCtrl controller;
    private Recipe recipe;
    private final ShoppingListUtils shoppingListUtils;

    private ObservableList<IngredientType> allIngredientTypes;
    private FilteredList<IngredientType> filteredIngredientTypes;
    private SortedList<IngredientType> sortedIngredientTypes;

    @FXML
    private ListView<ShoppingListItem> ingredientListView;

    @FXML
    private Button removeIngredientButton;

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
    private HBox editIngredientTypeContainer;

    @FXML
    private ComboBox<IngredientType> editIngredientTypeBox;

    @FXML
    private Button nextButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button confirmation;

    @FXML
    private HBox addRemoveEditHBox;

    @FXML
    private Label label;

    @FXML
    private Button backEditIngredientButton;

    private final LanguageService languages;

    private boolean newIngredientType = false;

    private void setTooltip(Control c, String key){
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

        ingredientListView.refresh();

        editUnitBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String value) {
                if (value == null) return "";
                if (UNIT_PLACEHOLDER.equals(value)) return b.getString("recipe.select.unit");
                return b.getString("recipe.select.unit." + value);
            }

            @Override
            public String fromString(String s) {
                return s;
            }
        });

        label.setText(b.getString("addShopping.title"));
        exitButton.setText(b.getString("addShopping.btn.exit"));
        confirmation.setText(b.getString("addShopping.btn.confirm"));

        editIngredientAmountField.setPromptText(b.getString("common.field.amount.prompt"));

        nextButton.setText(b.getString("common.btn.next"));
        backEditIngredientButton.setText(b.getString("common.btn.back"));
        cancelEditIngredientButton.setText(b.getString("common.btn.cancel"));
        doneEditIngredientButton.setText(b.getString("common.btn.done"));

        setTooltip(removeIngredientButton, "common.tooltip.removeIngredient");
        setTooltip(addIngredientButton, "common.tooltip.addIngredient");
        setTooltip(editIngredientButton, "common.tooltip.editIngredient");
    }
    @Inject
    AddToShoppingListCtrl(ServerUtils server,
                          UserConfig user,
                          MainCtrl controller,
                          LanguageService languages,
                          ShoppingListUtils shoppingListUtils) {
        this.server = server;
        this.user = user;
        this.controller = controller;
        this.languages = languages;
        this.shoppingListUtils = shoppingListUtils;
    }

    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed
     */
    @FXML
    private void onRefresh() {
        editIngredientAmountField.clear();
        editUnitBox.getSelectionModel().select(0);
        ingredientListView.refresh();
        // load all ingredient types from server
        try {
            allIngredientTypes.setAll(server.getIngredientTypes());
        } catch (RuntimeException e) {
            System.out.println("ERROR: Could not load ingredient types from server.");
            e.printStackTrace();
        }
    }

    /**
     * Sets the fields with ingredients from the recipe which was selected
     * @param recipe The recipe
     * @param scale The scale
     */
    public void setFields(Recipe recipe, double scale){
        newIngredientType = false;
        this.recipe = recipe;
        if(recipe != null && recipe.ingredients != null){
            shoppingListUtils.addIngredientsToListView(recipe, ingredientListView.getItems(), scale);
        }
        if(recipe != null && recipe.name != null){
            label.setText("Add to Shopping List - " + recipe.name);
        }
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     * @param value false for viewing mode, true for editing mode
     */
    private void changeIngredientViewEditMode(boolean value) {
        addRemoveEditHBox.setVisible(!value);
        addRemoveEditHBox.setMouseTransparent(value);
        editPane.setVisible(value);
        if (value) changeIngredientTypeViewEditMode(false);
        ingredientListView.setDisable(value);
        exitButton.getParent().setDisable(value);
    }

    /**
     * Changes the scene between editing ingredient and ingredient type
     * @param value false for type mode, true for ingredient mode
     */
    private void changeIngredientTypeViewEditMode(boolean value) {
        if (!value) {
            editIngredientTypeBox.show();
        } else {
            editIngredientTypeBox.hide();
        }
        editIngredientTypeBox.getSelectionModel().clearSelection();
        editIngredientTypeContainer.setVisible(!value);
        editIngredientBox.setVisible(value);
        editIngredientTypeContainer.setManaged(value);
    }

    /**
     * Initializes the shopping list screen with default values
     */
    @FXML
    private void initialize() {
        // load all ingredient types from server
        allIngredientTypes = FXCollections.observableArrayList();
        try {
            allIngredientTypes.setAll(server.getIngredientTypes());
        } catch (RuntimeException e) {
            System.out.println("ERROR: Could not load ingredient types from server.");
            e.printStackTrace();
        }

        // setup filtered and sorted ingredient types
        filteredIngredientTypes = new FilteredList<>(allIngredientTypes);
        sortedIngredientTypes = new SortedList<>(filteredIngredientTypes);
        editIngredientTypeBox.setItems(sortedIngredientTypes);

        editIngredientTypeBox.visibleRowCountProperty().bind(
                Bindings.min(5, Bindings.size(editIngredientTypeBox.getItems()))
        );

        editIngredientTypeBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            Platform.runLater(() -> {
                // apply the filter
                filteredIngredientTypes.setPredicate(item -> {
                    if (newValue == null || newValue.isBlank()) return true;
                    return item.name.toLowerCase().contains(newValue.toLowerCase());
                });

                editIngredientTypeBox.getSelectionModel().clearSelection();
                editIngredientTypeBox.hide();
                // show box while typing
                if (editIngredientTypeBox.getParent().isVisible()) editIngredientTypeBox.show();
            });
        });

        ingredientListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ShoppingListItem> call(ListView<ShoppingListItem> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ShoppingListItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            String displayText = item.toString(languages.bundle());
                            setText(displayText);
                        }
                    }
                };
            }
        });

        changeIngredientViewEditMode(false);
        editUnitBox.getItems().addAll(UNIT_PLACEHOLDER, "G", "KG", "ML", "L", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        ingredientListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldIngredient, newIngredient) -> onRefresh()
        );

        // Makes it so that the 'Add Recipe' button is selected when the app gets
        // started
        Platform.runLater(() -> addIngredientButton.requestFocus());

        applyTranslations();

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

        editIngredientTypeBox.setEditable(true);
        editIngredientTypeBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object o) {
                if (o == null) return "";
                return ((IngredientType) o).toString();
            }
            @Override
            public Object fromString(String s) {
                return allIngredientTypes.stream()
                        .filter(x -> x.name.equalsIgnoreCase(s))
                        .findFirst()
                        .orElseGet(() -> new IngredientType(s, null, new ArrayList<>(), null));
            }
        });

        // setup ingredient type sorting (alphabetical order)
        sortedIngredientTypes.setComparator((obj1, obj2) -> {
            String n1 = obj1.name == null ? "" : obj1.name.toLowerCase();
            String n2 = obj2.name == null ? "" : obj2.name.toLowerCase();
            return n1.compareTo(n2);
        });
    }

    /**
     * On action method for the Remove Ingredient Button
     * Removes the currently selected ingredient (if any)
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
        allIngredientTypes.remove(
                ingredientListView.getSelectionModel().getSelectedItem().getIngredient().ingredientType);
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
        //sets the values to the ingredient type choice box
        ShoppingListItem item = ingredientListView.getSelectionModel().getSelectedItem();
        if (item.getIngredient().ingredientType != null) {
            editIngredientTypeBox.setValue(item.getIngredient().ingredientType);
        } else {
            editIngredientTypeBox.getSelectionModel().clearSelection();
            editIngredientTypeBox.setValue(null);
        }

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
        editIngredientTypeBox.hide();
        onRefresh();
    }

    /**
     * On action method for the Done Edit Ingredient button
     */
    @FXML
    private void onDoneEditIngredientButton() {
        newIngredientType = false;
        if(!shoppingListUtils.ingredientValidation(editIngredientTypeBox.getValue().name,
                editIngredientAmountField.getText(),
                editUnitBox.getValue())){
            return;
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = ingredientListView.getItems().get(index).getIngredient();

        // Set Ingredient Type
        ingredient.ingredientType = editIngredientTypeBox.getValue();

        shoppingListUtils.applyEditsToIngredient(ingredient,
                editIngredientTypeBox.getValue().name,
                editIngredientAmountField.getText(),
                editUnitBox.getValue());

        // add new ingredient type to list if it is new
        if (! allIngredientTypes.contains(ingredient.ingredientType)) {
            allIngredientTypes.add(ingredient.ingredientType);
        }

        changeIngredientViewEditMode(false);
        editIngredientTypeBox.hide();
        onRefresh();
    }

    /**
     * On action method for the Next button
     * Allows for choosing the amount of the ingredient
     */
    public void onNext(){
        IngredientType ingredientType = editIngredientTypeBox.getValue();
        ingredientType.name = ingredientType.name.trim();

        if (ingredientType.name.isEmpty()) {
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

    /**
     * On action method for the Exit button
     * Switches the scene back to recipe overview
     */
    @FXML
    private void onExitButton(){
        ingredientListView.getItems().clear();
        controller.showRecipeOverview();
    }

    /**
     * On action method for the confirm button
     * Adds ingredients to Shopping List and closes the overview
     */
    @FXML
    private void onConfirmationButton(){
        shoppingListUtils.confirmAddingIngredients(ingredientListView.getItems(), recipe, user);
        onExitButton();
    }
}
