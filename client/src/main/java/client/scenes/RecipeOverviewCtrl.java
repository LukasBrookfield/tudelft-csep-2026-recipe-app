package client.scenes;

import client.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import commons.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.function.Predicate;

import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;

public class RecipeOverviewCtrl {
    // Constants
    private static final String EMPTY_STAR = "☆";
    private static final String FULL_STAR = "★";
    private static final String UNIT_PLACEHOLDER = "__SELECT_UNIT__";

    private final ServerUtility server;

    private final RecipeUtils recipeUtils;

    private UserConfig user;

    private final MainCtrl mainCtrl;

    private Predicate<Recipe> defaultPredicate = recipe -> true;
    private Predicate<Recipe> searchPredicate = recipe -> true;
    private Predicate<Recipe> favoritePredicate = recipe -> true;
    private Predicate<Recipe> ENPredicate = recipe -> true;
    private Predicate<Recipe> NLPredicate = recipe -> true;
    private Predicate<Recipe> PTPredicate = recipe -> true;

    private ObservableList<Recipe> allRecipes;
    private FilteredList<Recipe> filteredRecipes;
    private SortedList<Recipe> sortedRecipes;

    private ObservableList<IngredientType> allIngredientTypes;
    private FilteredList<IngredientType> filteredIngredientTypes;
    private SortedList<IngredientType> sortedIngredientTypes;

    boolean newRecipe = false;
    boolean newIngredient = false;
    boolean newStep = false;
    private Long newRecipeId = null;

    private final IngredientScaling ingredientScaling;
    private final ScaleFactorParser scaleFactorParser;
    private final QuantityFormatter servingsFormatter;

    private double scaleFactor = 1.0;

    private RecipeNutrition lastNutrition = null;

    private final SearchUtils searchUtils;

    // Root

    @FXML
    private AnchorPane rootPane;

    // Left Sidebar

    @FXML
    private TextField recipeSearchField;

    @FXML
    private MenuButton filterBox;

    @FXML
    private CheckMenuItem showFavorites;

    @FXML
    private CheckMenuItem showEN;

    @FXML
    private CheckMenuItem showNL;

    @FXML
    private CheckMenuItem showPT;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Button removeRecipeButton;

    @FXML
    private Button addRecipeButton;

    @FXML
    private Button cloneRecipeButton;

    // Top right

    @FXML
    private Button starRecipeButton;

    @FXML
    private Tooltip starTooltip;

    @FXML
    private Button addToShoppingListButton;

    @FXML
    private Button downloadRecipeButton;

    @FXML
    private Button printRecipeButton;

    @FXML
    public ChoiceBox<String> sceneBox;

    // Recipe title row

    @FXML
    private Label recipeTitleLabel;

    @FXML
    private TextField recipeTitleField;

    @FXML
    private ImageView recipeLanguage;

    @FXML
    private Tooltip languageToolTip;

    @FXML
    private ComboBox<ImageView> recipeLanguageBox;

    @FXML
    private ImageView ENFlag;

    @FXML
    private ImageView NLFlag;

    @FXML
    private ImageView PTFlag;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button doneEditButton;

    @FXML
    private Separator mainSeparator;

    // Details

    @FXML
    private Label servingsCaptionLabel;

    @FXML
    private Label servingsLabel;

    @FXML
    private Label kcalCaptionLabel;

    @FXML
    private Label recipeKcalPer100gLabel;

    @FXML
    private Button editDetailsButton;

    @FXML
    private TextField editServingsField;

    @FXML
    private TextField scaleFactorField;

    @FXML
    private Button cancelEditDetailsButton;

    @FXML
    private Button doneEditDetailsButton;

    // Ingredients

    @FXML
    private Label ingredientsHeaderLabel;

    @FXML
    private ListView<Ingredient> ingredientListView;

    // Edit ingredient section

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button editIngredientButton;

    // Edit ingredient step 1

    @FXML
    private ComboBox<IngredientType> editIngredientTypeBox;

    @FXML
    private Button cancelEditIngredientButton;

    @FXML
    private Button nextEditIngredientButton;

    // Edit ingredient step 2

    @FXML
    private TextField editIngredientAmountField;

    @FXML
    private ChoiceBox<String> editUnitBox;

    @FXML
    private Button doneEditIngredientButton;

    @FXML
    private Button backEditIngredientButton;

    // Preparation

    @FXML
    private Label preparationHeaderLabel;

    @FXML
    private ListView<String> preparationStepListView;

    // Edit preparation section

    @FXML
    private Button removeStepButton;

    @FXML
    private Button addStepButton;

    @FXML
    private Button editStepButton;

    @FXML
    private TextField editStepField;

    @FXML
    private Button moveStepUpButton;

    @FXML
    private Button moveStepDownButton;

    @FXML
    private Button cancelEditStepButton;

    @FXML
    private Button doneEditStepButton;

    @FXML
    private Label searchStatusLabel;

    @FXML
    private ChoiceBox<String> sortChoiceBox;

    @FXML
    private HBox languagePickerContainer;

    @FXML
    private Label nutriScoreLabel;

    private final LanguageService languages;

    private final Tooltip nutritionTooltip = new Tooltip();
    private final AmountParser amountParser;

    // created this because some parts on the code depend on the exact text these present
    private static final String FILTER_ALL = "All recipes";
    private static final String FILTER_FAV = "Favourites";

    private static final String SORT_ORDER_BY = "Order by";
    private static final String SORT_NAME_AZ = "Name (A-Z)";
    private static final String SORT_KCAL = "Least Kcal/100g first";
    private static final String SORT_NUTRI = "Best Nutri-Score first";
    private static final String SORT_FEWEST_STEPS = "Fewest steps first";
    private static final String SORT_FEWEST_ING = "Fewest ingredients first";

    private static final Map<Character, String> COLOURS = new HashMap<>();
    static {
        COLOURS.put('A', "#038141"); // dark green
        COLOURS.put('B', "#85BB2F"); // light green
        COLOURS.put('C', "#FECB02"); // yellow
        COLOURS.put('D', "#EE8100"); // orange
        COLOURS.put('E', "#E63E11"); // red
    }

    private int baseServings = 1;

    private int matches = 0;

    // General

    private void setTooltip(Control c, String key) {
        ResourceBundle b = languages.bundle();
        Tooltip t = c.getTooltip();
        if (t == null) {
            t = new Tooltip();
            c.setTooltip(t);
        }
        t.setText(b.getString(key));
    }

    private void refreshStartTooltip() {
        ResourceBundle b = languages.bundle();
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        boolean isFav = user.isFavouriteRecipe(selected);
        starTooltip.setText(b.getString(isFav ? "recipe.tooltip.star.remove" : "recipe.tooltip.star.add"));

    }

    private String translate(ResourceBundle b, String key, String fallback) {
        if (b == null) return fallback;
        return b.containsKey(key) ? b.getString(key) : fallback;
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
                && rootPane.getScene().getWindow().isShowing()) sceneBox.getSelectionModel().select(1);

        editUnitBox.setValue(editUnitBox.getValue());
        filterBox.setText(b.getString("recipe.filterBy"));

        if (languageToolTip == null) {
            languageToolTip = new Tooltip();
            languageToolTip.setStyle("-fx-font-size: 13px");
            Tooltip.install(recipeLanguage, languageToolTip);
        }

        downloadRecipeButton.setText(b.getString("common.btn.download"));
        printRecipeButton.setText(b.getString("common.btn.print"));

        recipeSearchField.setPromptText(b.getString("recipe.search.prompt"));
        recipeTitleField.setPromptText(b.getString("recipe.field.name.prompt"));
        editServingsField.setPromptText(b.getString("recipe.field.servings.prompt"));

        servingsCaptionLabel.setText(b.getString("recipe.label.servings"));
        kcalCaptionLabel.setText(b.getString("recipe.label.kcal100"));

        ingredientsHeaderLabel.setText(b.getString("recipe.header.ingredients"));
        preparationHeaderLabel.setText(b.getString("recipe.header.preparation"));

        editIngredientAmountField.setPromptText(b.getString("common.field.amount.prompt"));
        editStepField.setPromptText(b.getString("recipe.field.step.prompt"));

        cancelEditIngredientButton.setText(b.getString("common.btn.cancel"));
        doneEditIngredientButton.setText(b.getString("common.btn.done"));
        cancelEditStepButton.setText(b.getString("common.btn.cancel"));
        nextEditIngredientButton.setText(b.getString("common.btn.next"));
        backEditIngredientButton.setText(b.getString("common.btn.back"));
        doneEditStepButton.setText(b.getString("common.btn.done"));
        cancelEditButton.setText(b.getString("common.btn.cancel"));
        doneEditButton.setText(b.getString("common.btn.done"));
        cancelEditDetailsButton.setText(b.getString("common.btn.cancel"));
        doneEditDetailsButton.setText(b.getString("common.btn.done"));

        showFavorites.setText(b.getString("recipe.show.favorites"));
        showEN.setText(b.getString("recipe.show.en"));
        showNL.setText(b.getString("recipe.show.nl"));
        showPT.setText(b.getString("recipe.show.pt"));

        if (matches == 0) {
            searchStatusLabel.setText(b.getString("recipe.search.zero"));
        } else if (matches == 1) {
            searchStatusLabel.setText(b.getString("recipe.search.one"));
        } else {
            searchStatusLabel.setText(MessageFormat.format(
                    b.getString("recipe.search.multiple"), matches));
        }

        // tooltips
        setTooltip(removeRecipeButton, "common.tooltip.removeRecipe");
        setTooltip(addRecipeButton, "common.tooltip.addRecipe");
        setTooltip(cloneRecipeButton, "common.tooltip.cloneRecipe");
        setTooltip(editRecipeButton, "common.tooltip.editRecipe");
        setTooltip(addToShoppingListButton, "common.tooltip.addToShoppingList");

        setTooltip(removeIngredientButton, "common.tooltip.removeIngredient");
        setTooltip(addIngredientButton, "common.tooltip.addIngredient");
        setTooltip(editIngredientButton, "common.tooltip.editIngredient");

        setTooltip(removeStepButton, "common.tooltip.removeStep");
        setTooltip(addStepButton, "common.tooltip.addStep");
        setTooltip(editStepButton, "common.tooltip.editStep");
        setTooltip(moveStepUpButton, "common.tooltip.moveUp");
        setTooltip(moveStepDownButton, "common.tooltip.moveDown");
        setTooltip(editDetailsButton, "common.tooltip.editServings");

        sortChoiceBox.setConverter(new StringConverter<>() {
            @Override public String toString(String value) {
                if (value == null) return "";
                return switch (value) {
                    case SORT_ORDER_BY -> b.getString("recipe.sort.orderBy");
                    case SORT_NAME_AZ -> translate(b, "recipe.sort.nameAz", SORT_NAME_AZ);
                    case SORT_KCAL -> b.getString("recipe.sort.kcal");
                    case SORT_NUTRI -> b.getString("recipe.sort.nutri");
                    case SORT_FEWEST_STEPS -> b.getString("recipe.sort.fewestSteps");
                    case SORT_FEWEST_ING -> b.getString("recipe.sort.fewestIngredients");
                    default -> value;
                };
            }
            @Override public String fromString(String s) { return s; }
        });

        refreshStartTooltip();
    }

//    @FXML
//    private void switchLanguage() throws JsonProcessingException {
//        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
//        int index = allRecipes.indexOf(recipe);
//
//        String language = recipe.language;
//        List<String> languages = List.of("en", "nl", "pt");
//
//        int i = (languages.indexOf(language) + 1) % 3;
//        while (!user.isSelectedLanguage(languages.get(i))) {
//            i = (i + 1) % 3;
//        }
//
//        recipe.language = languages.get(i);
//        allRecipes.set(index, server.updateRecipe(recipe.id, recipe));
//        onRefresh();
//    }

    @Inject
    public RecipeOverviewCtrl(ServerUtility server,
                              RecipeUtils recipeUtils,
                              UserConfig user,
                              MainCtrl mainCtrl, LanguageService languages,
                              IngredientScaling ingredientScaling,
                              ScaleFactorParser scaleFactorParser,
                              QuantityFormatter servingsFormatter, SearchUtils searchUtils, AmountParser amountParser) {
        this.server = server;
        this.recipeUtils = recipeUtils;
        this.user = user;
        this.mainCtrl = mainCtrl;
        this.searchUtils = searchUtils;
        this.languages = languages;
        this.ingredientScaling = ingredientScaling;
        this.scaleFactorParser = scaleFactorParser;
        this.servingsFormatter = servingsFormatter;
        this.amountParser = amountParser;
    }

    /**
     * Changes the scene between viewing and editing a recipe
     *
     * @param value false for viewing mode, true for editing mode
     */
    private void changeViewEditMode(boolean value) {
        editIngredientTypeBox.hide();
        downloadRecipeButton.getParent().setDisable(value);
        recipeSearchField.getParent().setDisable(value);
        recipeTitleLabel.setVisible(!value);
        recipeTitleField.setVisible(value);
        recipeLanguageBox.setVisible(value);
        starRecipeButton.getParent().setVisible(!value);
        doneEditButton.getParent().setVisible(value);

        editDetailsButton.setVisible(value);
        editIngredientButton.getParent().setVisible(value);
        editStepButton.getParent().setVisible(value);

        // When going into edit mode, it automatically selects the
        // recipe name field
        Platform.runLater(() -> {
            recipeTitleField.requestFocus();
            recipeTitleField.selectAll();
        });
    }

    private void changeDetailsViewEditMode(boolean value) {
        editDetailsButton.getParent().setVisible(!value);
        editServingsField.getParent().setVisible(value);
        removeIngredientButton.getParent().setDisable(value);
        removeStepButton.getParent().setDisable(value);

        cancelEditButton.getParent().setDisable(value);

        Platform.runLater(() -> {
            editServingsField.requestFocus();
        });
    }

    /**
     * Changes the scene between viewing and editing the preparation steps
     *
     * @param value false for viewing mode, true for editing mode
     */
    private void changeStepViewEditMode(boolean value) {
        removeStepButton.getParent().setVisible(!value);
        editStepField.getParent().setVisible(value);
        removeStepButton.getParent().setMouseTransparent(value);
        editDetailsButton.getParent().setDisable(value);
        removeIngredientButton.getParent().setDisable(value);
        preparationStepListView.setDisable(value);

        cancelEditButton.getParent().setDisable(value);

//        setLanguagePickerVisible(!(value && newStep));

        // When going into edit mode, it automatically selects the
        // step name field
        Platform.runLater(() -> {
            editStepField.requestFocus();
            editStepField.selectAll();
        });

    }

    private void setLanguagePickerVisible(boolean visible) {
        if (languagePickerContainer == null) return;

        languagePickerContainer.setVisible(visible);
        languagePickerContainer.setManaged(visible);
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     *
     * @param value 0 for viewing mode, 1 for editing part 1, 2 for editing part 2
     *              Basically part 1 is where the user inputs the name of the ingredient type
     *              and part 2 is where the user inputs the unit and amount of the ingredient
     */
    private void changeIngredientTypeViewEditMode(int value) {
        removeIngredientButton.getParent().setVisible(value == 0);
        editIngredientTypeBox.getParent().setVisible(value == 1);
        if (value == 1) {
            editIngredientTypeBox.show();
        } else {
            editIngredientTypeBox.hide();
        }
        editIngredientAmountField.getParent().setVisible(value == 2);

        removeIngredientButton.getParent().setMouseTransparent(value > 0);
        ingredientListView.setDisable(value > 0);

        editDetailsButton.setDisable(value > 0);
        removeStepButton.getParent().setDisable(value > 0);

        cancelEditButton.getParent().setDisable(value > 0);
    }

    /**
     * On action method for the Move Step Up button
     */
    @FXML
    private void onMoveStepUpButton() {
        moveSelectedStep(-1);
    }

    /**
     * On action method for the Move Step Down button
     */
    @FXML
    private void onMoveStepDownButton() {
        moveSelectedStep(1);
    }

    /**
     * Moves the selected preparation step up or down one step (in the UI and in the actual recipe).
     *
     * @param offset can be -1 or 1, moving, respectively, up or down one step.
     */
    private void moveSelectedStep(int offset) {
        int index = preparationStepListView.getSelectionModel().getSelectedIndex();
        var items = preparationStepListView.getItems();

        // Nothing selected or empty list
        if (index < 0 || items.isEmpty()) {
            System.out.println("There is no preparation step selected to move.");
            return;
        }

        int newIndex = index + offset;

        // Out of bounds (already at the top or bottom)
        if (newIndex < 0 || newIndex >= items.size()) {
            return;
        }

        // Remove and re-insert at new position
        String step = items.remove(index);
        items.add(newIndex, step);

        // Keep the moved item selected
        preparationStepListView.getSelectionModel().select(newIndex);
    }

    /**
     * Sets the title, ingredient list and preparation step list based on which
     * recipe is selected
     */
    private void setLabelsAndFields() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        if (recipe != null) {
            recipeTitleLabel.setText(recipe.name);
            baseServings = recipe.servings;
            updateServingsLabel(baseServings);

            Image image = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(
                            "/flags/" + recipe.language + ".png")));
            recipeLanguage.setImage(image);

            ResourceBundle b = languages.bundle();
            if ("en".equals(recipe.language)) {
                recipeLanguageBox.getSelectionModel().select(0);
                languageToolTip.setText(b.getString("language.tooltip.en"));
            } else if ("nl".equals(recipe.language)) {
                recipeLanguageBox.getSelectionModel().select(1);
                languageToolTip.setText(b.getString("language.tooltip.nl"));
            } else {
                recipeLanguageBox.getSelectionModel().select(2);
                languageToolTip.setText(b.getString("language.tooltip.pt"));
            }

            List<Ingredient> ingredients = new ArrayList<>(recipe.ingredients);
            ingredientListView.setItems(FXCollections.observableList(ingredients));

            List<String> steps = new ArrayList<>(recipe.steps);
            preparationStepListView.setItems(FXCollections.observableList(steps));

            if (user.isFavouriteRecipe(recipe)) {
                starRecipeButton.setText(FULL_STAR);
                starTooltip.setText("Remove recipe from favorites");
            } else {
                starRecipeButton.setText(EMPTY_STAR);
                starTooltip.setText("Add recipe to favorites");
            }
            refreshStartTooltip();
        }

        this.updateNutritionLabels(recipe);
    }

    /**
     * Updates the UI label that shows the Kcals per 100g of a recipe.
     * @param recipe selected (for which the nutrition will be shown).
     */
    private void updateNutritionLabels(Recipe recipe) {
        if (recipe == null) {
            System.out.println("[nutrition] recipe is null");
            recipeKcalPer100gLabel.setText("-");
            return;
        }

        long requestedId = recipe.id;
        System.out.println("[nutrition] requesting for recipe id =" + requestedId);
        recipeKcalPer100gLabel.setText("...");

        new Thread(() -> {  // Start a new thread because the UI thread would freeze due to the server call being slow
            try {
                var n = server.getRecipeNutrition(requestedId); // server call

                System.out.println("[nutrition] got response: totalGrams = " + n.totalGrams() + ", kcalPer100g = "
                        + n.kcalPer100g() + ", ignored = " + n.ignoredIngredients());


                Platform.runLater(() -> {   // Update the UI on the main thread once possible
                    // Just check that the intended recipe is the one being selected
                    // (so we don't update the wrong recipe in the UI)
                    Recipe current = recipeListView.getSelectionModel().getSelectedItem();

                    lastNutrition = n;
                    updateNutritionTooltip();

                    if (current == null || current.id != requestedId) return;

                    if (n.totalGrams() <= 0) {
                        recipeKcalPer100gLabel.setText("-");
                    } else {
                        recipeKcalPer100gLabel.setText(languages.formatInteger(Math.round(n.kcalPer100g())));
                    }
                    updateNutriScoreLabel(n);
                });
            } catch (Exception e) {
                System.out.println("[nutrition] ERROR while loading nutrition:");
                e.printStackTrace();
                Platform.runLater(() -> {
                    recipeKcalPer100gLabel.setText("-");
                });
            }

        }).start();
    }

    private void setShowEN(boolean value) {
        if (value) {
            ENPredicate = defaultPredicate;
            user.addSelectedLanguage("en");
        } else {
            ENPredicate = recipe -> !("en".equals(recipe.language));
            user.removeSelectedLanguage("en");
        }
        user.saveUser();
        updateFilteredList();
    }

    private void setShowNL(boolean value) {
        if (value) {
            NLPredicate = defaultPredicate;
            user.addSelectedLanguage("nl");
        } else {
            NLPredicate = recipe -> !("nl".equals(recipe.language));
            user.removeSelectedLanguage("nl");
        }
        user.saveUser();
        updateFilteredList();
    }

    private void setShowPT(boolean value) {
        if (value) {
            PTPredicate = defaultPredicate;
            user.addSelectedLanguage("pt");
        } else {
            PTPredicate = recipe -> !("pt".equals(recipe.language));
            user.removeSelectedLanguage("pt");
        }
        user.saveUser();
        updateFilteredList();
    }

    /**
     * Initializes the home screen with default values
     */
    @FXML
    private void initialize() {
        changeDetailsViewEditMode(false);
        changeIngredientTypeViewEditMode(0);
        changeStepViewEditMode(false);
        changeViewEditMode(false);
        recipeTitleField.setVisible(false);

        Tooltip.install(kcalCaptionLabel, nutritionTooltip);

        showFavorites = new CheckMenuItem("");
        showEN = new CheckMenuItem("");
        showNL = new CheckMenuItem("");
        showPT = new CheckMenuItem("");
        filterBox.getItems().addAll(showFavorites, showEN, showNL, showPT);

        showFavorites.setOnAction((e) -> {
            if (showFavorites.isSelected()) {
                favoritePredicate = recipe -> user.isFavouriteRecipe(recipe);
            } else {
                favoritePredicate = defaultPredicate;
            }
            updateFilteredList();

            // reapply the search filter, because the amount of matches might change
            applySearchFilter(recipeSearchField.getText());
        });

        showEN.setOnAction((e) -> {
            setShowEN(showEN.isSelected());
            recipeLanguageBox.getItems().setAll(ENFlag, NLFlag, PTFlag);

            // reapply the search filter, because the amount of matches might change
            applySearchFilter(recipeSearchField.getText());
        });

        showNL.setOnAction((e) -> {
            setShowNL(showNL.isSelected());
            recipeLanguageBox.getItems().setAll(ENFlag, NLFlag, PTFlag);

            // reapply the search filter, because the amount of matches might change
            applySearchFilter(recipeSearchField.getText());
        });

        showPT.setOnAction((e) -> {
            setShowPT(showPT.isSelected());
            recipeLanguageBox.getItems().setAll(ENFlag, NLFlag, PTFlag);

            // reapply the search filter, because the amount of matches might change
            applySearchFilter(recipeSearchField.getText());
        });

        applyTranslations();

        editUnitBox.getItems().addAll(UNIT_PLACEHOLDER, "G", "KG", "ML", "L", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");
        editUnitBox.getSelectionModel().select(0);

        ENFlag = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/flags/en.png"))));
        NLFlag = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/flags/nl.png"))));
        PTFlag = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/flags/pt.png"))));

        recipeLanguageBox.getItems().setAll(ENFlag, NLFlag, PTFlag);
        recipeLanguageBox.setCellFactory(cb -> {
            ImageView image = new ImageView();
            return new ListCell<ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    if (item == null || empty) {
                        setGraphic(null);
                        image.setImage(null);
                    } else {
                        super.updateItem(item, empty);
                        image.setImage(item.getImage());
                        image.setFitWidth(20);
                        image.setFitHeight(20);
                        setGraphic(image);

                        boolean disable =
                                (item.equals(ENFlag) && !user.isSelectedLanguage("en"))
                                || (item.equals(NLFlag) && !user.isSelectedLanguage("nl"))
                                || (item.equals(PTFlag) && !user.isSelectedLanguage("pt"));
                        setDisable(disable);
                        setOpacity(disable ? 0.5 : 1);
                    }
                    setText(null);
                }
            };
        });
        recipeLanguageBox.setButtonCell(recipeLanguageBox.getCellFactory().call(null));

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

        editIngredientTypeBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            Platform.runLater(() -> {
                // apply the filter
                filteredIngredientTypes.setPredicate(item -> {
                    if (newValue == null || newValue.isBlank()) return true;
//                    return item.name.toLowerCase().contains(newValue.toLowerCase());
                    return searchUtils.containsNormalized(item.name, newValue);
                });

                editIngredientTypeBox.getSelectionModel().clearSelection();
                editIngredientTypeBox.hide();
                // show box while typing
                if (editIngredientTypeBox.getParent().isVisible()) editIngredientTypeBox.show();
            });
        });

        // set up "all recipes" + filtered list
        allRecipes = FXCollections.observableArrayList();

        // load from server
        try {
            List<Recipe> fromServer = server.getRecipes();
            allRecipes.setAll(fromServer);
        } catch (Exception e) {
            System.out.println("ERROR: Could not load recipes from server.");
            e.printStackTrace();
        }

        if (searchStatusLabel != null) {
            searchStatusLabel.setText("");
        }
        searchStatusLabel.setVisible(false);
        searchStatusLabel.setManaged(false);


        // this shows a subset of the original list (the first argument) based on a filter condition (the second argument)
        // the defuault is recipe -> true (because initially there is no filtering)
        filteredRecipes = new FilteredList<>(allRecipes, recipe -> true);

        sortedRecipes = new SortedList<>(filteredRecipes);

        // make it show the UI shows the ordered list
        recipeListView.setItems(sortedRecipes);

        setShowEN(user.isSelectedLanguage("en"));
        showEN.setSelected(user.isSelectedLanguage("en"));

        setShowNL(user.isSelectedLanguage("nl"));
        showNL.setSelected(user.isSelectedLanguage("nl"));

        setShowPT(user.isSelectedLanguage("pt"));
        showPT.setSelected(user.isSelectedLanguage("pt"));

        // subscribe to the websocket URL
        server.subscribeToRecipeList(list -> Platform.runLater(() -> {
            allRecipes.setAll(list);
            recipeListView.refresh();
            setLabelsAndFields();

            // Alerts user if any of their favourite recipes have been deleted
            int n = user.removeDeletedRecipes(allRecipes);
            user.saveUser();
            if (n > 0) showDeletedFavouritesAlert(n);
        }));


        sortedRecipes.setComparator(null); // default is no custom order

        // setup ingredient type sorting (alphabetical order)
        sortedIngredientTypes.setComparator((obj1, obj2) -> {
            String n1 = obj1.name == null ? "" : obj1.name.toLowerCase();
            String n2 = obj2.name == null ? "" : obj2.name.toLowerCase();
            return n1.compareTo(n2);
        });

        initializeScaleFactorUI();
        initializeIngredientRendering();
        recipeKcalPer100gLabel.setTooltip(nutritionTooltip);

        // combines the Recipe toString method with a star if it is in the user's favourite recipes
        recipeListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Recipe> call(ListView<Recipe> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Recipe recipe, boolean empty) {
                        super.updateItem(recipe, empty);
                        if (empty || recipe == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            String displayText = recipe.toString();
                            if (user.isFavouriteRecipe(recipe)) displayText += "   " + FULL_STAR;
                            setText(displayText);
                        }
                    }
                };
            }
        });

        onRefresh();

        recipeListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldRecipe, newRecipe) -> {
                    onRefresh();

                    if (newRecipe == null) return;

                    // subscribe to updates for the selected recipe
                    server.subscribeToRecipe(newRecipe.id, updatedRecipe -> Platform.runLater(() -> {

                        // update the selected recipe in the UI + model
                        // replace it inside allRecipes so the sidebar list stays correct
                        for (int i = 0; i < allRecipes.size(); i++) {
                            if (allRecipes.get(i).id == updatedRecipe.id) {
                                allRecipes.set(i, updatedRecipe);
                                break;
                            }
                        }

                        // if the user is still viewing that same recipe, refresh right pane
                        Recipe current = recipeListView.getSelectionModel().getSelectedItem();
                        if (current != null && current.id == updatedRecipe.id) {
                            setLabelsAndFields();
                        }

                        recipeListView.refresh();
                    }));
                }
        );
        moveStepUpButton.setVisible(false);
        moveStepDownButton.setVisible(false);

        // Show/hide and enable/disable the step reordering arrows
        // (based on which preparation step is selected)
        preparationStepListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, oldIndex, newIndex) -> {
                    int idx = newIndex == null ? -1 : newIndex.intValue();
                    int size = preparationStepListView.getItems().size();

                    boolean hasSelection = (idx >= 0 && size > 0);

                    // Only show arros if a step is selected
                    moveStepUpButton.setVisible(hasSelection);
                    moveStepDownButton.setVisible(hasSelection);

                    if (!hasSelection) {
                        return;
                    }

                    // Disable Up at the top and Down at the bottom
                    moveStepUpButton.setDisable(idx == 0);
                    moveStepDownButton.setDisable(idx == size - 1);
                });

        setupSearch();
        setupSort();

        Platform.runLater(() -> {
            // Makes it so that the 'Add Recipe' button is selected when the app gets started
            addRecipeButton.requestFocus();

            // Alerts user if any of their favourite recipes have been deleted
            int n = user.removeDeletedRecipes(allRecipes);
            user.saveUser();
            if (n > 0) showDeletedFavouritesAlert(n);
        });

        sceneBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> {
                    mainCtrl.showScene(sceneBox.getItems().indexOf(newValue));
                });

        editButtonsSetup();

        enterEditState(EditMode.NO_EDIT);
    }

    private void updateNutriScoreLabel(RecipeNutrition recipeNutrition) {
        if (recipeNutrition.nutriScore() == ' ') {
            nutriScoreLabel.setText("-");
            nutriScoreLabel.setStyle("");
            return;
        }
        nutriScoreLabel.setText(String.valueOf(recipeNutrition.nutriScore()));
        String colour = COLOURS.get(recipeNutrition.nutriScore());
        nutriScoreLabel.setTextFill(Color.web(colour));
        nutriScoreLabel.setFont(javafx.scene.text.Font.font("Arial", FontWeight.BOLD, 14));
    }

    private void initializeScaleFactorUI() {
        scaleFactorField.setText("1");
        scaleFactorField.setPromptText("1");

        scaleFactorField.setOnAction(e -> applyScaleFromField());

        scaleFactorField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                applyScaleFromField();
            }
        });
    }

    private void initializeIngredientRendering(){
        ingredientListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Ingredient> call(ListView<Ingredient> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Ingredient ingredient, boolean empty) {
                        super.updateItem(ingredient, empty);
                        if (empty || ingredient == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(ingredientScaling.format(ingredient, scaleFactor, languages.bundle()));
                        }
                    }
                };
            }
        });
    }

    private int getBaseServingsFromLabel() {
        String txt = servingsLabel.getText();

        if (txt == null || txt.isBlank()) return 1;

        int space = txt.indexOf(' ');

        String base = (space >= 0) ? txt.substring(0, space) : txt;

        return Integer.parseInt(base.trim());
    }

    private void applyScaleFromField(){
        String raw = scaleFactorField.getText();
        double parsed = scaleFactorParser.parseOrDefault(raw, scaleFactor);

        if (!(parsed> 0)) parsed = 1.0;

        scaleFactor = parsed;
        scaleFactorField.setText(scaleFactorParser.formatForField(scaleFactor));

        refreshScaledViewOnly();
    }

    private void updateServingsLabel(int baseServings) {
        String baseText = languages.formatInteger(baseServings);

        if (scaleFactor == 1.0){
            servingsLabel.setText(baseText);
            return;
        }

        double scaled = baseServings * scaleFactor;

        String scaledText = servingsFormatter.format(scaled);

        servingsLabel.setText(baseText + " (" + scaledText + ")");
    }

    private void refreshScaledViewOnly() {
        Recipe r = recipeListView.getSelectionModel().getSelectedItem();
        if (r != null) updateServingsLabel(r.servings);

        ingredientListView.refresh();

        updateNutritionTooltip();

    }

    private void updateNutritionTooltip(){

        Tooltip t = recipeKcalPer100gLabel.getTooltip();

        if (lastNutrition == null) {
            // no nutrition loaded yet (or recipe has none)
            String msg = languages.translate("recipe.tooltip.nutrition.notAvailable");
            if (t == null) recipeKcalPer100gLabel.setTooltip(new Tooltip(msg));
            else t.setText(msg);
            return;
        }
        long baseKcal = Math.round(lastNutrition.totalKcal());
        long baseGrams = Math.round(lastNutrition.totalGrams());

        long scaledKcal = Math.round(lastNutrition.totalKcal() * scaleFactor);
        long scaledGrams = Math.round(lastNutrition.totalGrams() * scaleFactor);

        long ignored = lastNutrition.ignoredIngredients();

        String baseKcalStr = languages.formatInteger(baseKcal);
        String baseGramsStr = languages.formatInteger(baseGrams);
        String scaledKcalStr = languages.formatInteger(scaledKcal);
        String scaledGramsStr = languages.formatInteger(scaledGrams);
        String ignoredStr = languages.formatInteger(ignored);

        long ingored = lastNutrition.ignoredIngredients();

        String scaleStr = scaleFactorParser.formatForField(scaleFactor);

        String text = languages.translate("recipe.tooltip.nutrition.text", baseKcalStr, baseGramsStr,
                scaleStr, scaledKcalStr, scaledGramsStr, ignoredStr);

//        String text =
//                "Base totals:\n " + Math.round(baseKcal) + " kcal, "
//                        + Math.round(baseGrams) + " grams\n\n" +
//                        "Scaled (x" + scaleFactorParser.formatForField(scaleFactor) + "):\n "
//                + Math.round(scaledKcal) + " kcal, " + Math.round(scaledGrams) + " grams\n\n" +
//                        "Ignored (informal) ingredients: " + ingored;

        if (t == null) {
            recipeKcalPer100gLabel.setTooltip(new Tooltip(text));
        }
        else {
            t.setText(text);
        }
    }

    private void updateFilteredList() {
        filteredRecipes.setPredicate(searchPredicate.and(favoritePredicate)
                .and(ENPredicate).and(NLPredicate).and(PTPredicate));

        if (recipeListView.getSelectionModel().getSelectedIndex() == -1) {
            recipeListView.getSelectionModel().select(0);
        }
    }

    private void setupSort() {
//        sortChoiceBox.getItems().addAll(
//                "Order by",
//                "Name (A-Z)",
//                "Fewest steps first",
//                "Fewest ingredients first"
//        );

                sortChoiceBox.getItems().addAll(
                SORT_ORDER_BY,
                SORT_NAME_AZ,
                SORT_KCAL,
                SORT_NUTRI,
                SORT_FEWEST_STEPS,
                SORT_FEWEST_ING
        );

        sortChoiceBox.getSelectionModel().select(0);

        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (SORT_ORDER_BY.equals(newValue)) {
                        sortedRecipes.setComparator(null);
                        return;
                    }
                    applySort(newValue);
                }
        );
    }

    private void applySort(String option) {
        if (option == null) {
            sortedRecipes.setComparator(null);
            return;
        }

        // comparator returns negative if first comes before second, positive otherwise and zero if equal
        switch (option) {
            case SORT_NAME_AZ:
                sortedRecipes.setComparator((recipe1, recipe2) -> {
                    String n1 = recipe1.name == null ? "" : recipe1.name.toLowerCase();
                    String n2 = recipe2.name == null ? "" : recipe2.name.toLowerCase();
                    return n1.compareTo(n2);
                });
                break;
            case SORT_KCAL:
                sortedRecipes.setComparator((recipe1, recipe2) -> {
                    var n1 = server.getRecipeNutrition(recipe1.id);
                    var n2 = server.getRecipeNutrition(recipe2.id);

                    Double kcal1 = n1.totalKcal() <= 0 ? 1000000 : n1.kcalPer100g();
                    Double kcal2 = n2.totalKcal() <= 0 ? 1000000 : n2.kcalPer100g();
                    if (kcal1.equals(kcal2)) {
                        String s1 = recipe1.name == null ? "" : recipe1.name.toLowerCase();
                        String s2 = recipe2.name == null ? "" : recipe2.name.toLowerCase();
                        return s1.compareTo(s2);
                    } else {
                        return Double.compare(kcal1, kcal2);
                    }
                });
                break;
            case SORT_NUTRI:
                sortedRecipes.setComparator((recipe1, recipe2) -> {
                    var n1 = server.getRecipeNutrition(recipe1.id);
                    var n2 = server.getRecipeNutrition(recipe2.id);

                    char nutri1 = n1.nutriScore() == ' ' ? 'F' : n1.nutriScore();
                    char nutri2 = n2.nutriScore() == ' ' ? 'F' : n2.nutriScore();
                    if (nutri1 == nutri2) {
                        String s1 = recipe1.name == null ? "" : recipe1.name.toLowerCase();
                        String s2 = recipe2.name == null ? "" : recipe2.name.toLowerCase();
                        return s1.compareTo(s2);
                    } else {
                        return String.valueOf(nutri1).compareTo(String.valueOf(nutri2));
                    }
                });
                break;
            case SORT_FEWEST_STEPS:
                sortedRecipes.setComparator((r1, r2) -> {
                    int s1 = r1.steps == null ? 0 : r1.steps.size();
                    int s2 = r2.steps == null ? 0 : r2.steps.size();
                    if (s1 == s2) {
                        String n1 = r1.name == null ? "" : r1.name.toLowerCase();
                        String n2 = r2.name == null ? "" : r2.name.toLowerCase();
                        return n1.compareTo(n2);
                    }
                    return Integer.compare(s1, s2);
                });
                break;
            case SORT_FEWEST_ING:
                sortedRecipes.setComparator((r1, r2) -> {
                    int i1 = (r1.ingredients == null) ? 0 : r1.ingredients.size();
                    int i2 = (r2.ingredients == null) ? 0 : r2.ingredients.size();
                    if (i1 == i2) {
                        String s1 = r1.name == null ? "" : r1.name.toLowerCase();
                        String s2 = r2.name == null ? "" : r2.name.toLowerCase();
                        return s1.compareTo(s2);
                    }
                    return Integer.compare(i1, i2);
                });
                break;
            default:
                sortedRecipes.setComparator(null);
        }
    }


    /**
     * Sets up the search functionality.
     */
    public void setupSearch() {
        // this listens to changes in the TextField's text
        recipeSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applySearchFilter(newValue);
        });

        // This listens to key presses when the TextField is focused
        recipeSearchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                recipeSearchField.clear(); // sets the text to ""
                applySearchFilter(""); // clears the filter
                recipeListView.getSelectionModel().clearSelection(); // deselects any recipe
                onRefresh(); // re-sync
            }
        });
    }

    /**
     * Applies a search filter on the recipes shown in the ListView (in the UI).
     * It's case-insensitive, and, as instructed, uses "AND" logic (with the matchesAllWords method).
     *
     * @param query what the user typed in the search field
     */
    private void applySearchFilter(String query) {
        if (query == null || query.isBlank()) {
            // np search, meaning show everything
            searchPredicate = defaultPredicate;
            updateFilteredList();

            // Hide label and give space back to the list
            searchStatusLabel.setText("");
            searchStatusLabel.setVisible(false);
            searchStatusLabel.setManaged(false);

            return;
        }

        // status text
        int total = allRecipes == null ? 0 : allRecipes.size();
        searchStatusLabel.setText("Showing " + total + " recipes");

        // spilt the query into words
//        String[] words = query.toLowerCase().trim().split("\\s+");
        String[] words = searchUtils.normalizeForSearch(query).split("\\s+");

        searchPredicate = recipe -> mattchesAllWords(recipe, words);
        updateFilteredList();

        ResourceBundle b = languages.bundle();

        matches = filteredRecipes.size();
        if (matches == 0) {
            searchStatusLabel.setText(b.getString("recipe.search.zero"));
        } else if (matches == 1) {
            searchStatusLabel.setText(b.getString("recipe.search.one"));
        } else {
            searchStatusLabel.setText(MessageFormat.format(
                    b.getString("recipe.search.multiple"), matches));
        }
        searchStatusLabel.setVisible(true);
        searchStatusLabel.setManaged(true);
    }

    /**
     * Checks whether a recipe contains all the words that are in the search field.
     *
     * @param recipe (any)
     * @param words  the user's query (split into each word)
     * @return true if the recipe contains all the words in the search field, false otherwise
     */
    private boolean mattchesAllWords(Recipe recipe, String[] words) {
        String searchableText = buildSearchText(recipe);

        // just go over a recipe and make sure all words from the search field are there
        for (String word : words) {
            if (!searchableText.contains(word)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Builds a lower-cased text representation of a recipe. Built for text search.
     *
     * @param recipe (any)
     * @return a single lower-cased string containing all searchable fields of the recipe (separated by spaces)
     */
    private String buildSearchText(Recipe recipe) {
        StringBuilder sb = new StringBuilder();

        sb.append(recipe.name.toLowerCase()).append(" ");

        if (recipe.ingredients != null) {
            for (Ingredient ingredient : recipe.ingredients) {
                if (ingredient != null && ingredient.ingredientType != null && ingredient.ingredientType.name != null) {
                    sb.append(ingredient.ingredientType.name.toLowerCase()).append(" ");
                }
            }
        }

        if (recipe.steps != null) {
            for (String step : recipe.steps) {
                if (step != null) {
                    sb.append(step.toLowerCase()).append(" ");
                }
            }
        }

        return searchUtils.normalizeForSearch(sb.toString());
    }


    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed (button will be
     * removed later)
     */
    @FXML
    public void onRefresh() {
        // load all ingredient types from server
        try {
            allIngredientTypes.setAll(server.getIngredientTypes());
        } catch (RuntimeException e) {
            System.out.println("ERROR: Could not load ingredient types from server.");
            e.printStackTrace();
        }

        boolean empty = recipeListView.getItems().isEmpty();
        mainSeparator.getParent().setVisible(!empty);
        addToShoppingListButton.setVisible(!empty);
        downloadRecipeButton.setVisible(!empty);
        printRecipeButton.setVisible(!empty);
        starRecipeButton.setVisible(!empty);

        if (recipeListView.getSelectionModel().getSelectedIndex() == -1) {
            recipeListView.getSelectionModel().select(0);
        }

        setLabelsAndFields();
    }

    // Left sidebar

    /**
     * On action method for the Remove Recipe button
     * It removes the currently selected recipe
     */
    @FXML
    private void onRemoveRecipe() throws JsonProcessingException {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        if (recipe == null) {
            return;
        }
        System.out.println(new ObjectMapper().writeValueAsString(recipe));
        server.deleteRecipe(recipe.id);
        allRecipes.remove(recipe);
        if (user.isFavouriteRecipe(recipe)) {
            user.removeFavouriteRecipe(recipe);
        }
        onRefresh();

        // apply the search filter again, because the counter might
        // need to be decreased by 1
        applySearchFilter(recipeSearchField.getText());
    }

    /**
     * On action method for the Add Recipe Button
     * Adds an empty recipe (default name, ingredient and step lists are null) and
     * then selects this recipe
     */
    @FXML
    private void onAddRecipe() throws JsonProcessingException {
        String language = languages.getLanguageTag();
        if (!user.isSelectedLanguage(language)) {
            if ("en".equals(language)) {
                recipeUtils.displayAlertWarning("recipe.en.not.selected", null);
            } else if ("nl".equals(language)) {
                recipeUtils.displayAlertWarning("recipe.nl.not.selected", null);
            } else {
                recipeUtils.displayAlertWarning("recipe.pt.not.selected", null);
            }
            return;
        }

        // reset the search query, otherwise 'new recipe' might not show, and it will break the app
        recipeSearchField.clear();
        applySearchFilter("");
        onRefresh();

        addRecipeToServer(new Recipe(
                languages.bundle().getString("recipe.title.new"), language));
        leaveEditState(EditMode.NO_EDIT);
        enterEditState(EditMode.EDIT_NAME);
    }

    @FXML
    private void onCloneRecipe() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        if (recipe == null) {
            System.out.println("No recipe selected.");
            return;
        }

        Recipe clonedRecipe = new Recipe(recipe.name + " [clone]");
        clonedRecipe.servings = recipe.servings;
        clonedRecipe.language = recipe.language;
        for (Ingredient ingredient : recipe.ingredients) {
            Ingredient clonedIngredient = new Ingredient(ingredient.ingredientType,
                    ingredient.amount, ingredient.unit, null);
            clonedRecipe.ingredients.add(clonedIngredient);
        }
        clonedRecipe.steps = new ArrayList<>(recipe.steps);

        addRecipeToServer(clonedRecipe);
    }

    private void addRecipeToServer(Recipe recipe) {
        recipe = server.addRecipe(recipe);

        // if user has filtered by favourite recipes, automatically make the new recipe a favourite
        if (showFavorites.isSelected()) {
            user.addFavouriteRecipe(recipe);
            user.saveUser();
        }

        onRefresh();

        allRecipes.add(recipe);
        recipeListView.getSelectionModel().select(recipe);

        newRecipe = true;
        newRecipeId = recipe.id;
        // Now immediately enter edit mode for this recipe
        onEditRecipeButton();

        // Also clear the placeholder so the user doesn't have to delete "New recipe"
        recipeTitleField.clear();
        editServingsField.clear();
        recipeTitleField.requestFocus(); // Tells JavaFX to put the cursor inside that text field
    }

    // Top right

    /**
     * Generates a PDF file from the recipe information.
     *
     * @param doc         the document to write to
     * @param title       the title of the recipe
     * @param ingredients the list of ingredients belonging to the recipe
     * @param steps       the ordered list of preparation steps
     * @param servings    the number of servings in the recipe
     * @throws Exception if the content cannot be added to the document
     */
    private void addRecipeContentToDocument(Document doc,
                                            String title, List<Ingredient> ingredients,
                                            List<String> steps,
                                            String servings) throws Exception {

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
        Font sectinFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        doc.open();
        doc.add(new Paragraph(languages.bundle().getString("recipeHeader") + ": " + title, titleFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(languages.bundle().getString("servingsHeader") + ": " + servings));
        doc.add(new Paragraph(languages.bundle().getString("ingredientsHeader") + ":", sectinFont));
        for (Ingredient i : ingredients) {
            doc.add(new Paragraph(" • " + ingredientScaling.format(i, scaleFactor, languages.bundle()), bodyFont));
        }
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(languages.bundle().getString("preparationHeader") + ":", sectinFont));
        for (int i = 0; i < steps.size(); i++) {
            Object step = steps.get(i);
            doc.add(new Paragraph(String.valueOf(i + 1) + ". " + step, bodyFont));
        }
    }

    /**
     * Generates a PDF file from the previously created doc (with the recipe content).
     *
     * @param file        the PDF to write to
     * @param title       the title of the recipe
     * @param ingredients the list of ingredients belonging to the recipe
     * @param steps       the ordered list of preparation steps
     * @param servings    the number of servings of the recipe
     * @throws Exception if the PDF cannot be created
     */
    private void writeRecipePDF(File file,
                                String title,
                                List<Ingredient> ingredients,
                                List<String> steps,
                                String servings) throws Exception {

        Document doc = new Document();

        // PdfWriter connects the doc to an OutputStream (a file in this case)
        PdfWriter.getInstance(doc, new FileOutputStream(file));

        doc.open();
        addRecipeContentToDocument(doc, title, ingredients, steps, servings);
        doc.close();
    }

    /**
     * Tries to open the PDF file in the default PDF viewer.
     *
     * @param file to be opened
     */
    private void openPdfInViewer(File file) {
        if (file == null) {
            return;
        }
        try {
            if (!Desktop.isDesktopSupported()) {
                System.out.println("ERROR: Desktop not supported.");
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                System.out.println("ERROR: Opening PDF not supported.");
                return;
            }

            desktop.open(file);
        } catch (IOException e) {
            System.out.println("ERROR: Could not open PDF in viewer.");
            e.printStackTrace();
        }
    }

    /**
     * Download button:
     * Asks the user where to save the PDF, then writes the recipe PDF there.
     */
    @FXML
    private void onDownloadRecipe() {
        String title = recipeTitleLabel.getText();

        if (title == null || title.isBlank()) {
            throw new IllegalStateException("ERROR: No recipe selected. Cannot save PDF.");
        }

        var ingredients = ingredientListView.getItems();
        var steps = preparationStepListView.getItems();
        var servings = servingsLabel.getText();

        // FileChooser is a JavaFX helper that opens a normal "Save as" dialog
        FileChooser chooser = new FileChooser();
        chooser.setTitle(languages.bundle().getString("recipe.fileChooser.savePdf.title"));

        // This limits visible file types to *.pdf
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PDF files (*.pdf)", "*.pdf"));

        // Suggest a default filename based on the recipe title
        String safeName = title.replaceAll("\\s+", "_").toLowerCase();
        chooser.setInitialFileName(safeName + ".pdf");

        // Show the dialog
        File file = chooser.showSaveDialog(rootPane.getScene().getWindow());

        // If the user presses "Cancel", file will be null
        if (file == null) {
            System.out.println("PDF not saved.");
            return;
        }
        ;

        try {
            writeRecipePDF(file, title, ingredients, steps, servings);
            System.out.println("Recipe saved as PDF" + file.getAbsolutePath());

            openPdfInViewer(file);
        } catch (Exception e) {
            System.out.println("ERROR: Could not save recipe PDF.");
            e.printStackTrace();
        }
    }

    /**
     * Print button:
     * Creates a temporary PDF file and sends it to the OS printer.
     */
    @FXML
    private void onPrintRecipe() {
        String title = recipeTitleLabel.getText();

        if (title == null || title.isBlank()) {
            throw new IllegalStateException("ERROR: No recipe selected. Cannot print PDF.");
        }

        var ingredients = ingredientListView.getItems();
        var steps = preparationStepListView.getItems();
        var servings = servingsLabel.getText();

        File tempFile;

        try {
            // Create a temporary PDF file
            tempFile = File.createTempFile("recipe", ".pdf");

            writeRecipePDF(tempFile, title, ingredients, steps, servings);
            System.out.println("Temporary recipe PDF for printing:" + tempFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("ERROR: Could not create PDF for printing.");
            e.printStackTrace();
            return;
        }

        // Load the PDF with the PDFBox library
        try (PDDocument document = PDDocument.load(tempFile)) {
            // Create a PrinterJob (the Java printing system)
            PrinterJob job = PrinterJob.getPrinterJob();

            // Wrap the PDF so the printer knows how many pages etc.
            job.setPageable(new PDFPageable(document));

            // Show the print dialog (select printer, pages, etc.)
            if (job.printDialog()) {
                // Print the document
                job.print();
                System.out.println("PDF sent to printer.");
            } else {
                System.out.println("PDF not printed.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Could not send PDF to printer.");
            e.printStackTrace();
        }
    }

    // Recipe title row

    /**
     * On action method for the Edit Recipe button
     * Changes the scene to Edit Mode
     */
    @FXML
    private void onEditRecipeButton() {
        changeViewEditMode(true);
        recipeTitleField.setText(recipeTitleLabel.getText());
        leaveEditState(EditMode.NO_EDIT);
        enterEditState(EditMode.EDIT_NAME);
    }

    /**
     * On action method for the Cancel Edit Button
     * The original recipe is not changed
     */
    @FXML
    private void onCancelEditButton() {
        if (newRecipe && newRecipeId != null) {
            try {
                server.deleteRecipe(newRecipeId);
                allRecipes.removeIf(r -> r.id == newRecipeId);
            } finally {
                newRecipe = false;
                newRecipeId = null;
            }
        }

        onRefresh();
        changeViewEditMode(false);
        newRecipe = false;
        leaveEditState(EditMode.EDIT_NAME);
        enterEditState(EditMode.NO_EDIT);
    }

    /**
     * On action method for the Done Edit button
     * All the changes are added to the selected recipe
     */
    @FXML
    private void onDoneEditButton() throws JsonProcessingException {
        // Check if the name is blank
        recipeTitleField.setText(recipeTitleField.getText().trim());
        if (recipeTitleField.getText().isBlank()) {
            System.out.println("The recipe needs a name");
            List<TextInputControl> textFields = new ArrayList<>();
            textFields.add(recipeTitleField);
            recipeUtils.displayAlertWarning("recipe.warning.empty.name", null);
            return;
        }
        // Check if the name starts with a digit
        if (Character.isDigit(recipeTitleField.getText().charAt(0))) {
            System.out.println("The recipe name cannot start with a digit");
            List<TextInputControl> textFields = new ArrayList<>();
            textFields.add(recipeTitleField);
            recipeUtils.displayAlertWarning("recipe.warning.start.with.number", textFields);
            return;
        }

        if(recipeTitleField.getText().length()>100) {
            System.out.println("The recipe name exceeds 100 characters!");
            List<TextInputControl> textFields = new ArrayList<>();
            textFields.add(recipeTitleField);
            recipeUtils.displayAlertWarning("recipe.warning.exceeds.limit", textFields);
            return;
        }

        int index = recipeListView.getSelectionModel().getSelectedIndex();
        Recipe recipe = recipeListView.getItems().get(index);

        recipe.name = recipeTitleField.getText();

        ImageView lang = recipeLanguageBox.getSelectionModel().getSelectedItem();
        if (lang.equals(ENFlag)) {
            recipe.language = "en";
        } else if (lang.equals(NLFlag)) {
            recipe.language = "nl";
        } else {
            recipe.language = "pt";
        }

        recipe.servings = parseBaseServings(servingsLabel.getText());
        recipe.ingredients = new ArrayList<>(ingredientListView.getItems().stream().toList());

        recipeUtils.normalizeIngredients(recipe.ingredients);
        recipeUtils.commitLocalIngredientTypes(recipe, server);
        recipe.steps = new ArrayList<>(preparationStepListView.getItems().stream().toList());

        System.out.println(new ObjectMapper().writeValueAsString(recipe));

        allRecipes.set(index, server.updateRecipe(recipe.id, recipe));

        onRefresh();
        recipeListView.getSelectionModel().select(recipe);
        changeViewEditMode(false);
        newRecipe = false;

        // apply the search filter again, because the recipe might not
        // match anymore after a name change
        applySearchFilter(recipeSearchField.getText());
        leaveEditState(EditMode.EDIT_NAME);
        enterEditState(EditMode.NO_EDIT);
    }

    private int parseBaseServings(String servings) {
        if (servings == null || servings.isBlank()) return 1;

        int end = 0;
        while (end < servings.length() && Character.isDigit(servings.charAt(end))) {
            end++;
        }

        if (end == 0) return 1;
        return Integer.parseInt(servings.substring(0, end));
    }

    // Servings section

    @FXML
    private void onEditDetailsButton() {
        editServingsField.setText(Integer.toString(getBaseServingsFromLabel()));
        changeDetailsViewEditMode(true);
        leaveEditState(EditMode.EDIT_NAME);
        enterEditState(EditMode.EDIT_DETAILS);
    }

    @FXML
    private void onCancelEditDetailsButton() {
        changeDetailsViewEditMode(false);
    }

    @FXML
    private void onDoneEditDetailsButton() {
        List<TextInputControl> textFields = new ArrayList<>();
        editServingsField.setText(editServingsField.getText().trim());
        textFields.add(editServingsField);
        if (editServingsField.getText().isBlank()) {
            System.out.println("Enter a valid amount.");
            recipeUtils.displayAlertWarning("recipe.warning.servings.empty.amount", textFields);
            return;
        }
        // Check if fields exceeds 8 characters
        if(editServingsField.getText().length()>8) {
            System.out.println("The servings amount exceeds 8 characters!");
            recipeUtils.displayAlertWarning("recipe.warning.serving.exceeds.limit", textFields);
            return;
        }
        try {
            if (Integer.parseInt(editServingsField.getText()) <= 0) {
                System.out.println("The amount of servings needs to " +
                        "be a positive integer.");
                recipeUtils.displayAlertWarning("recipe.warning.servings.negative.amount", textFields);
                return;
            }
        } catch (NumberFormatException e) {
            recipeUtils.displayAlertWarning("recipe.warning.servings.non.integer", textFields);
        }

        try {
            Integer.parseInt(editServingsField.getText());
            servingsLabel.setText(editServingsField.getText());
            Integer.parseInt(scaleFactorField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
            recipeUtils.displayAlertWarning("recipe.warning.servings.non.integer", textFields);
            return;
        }

        changeDetailsViewEditMode(false);
    }

    // Ingredient edit section

    /**
     * On action method for the Remove Ingredient Button
     * Removes the currently selected ingredient (if any), note that a change like
     * this only affects the recipe if the user presses 'Done' later
     */
    @FXML
    private void onRemoveIngredientButton() throws JsonProcessingException {
        if (ingredientListView.getItems().isEmpty()) {
            System.out.println("There is no ingredient to remove.");
            return;
        }
        if (ingredientListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no ingredient selected.");
            return;
        }
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        ingredientListView.getItems().remove(ingredient);
    }

    /**
     * On action method for the Add Ingredient button
     * Adds a new ingredient to the ListView with a default name. Amount and Unit are
     * set to null. It then selects this ingredient
     */
    @FXML
    private void onAddIngredientButton() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        Ingredient ingredient = new Ingredient(null,
                null, null, recipe);
        ingredientListView.getItems().add(ingredient);
        ingredientListView.getSelectionModel().select(
                ingredientListView.getItems().size() - 1
        );

        newIngredient = true;
        // Immediately open ingredient edit mode
        onEditIngredientButton();

        // Clear the placeholder so the user doesn't have to delete "New ingredient"
        editIngredientAmountField.clear();
        editUnitBox.getSelectionModel().select(0);
        leaveEditState(EditMode.EDIT_NAME);
        enterEditState(EditMode.EDIT_INGREDIENT_1);
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

        changeIngredientTypeViewEditMode(1);
        editUnitBox.getSelectionModel().select(0);

        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        if (ingredient.ingredientType != null) {
            editIngredientTypeBox.setValue(ingredient.ingredientType);
        } else {
            editIngredientTypeBox.getSelectionModel().clearSelection();
            editIngredientTypeBox.setValue(null);
        }
        leaveEditState(EditMode.EDIT_NAME);
        enterEditState(EditMode.EDIT_INGREDIENT_1);
    }

    /**
     * On action method for the Cancel Edit Ingredient button
     */
    @FXML
    private void onCancelEditIngredientButton() {
        if (newIngredient) {
            ingredientListView.getItems().removeLast();
        }
        changeIngredientTypeViewEditMode(0);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
        newIngredient = false;

        // activate button behaviour
        leaveEditState(EditMode.EDIT_INGREDIENT_1);
        enterEditState(EditMode.EDIT_NAME);
    }

    /**
     * On action method for the Next Edit Ingredient button
     * Switches the scene to edit ingredient part 2 (which is where the user inputs
     * the unit and amount of the ingredient)
     */
    @FXML
    private void onNextEditIngredientButton() {
        IngredientType ingredientType = editIngredientTypeBox.getValue();
        ingredientType.name = ingredientType.name.trim();

        // Check if the name field is empty first to avoid an IndexOutOfBoundsException
        if (ingredientType.name.isEmpty()) {
            recipeUtils.displayAlertWarning("recipe.warning.ing.empty", null);
            System.out.println("The ingredient type needs a name.");
            return;
        }

        if(ingredientType.name.length()>50) {
            System.out.println("The ingredient type name exceeds 50 characters!");
            List<TextInputControl> textFields = new ArrayList<>();
            textFields.add(new TextField(ingredientType.name));
            recipeUtils.displayAlertWarning("recipe.warning.ingredient.exceeds.limit", textFields);
            return;
        }

        // check if ingredient type starts with a digit
        if (Character.isDigit(ingredientType.name.charAt(0))) {
            List<TextInputControl> textFields = new ArrayList<>();
            textFields.add(new TextField(ingredientType.name));
            recipeUtils.displayAlertWarning("recipe.warning.ing.number", textFields);
            return;
        }

        changeIngredientTypeViewEditMode(2);

        Ingredient ingredient = ingredientListView.getSelectionModel()
                .getSelectedItem();
        if (ingredient.amount != null) {
            editIngredientAmountField.setText(String.valueOf(ingredient.amount));
        }
        if (ingredient.unit != null) {
            editUnitBox.setValue(ingredient.unit.name());
        }
        if (editUnitBox.getValue().isEmpty()) {
            editUnitBox.getSelectionModel().select(0);
        }

        // activate button behaviour
        leaveEditState(EditMode.EDIT_INGREDIENT_1);
        enterEditState(EditMode.EDIT_INGREDIENT_2);
    }

    /**
     * On action method for the Back Edit Ingredient button
     * Switches the scene back to edit ingredient part 1 (which is where the user
     * enters the name of the ingredient type
     */
    @FXML
    private void onBackEditIngredientButton() {
        changeIngredientTypeViewEditMode(1);
        leaveEditState(EditMode.EDIT_INGREDIENT_2);
        enterEditState(EditMode.EDIT_INGREDIENT_1);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() throws JsonProcessingException {
        // Prepare the list of fields to clear if validation fails
        String selectedUnit = editUnitBox.getValue();
        if (selectedUnit == null || UNIT_PLACEHOLDER.equals(selectedUnit)) {
            recipeUtils.displayAlertWarning("recipe.warning.ing.unit", null);
            System.out.println("Select a unit.");
            return;
        }

        String amountText = editIngredientAmountField.getText().trim();

        List<TextInputControl> textFields = new ArrayList<>();
        textFields.add(editIngredientAmountField);

        if (selectedUnit.equals("TO_TASTE") && !amountText.isEmpty()) {
            recipeUtils.displayAlertWarning("recipe.warning.ing.TO_TASTE", textFields);
            System.out.println("This unit cannot have an amount.");
            return;
        }

        if (!selectedUnit.equals("TO_TASTE") && amountText.isEmpty()) {
            recipeUtils.displayAlertWarning("recipe.warning.ing.required", textFields);
            System.out.println("This unit needs an amount.");
            return;
        }

        if (amountText.length() > 8){
            System.out.println("Amount exceeds the limit!");
            recipeUtils.displayAlertWarning("recipe.warning.ing.exceed.limit", textFields);
            return;
        }

        Double parsedAmount = amountParser.parseAmount(amountText);
        if (!"TO_TASTE".equals(selectedUnit) && parsedAmount == null) {
            recipeUtils.displayAlertWarning("recipe.warning.ing.nonNumeric", textFields);
            System.out.println("Enter a valid number.");
            return;
        }
        if (!"TO_TASTE".equals(selectedUnit) && parsedAmount <= 0) {
            recipeUtils.displayAlertWarning("recipe.warning.ing.negative", textFields);
            return;
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = ingredientListView.getItems().get(index);

        // Set Ingredient Type
        ingredient.ingredientType = editIngredientTypeBox.getValue();

        // Apply validated data
        ingredient.amount = parsedAmount;
        ingredient.unit = Unit.valueOf(selectedUnit);

        // Update UI and Server
        ingredientListView.getItems().set(index, ingredient);
        System.out.println(new ObjectMapper().writeValueAsString(ingredient));

        editIngredientAmountField.clear();
        editUnitBox.getSelectionModel().select(0);

        // add new ingredient type to list if it is new
        if (! allIngredientTypes.contains(ingredient.ingredientType)) {
            allIngredientTypes.add(ingredient.ingredientType);
        }

        changeIngredientTypeViewEditMode(0);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
        newIngredient = false;

        // activate button behaviour
        leaveEditState(EditMode.EDIT_INGREDIENT_2);
        enterEditState(EditMode.EDIT_NAME);
    }

    // Edit preparation step section

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

        newStep = true;
        // Immediately open step edit mode
        onEditStepButton();

        // Clear the placeholder so the user doesn't have to delete "New step"
        editStepField.clear();
        editStepField.requestFocus();
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

        // Force focus into the step field
        editStepField.requestFocus();

        leaveEditState(EditMode.EDIT_NAME);
        enterEditState(EditMode.EDIT_STEPS);
    }

    /**
     * On action method for the Cancel Edit Step button
     */
    @FXML
    private void onCancelEditStepButton() {
        if (newStep) {
            preparationStepListView.getItems().removeLast();
        }
        changeStepViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
        newStep = false;

        leaveEditState(EditMode.EDIT_STEPS);
        enterEditState(EditMode.EDIT_NAME);
    }

    /**
     * On action method for the Done Edit Step button
     */
    @FXML
    private void onDoneEditStepButton() {
        editStepField.setText(editStepField.getText().trim());
        if (editStepField.getText().isEmpty()) {
            recipeUtils.displayAlertWarning("recipe.warning.step.empty", null);
            System.out.println("The step cannot be empty.");

            return;
        }
        // Check if the step exceeds 250 characters
        if(editStepField.getText().length()>250){
            List<TextInputControl> textFields = new ArrayList<>();
            textFields.add(editStepField);
            recipeUtils.displayAlertWarning("recipe.warning.step.exceed.limit", textFields);
            System.out.println("The step exceeds 250 characters!");
        }
        int index = preparationStepListView.getSelectionModel().getSelectedIndex();
        preparationStepListView.getItems().set(index, editStepField.getText());

        preparationStepListView.getSelectionModel().select(index);

        changeStepViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
        newStep = false;
        leaveEditState(EditMode.EDIT_STEPS);
        enterEditState(EditMode.EDIT_NAME);
    }

    /**
     * On action method for the Add to Shopping List button
     * A new scene with AddToShoppingList overview is opened
     */
    @FXML
    private void onAddToShoppingList() {
        mainCtrl.showAddToShoppingList(recipeListView.getSelectionModel().getSelectedItem(), scaleFactor);
    }

    /**
     * On action method for the star recipe button
     * Adds to/removes a recipe from the user's favourite recipes when the star recipe button is clicked
     */
    @FXML
    private void onStarRecipe() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        if (user.isFavouriteRecipe(recipe)) {
            user.removeFavouriteRecipe(recipe);
        } else {
            user.addFavouriteRecipe(recipe);
        }
        user.saveUser();
        recipeListView.refresh();
        onRefresh();
    }

    /**
     * Show alert if some of user's favourite recipes have been deleted
     *
     * @param n Number of favourite recipes deleted
     */
    private void showDeletedFavouritesAlert(int n) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.setTitle("Some of your favourite recipes have been deleted");
        ResourceBundle b = languages.bundle();
        alert.setTitle(b.getString("recipe.alert.favDeleted.title"));
        alert.setHeaderText(null);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/FoodPalLogo.png"))));

//        alert.setContentText(n + " of your favourite recipes have been deleted by others :(");
        alert.setContentText(MessageFormat.format(b.getString("recipe.alert.favDeleted.content"), n));
        alert.show();
    }

    // ----------------------------------------------------------------------------------
    // -- enums and classes to change the edit buttons when the edit state changes
    // possible ingredient edit states
    private enum EditMode {
        NO_EDIT,
        EDIT_NAME,
        EDIT_DETAILS,
        EDIT_INGREDIENT_1,
        EDIT_INGREDIENT_2,
        EDIT_STEPS,
    }

    private void enterEditState(EditMode state) {
        changeEditState(state,true);
    }

    private void leaveEditState(EditMode state) {
        changeEditState(state, false);
    }

    private void changeEditState(EditMode state, boolean active) {
        if (state == EditMode.NO_EDIT)
            return;
        editButtonMap.get(state).defaultButton.setDefaultButton(active);
        editButtonMap.get(state).cancelButton.setCancelButton(active);
    }

    class EditButtonSet {
        Button defaultButton;
        Button cancelButton;

        EditButtonSet(Button def, Button can) {
            defaultButton = def;
            cancelButton = can;
        }
    }

    Map<EditMode, EditButtonSet> editButtonMap = new HashMap<>();

    private void editButtonsSetup() {
        editButtonMap.put(EditMode.EDIT_NAME, new EditButtonSet(doneEditButton, cancelEditButton));
        editButtonMap.put(EditMode.EDIT_DETAILS, new EditButtonSet(doneEditDetailsButton, cancelEditDetailsButton));
        editButtonMap.put(EditMode.EDIT_INGREDIENT_1, new EditButtonSet(nextEditIngredientButton, cancelEditIngredientButton));
        editButtonMap.put(EditMode.EDIT_INGREDIENT_2, new EditButtonSet(doneEditIngredientButton, backEditIngredientButton));
        editButtonMap.put(EditMode.EDIT_STEPS, new EditButtonSet(doneEditStepButton, cancelEditStepButton));
    }
}
