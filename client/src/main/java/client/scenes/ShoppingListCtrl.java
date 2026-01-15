package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.IngredientType;
import commons.ShoppingListItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

public class ShoppingListCtrl {

    private final ServerUtility server;

    private final UserConfig user;

    private final MainCtrl mainCtrl;

    public boolean lastScene;

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
    private Label shoppingListHeaderLabel;

    @FXML
    private Button backEditIngredientButton;

    private final LanguageService languages;

    private static final String CREATE_NEW_INGREDIENT_TYPE = "Create new ingredient type";

    @Inject
    public ShoppingListCtrl(ServerUtility server,
                            UserConfig user,
                            MainCtrl mainCtrl, LanguageService languages) {
        this.server = server;
        this.user = user;
        this.mainCtrl = mainCtrl;
        this.languages = languages;
    }

    public ShoppingListCtrl(ServerUtility server,
                            UserConfig user,
                            MainCtrl mainCtrl) {
        this(server, user, mainCtrl, LanguageService.defaultService());
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
        exitButton.setText(b.getString("shopping.btn.exit"));

        editIngredientAmountField.setPromptText(b.getString("common.field.amount.prompt"));

        nextButton.setText(b.getString("common.btn.next"));
        backEditIngredientButton.setText(b.getString("common.btn.back"));
        cancelEditIngredientButton.setText(b.getString("common.btn.cancel"));
        doneEditIngredientButton.setText(b.getString("common.btn.done"));

        setTooltip(removeIngredientButton, "common.tooltip.removeIngredient");
        setTooltip(addIngredientButton, "common.tooltip.addIngredient");
        setTooltip(editIngredientButton, "common.tooltip.editIngredient");
    }

    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed
     */
    @FXML
    private void onRefresh() {
        editIngredientAmountField.clear();
        ingredientListView.refresh();

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
        if (value) changeIngredientTypeViewEditMode(false);
        ingredientListView.setDisable(value);
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

        editIngredientTypeBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            Platform.runLater(() -> {
                // apply the filter
                filteredIngredientTypes.setPredicate(item -> {
                    if (newValue == null || newValue.isBlank()) return true;
                    return item.name.toLowerCase().contains(newValue.toLowerCase());
                });

                editIngredientTypeBox.getSelectionModel().clearSelection();
                editIngredientTypeBox.show();
            });
        });

        changeIngredientViewEditMode(false);
        editUnitBox.getItems().addAll("", "G", "ML", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        ingredientListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldIngredient, newIngredient) -> onRefresh()
        );

        // Makes it so that the 'Add Recipe' button is selected when the app gets
        // started
        Platform.runLater(() -> addIngredientButton.requestFocus());

        ingredientListView.getItems().addAll(user.getShoppingList());
        applyTranslations();

        ingredientListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ShoppingListItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(ShoppingListService.shoppingListItemString(item));
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
                        .filter(x -> x.name.equals(s))
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
        allIngredientTypes.remove(
                ingredientListView.getSelectionModel().getSelectedItem().getIngredient().ingredientType);
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
        }

        if(ingredientListView.getItems().isEmpty()){
            System.out.println("There is no ingredient to edit.");
            return;
        }
        if(ingredientListView.getSelectionModel().getSelectedItem() == null){
            System.out.println("There is no ingredient selected.");
            return;
        }

        // remove duplicates in combo box
        List<IngredientType> newList = new ArrayList<>(new HashSet<>(allIngredientTypes));
        allIngredientTypes = FXCollections.observableList(newList);

        changeIngredientViewEditMode(true);

        // Force focus into the IngredientType name box
        Platform.runLater(() -> editIngredientTypeBox.requestFocus());
    }

    /**
     * On action method for the Cancel Edit Ingredient button
     */
    @FXML
    private void onCancelEditIngredientButton() {
        changeIngredientViewEditMode(false);
        editIngredientTypeBox.hide();
        onRefresh();
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() {
        if(!ShoppingListService.ingredientValidation(editIngredientTypeBox.getValue().name,
                editIngredientAmountField.getText(),
                editUnitBox.getValue())){
            return;
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = ingredientListView.getItems().get(index).getIngredient();

        // set ingredient type
        ingredient.ingredientType = editIngredientTypeBox.getValue();

        ShoppingListService.applyEditsToIngredient(ingredient,
                editIngredientTypeBox.getValue().name,
                editIngredientAmountField.getText(),
                editUnitBox.getValue());

        allIngredientTypes.add(ingredient.ingredientType);

        changeIngredientViewEditMode(false);
        editIngredientTypeBox.hide();

        user.setShoppingList(new ArrayList<>(ingredientListView.getItems()));
        user.saveUser();
        onRefresh();
    }

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

    @FXML
    private void onExitButton() {
        if(lastScene == false) {
            mainCtrl.showHomeScreen();
        } else {
            mainCtrl.showRecipeOverview();
        }
    }
}
