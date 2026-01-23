package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.*;
import commons.Ingredient;
import commons.IngredientType;
import commons.ShoppingListItem;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    private static final String UNIT_PLACEHOLDER = "__SELECT_UNIT__";

    private final ServerUtility server;

    private final RecipeUtils recipeUtils;

    private final UserConfig user;

    private final MainCtrl mainCtrl;

    @FXML
    public ChoiceBox<String> sceneBox;

    private ObservableList<IngredientType> allIngredientTypes;
    private FilteredList<IngredientType> filteredIngredientTypes;
    private SortedList<IngredientType> sortedIngredientTypes;

    private final ShoppingListUtils shoppingListUtils;

    // Root

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<ShoppingListEntry> ingredientListView;

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
    private Button backEditIngredientButton;

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
    private Label shoppingListHeaderLabel;
    
    @FXML
    private Button printButton;

    @FXML
    private Button downloadButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button sortButton;

    private final LanguageService languages;

    private boolean newIngredientType = false;
    private int sortingOption = 1;

    @Inject
    public ShoppingListCtrl(ServerUtility server,
                            RecipeUtils recipeUtils,
                            UserConfig user,
                            MainCtrl mainCtrl,
                            LanguageService languages,
                            ShoppingListUtils shoppingListUtils) {
        this.server = server;
        this.recipeUtils = recipeUtils;
        this.user = user;
        this.mainCtrl = mainCtrl;
        this.languages = languages;
        this.shoppingListUtils = shoppingListUtils;
    }

    public ShoppingListCtrl(ServerUtility server,
                            RecipeUtils recipeUtils,
                            UserConfig user,
                            MainCtrl mainCtrl,
                            ShoppingListUtils shoppingListUtils) {
        this(server, recipeUtils, user, mainCtrl,
                LanguageService.defaultService(), shoppingListUtils);
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

        sceneBox.getItems().setAll(
                b.getString("home.btn.home"),
                b.getString("home.btn.recipeOverview"),
                b.getString("home.btn.ingredientOverview"),
                b.getString("home.btn.shoppingList"));
        if (rootPane != null && rootPane.getScene() != null && rootPane.getScene().getWindow() != null
                && rootPane.getScene().getWindow().isShowing()) sceneBox.getSelectionModel().select(3);

        shoppingListHeaderLabel.setText(b.getString("shopping.title"));

        editIngredientAmountField.setPromptText(b.getString("common.field.amount.prompt"));

        nextButton.setText(b.getString("common.btn.next"));
        backEditIngredientButton.setText(b.getString("common.btn.back"));
        cancelEditIngredientButton.setText(b.getString("common.btn.cancel"));
        doneEditIngredientButton.setText(b.getString("common.btn.done"));
        downloadButton.setText(b.getString("common.btn.download"));
        resetButton.setText(b.getString("shopping.btn.reset"));
        printButton.setText(b.getString("common.btn.print"));

        setTooltip(removeIngredientButton, "common.tooltip.removeIngredient");
        setTooltip(addIngredientButton, "common.tooltip.addIngredient");
        setTooltip(editIngredientButton, "common.tooltip.editIngredient");

        try {
            sortButton.setText(b.getString("shopping.btn.sorting." + sortingOption));
        }catch (Exception e){
            sortButton.setText("No translation in this language: " + sortingOption);
        }

        applySort();
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
     * Public method activated each time when shopping list is accessed.
     * It updates the shopping list and sets the fields
     */
    @FXML
    public void set(){
        ingredientListView.getItems().clear();
        ingredientListView.getItems().addAll(new ArrayList<>(user.getShoppingList()));
        applySort();
        onRefresh();
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     * @param value false for viewing mode, true for editing mode
     */
    private void changeIngredientViewEditMode(int value) {
        editIngredientTypeBox.setVisible(value == 1);
        removeIngredientButton.getParent().setVisible(value == 0);
        cancelEditIngredientButton.getParent().setVisible(value == 1);
        editIngredientAmountField.getParent().setVisible(value == 2);
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

        changeIngredientViewEditMode(0);

        editUnitBox.getItems().addAll(UNIT_PLACEHOLDER, "G", "KG", "ML", "L", "TBSP", "TSP", "PINCH",
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
                    mainCtrl.showScene(sceneBox.getItems().indexOf(newValue));
                });

        ingredientListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ShoppingListEntry item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                } else {
                    if(item.getClass().equals(ShoppingListItem.class)) {
                        ShoppingListItem shoppingListItem = (ShoppingListItem) item;
                        setText(shoppingListUtils.shoppingListItemString(shoppingListItem));
                        setStyle(null);
                    }
                    if(item.getClass().equals(ShoppingListHeader.class)) {
                        ShoppingListHeader shoppingListHeader = (ShoppingListHeader) item;
                        setText(shoppingListUtils.shoppingListHeaderString(shoppingListHeader));
                        setStyle(
                                "-fx-background-color: #3a3a3a;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-weight: bold;"
                        );
                    }
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

        // activate buttons
        activateButtons(EditMode.NO_EDIT);
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
        shoppingListUtils.saveListToFile(file, ingredientListView.getItems());
    }

    /**
     * Print button:
     * Creates a temporary PDF file and sends it to the OS printer.
     */
    @FXML
    private void onPrintButton() {
        shoppingListUtils.printShoppingList(ingredientListView.getItems());
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
     * Increases sorting option and sorts listview
     */
    @FXML
    private void onSortButton(){
        sortingOption = (sortingOption)%3 + 1;

        ResourceBundle b = languages.bundle();

        try {
            sortButton.setText(b.getString("shopping.btn.sorting." + sortingOption));
        }catch(Exception e){
            sortButton.setText("No translation in this language: " + sortingOption);
        }
        applySort();
    }

    /**
     * Sorts the listview based on current sortingOption
     */
    private void applySort(){
        shoppingListUtils.sort(ingredientListView.getItems(), sortingOption);
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

        if(ingredientListView.getSelectionModel().getSelectedItem().getClass().equals(ShoppingListItem.class)) {
            ShoppingListItem shoppingListItem = (ShoppingListItem) ingredientListView.getSelectionModel().getSelectedItem();
            allIngredientTypes.remove(
                    shoppingListItem.getIngredient().ingredientType);
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        ingredientListView.getItems().remove(index);

        applySort();

        user.setShoppingList(shoppingListUtils.getShoppingListItems(ingredientListView.getItems()));
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

        if(ingredientListView.getItems().isEmpty()){
            System.out.println("There is no ingredient to edit.");
            return;
        }
        if(ingredientListView.getSelectionModel().getSelectedItem() == null){
            System.out.println("There is no ingredient selected.");
            return;
        }

        //sets the values to the ingredient type choice box
        ShoppingListItem item;

        if(ingredientListView.getSelectionModel().getSelectedItem().getClass().equals(ShoppingListItem.class)) {
            item = (ShoppingListItem) ingredientListView.getSelectionModel().getSelectedItem();
        }else{
            return;
        }

        if (item.getIngredient().ingredientType != null) {
            editIngredientTypeBox.setValue(item.getIngredient().ingredientType);
        } else {
            editIngredientTypeBox.getSelectionModel().clearSelection();
            editIngredientTypeBox.setValue(null);
        }

        changeIngredientViewEditMode(1);
        editIngredientTypeBox.requestFocus();

        // activate buttons
        activateButtons(EditMode.EDIT_1);
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
        changeIngredientViewEditMode(0);
        editIngredientTypeBox.hide();
        onRefresh();

        // change button behaviour for edit state
        activateButtons(EditMode.NO_EDIT);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
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
        ShoppingListEntry shoppingListEntry = ingredientListView.getItems().get(index);
        Ingredient ingredient;
        if(!shoppingListEntry.getClass().equals(ShoppingListItem.class)) {
            return;
        }else{
            ingredient = ((ShoppingListItem) shoppingListEntry).getIngredient();
        }

        // set ingredient type
        ingredient.ingredientType = editIngredientTypeBox.getValue();

        shoppingListUtils.applyEditsToIngredient(ingredient,
                editIngredientTypeBox.getValue().name,
                editIngredientAmountField.getText(),
                editUnitBox.getValue());

        // add new ingredient type to list if it is new
        if (! allIngredientTypes.contains(ingredient.ingredientType)) {
            allIngredientTypes.add(ingredient.ingredientType);
        }

        changeIngredientViewEditMode(0);

        user.setShoppingList(shoppingListUtils.getShoppingListItems(ingredientListView.getItems()));
        user.saveUser();
        applySort();
        onRefresh();

        // change button behaviour for edit state
        activateButtons(EditMode.NO_EDIT);
    }

    /**
     * On action method for the Next Edit Ingredient button
     * Switches the scene to edit ingredient part 2 (which is where the user inputs
     * the unit and amount of the ingredient)
     */
    @FXML
    private void onNext(){
        IngredientType ingredientType = editIngredientTypeBox.getValue();
        ingredientType.name = ingredientType.name.trim();
        if (ingredientType.name.isEmpty()) {
            System.out.println("The ingredient type needs a name.");
            recipeUtils.displayAlertWarning("ingredient.warning.empty.name", null);
            return;
        }

        changeIngredientViewEditMode(2);
        Ingredient ingredient;
        ShoppingListEntry shoppingListEntry = ingredientListView.getSelectionModel().getSelectedItem();
        if(shoppingListEntry.getClass().equals(ShoppingListItem.class)) {
            ingredient = ((ShoppingListItem) shoppingListEntry).getIngredient();
        }else{
            return;
        }

        if (ingredient.amount != null) {
            editIngredientAmountField.setText(String.valueOf(ingredient.amount));
        }
        if (ingredient.unit != null) {
            editUnitBox.setValue(ingredient.unit.name());
        }
        if (editUnitBox.getValue() == null || editUnitBox.getValue().isEmpty()) {
            editUnitBox.getSelectionModel().select(0);
        }

        // change button behaviour
        activateButtons(EditMode.EDIT_2);
    }

    /**
     * On action method for the Back Edit Ingredient button
     * Switches the scene back to edit ingredient part 1 (which is where the user
     * enters the name of the ingredient type
     */
    @FXML
    private void onBackEditIngredientButton() {
        changeIngredientViewEditMode(1);

        // change button behaviour
        activateButtons(EditMode.EDIT_1);
    }

    // possible ingredient edit states
    private enum EditMode {
        NO_EDIT,
        EDIT_1,
        EDIT_2,
    }

    private void activateButtons(EditMode mode) {
        switch (mode) {
            case NO_EDIT -> {
                // edit ingredient - first step
                cancelEditIngredientButton.setCancelButton(false);
                nextButton.setDefaultButton(false);
                // edit ingredient - second step
                backEditIngredientButton.setCancelButton(false);
                doneEditIngredientButton.setDefaultButton(false);
            }
            case EDIT_1 -> {
                // edit ingredient - first step
                cancelEditIngredientButton.setCancelButton(true);
                nextButton.setDefaultButton(true);
                // edit ingredient - second step
                backEditIngredientButton.setCancelButton(false);
                doneEditIngredientButton.setDefaultButton(false);
            }
            case EDIT_2 -> {
                // edit ingredient - first step
                cancelEditIngredientButton.setCancelButton(false);
                nextButton.setDefaultButton(false);
                // edit ingredient - second step
                backEditIngredientButton.setCancelButton(true);
                doneEditIngredientButton.setDefaultButton(true);
            }
        }
    }
}

