package client.scenes;

import client.utils.RecipeUtils;
import client.utils.UserConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import java.awt.print.PrinterJob;
import java.util.function.Predicate;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;
public class RecipeOverviewCtrl {
    // Constants
    private static final String EMPTY_STAR = "☆";
    private static final String FULL_STAR = "★";

    private final ServerUtils server;

    private final RecipeUtils recipeUtils;

    private UserConfig user;

    private final MainCtrl mainCtrl;

    private Predicate<Recipe> currentPredicate = recipe -> true;
    private ObservableList<Recipe> allRecipes;
    private FilteredList<Recipe> filteredRecipes;
    private SortedList<Recipe> sortedRecipes;

    boolean newRecipe = false;
    boolean newIngredient = false;
    boolean newStep = false;

    // Root

    @FXML
    private AnchorPane rootPane;

    // Left Sidebar

    @FXML
    private TextField recipeSearchField;

    @FXML
    private ChoiceBox<String> favouriteRecipeFilterBox;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Button removeRecipeButton;

    @FXML
    private Button addRecipeButton;

    @FXML
    private Button cloneRecipeButton;

    @FXML
    private Button shoppingListButton;

    // Top right

    @FXML
    private Button starRecipeButton;

    @FXML
    private Tooltip starTooltip;

    @FXML
    private Button downloadRecipeButton;

    @FXML
    private Button printRecipeButton;

    @FXML
    private Button toggleOverviewButton;

    @FXML
    private Button homeButton;

    // Recipe title row

    @FXML
    private Label recipeTitleLabel;

    @FXML
    private TextField recipeTitleField;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button doneEditButton;

    @FXML
    private Separator mainSeparator;

    // Servings

    @FXML
    private Label servingsLabel;

    @FXML
    private TextField editServingsField;

    @FXML
    private Button editServingsButton;

    @FXML
    private Button cancelEditServingsButton;

    @FXML
    private Button doneEditServingsButton;

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
    private ChoiceBox<IngredientType> editIngredientBox;

    @FXML
    private TextField editIngredientNameField;

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
    private Button onBackEditIngredientButton;

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
    private Label recipeKcalPer100gLabel;

    // General

    @Inject
    public RecipeOverviewCtrl(ServerUtils server,
                              RecipeUtils recipeUtils,
                              UserConfig user,
                              MainCtrl mainCtrl) {
        this.server = server;
        this.recipeUtils = recipeUtils;
        this.user = user;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Changes the scene between viewing and editing a recipe
     *
     * @param value false for viewing mode, true for editing mode
     */
    private void changeViewEditMode(boolean value) {
        recipeTitleLabel.setVisible(!value);
        recipeTitleField.setVisible(value);

        editRecipeButton.setVisible(!value);

        editServingsButton.getParent().setVisible(value);
        removeIngredientButton.getParent().setVisible(value);
        removeStepButton.getParent().setVisible(value);

        cancelEditButton.setVisible(value);
        doneEditButton.setVisible(value);

        addRecipeButton.setVisible(!value);
        removeRecipeButton.setVisible(!value);
        cloneRecipeButton.setVisible(!value);
        shoppingListButton.setVisible(!value);

        recipeSearchField.setDisable(value);
        sortChoiceBox.setDisable(value);
        recipeListView.setDisable(value);

        starRecipeButton.setVisible(!value);
        downloadRecipeButton.setVisible(!value);
        printRecipeButton.setVisible(!value);
        toggleOverviewButton.setVisible(!value);
        homeButton.setVisible(!value);

        favouriteRecipeFilterBox.setDisable(value);

        // When going into edit mode, it automatically selects the
        // recipe name field
        Platform.runLater(() -> {
            recipeTitleField.requestFocus();
            recipeTitleField.selectAll();
        });
    }

    private void changeServingsViewEditMode(boolean value) {
        editServingsButton.getParent().setVisible(!value);
        editServingsField.getParent().setVisible(value);
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
        preparationStepListView.setDisable(value);
        removeIngredientButton.getParent().setVisible(!value);

        // When going into edit mode, it automatically selects the
        // step name field
        Platform.runLater(() -> {
            editStepField.requestFocus();
            editStepField.selectAll();
        });
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
        editIngredientBox.getParent().setVisible(value == 1);
        editIngredientAmountField.getParent().setVisible(value == 2);

        removeIngredientButton.getParent().setMouseTransparent(value > 0);
        ingredientListView.setDisable(value > 0);
        removeStepButton.getParent().setVisible(value == 0);

        // When going into edit mode, it automatically selects the
        // ingredient name field
        Platform.runLater(() -> {
            editIngredientNameField.requestFocus();
            editIngredientNameField.selectAll();
        });
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
            servingsLabel.setText(String.valueOf(recipe.servings));

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

                    if (current == null || current.id != requestedId) return;

                    if (n.totalGrams() <= 0) {
                        recipeKcalPer100gLabel.setText("-");
                    } else {
                        recipeKcalPer100gLabel.setText(String.valueOf(Math.round(n.kcalPer100g())));
                    }
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
    /**
     * Initializes the home screen with default values
     */
    @FXML
    private void initialize() {
        changeServingsViewEditMode(false);
        changeIngredientTypeViewEditMode(0);
        changeStepViewEditMode(false);
        changeViewEditMode(false);
        recipeTitleField.setVisible(false);
        onRefresh();

        editUnitBox.getItems().addAll("Select a unit", "G", "ML", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

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

        // subscribe to the websocket URL
        server.subscribeToRecipeList(list -> Platform.runLater(() -> {
            allRecipes.setAll(list);
            recipeListView.refresh();
            setLabelsAndFields();
        }));

        sortedRecipes.setComparator(null); // default is no custom order

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
        setupFavouriteRecipeFilter();

        Platform.runLater(() -> {
            // Makes it so that the 'Add Recipe' button is selected when the app gets started
            addRecipeButton.requestFocus();

            // Alerts user if any of their favourite recipes have been deleted
            int n = user.removeDeletedRecipes(allRecipes);
            user.saveUser();
            if (n > 0) showDeletedFavouritesAlert(n);
        });
    }

    private void setupSort() {
        sortChoiceBox.getItems().addAll(
                "Order by",
                "Name (A-Z)",
                "Fewest steps first",
                "Fewest ingredients first"
        );

        sortChoiceBox.getSelectionModel().select(0);

        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if ("Order by".equals(newValue)) {
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
            case "Name (A-Z)":
                sortedRecipes.setComparator((recipe1, recipe2) -> {
                    String n1 = recipe1.name == null ? "" : recipe1.name.toLowerCase();
                    String n2 = recipe2.name == null ? "" : recipe2.name.toLowerCase();
                    return n1.compareTo(n2);
                });
                break;
            case "Fewest steps first":
                sortedRecipes.setComparator((r1, r2) -> {
                    int s1 = r1.steps == null ? 0 : r1.steps.size();
                    int s2 = r2.steps == null ? 0 : r2.steps.size();
                    return Integer.compare(s1, s2);
                });
                break;
            case "Fewest ingredients first":
                sortedRecipes.setComparator((r1, r2) -> {
                    int i1 = (r1.ingredients == null) ? 0 : r1.ingredients.size();
                    int i2 = (r2.ingredients == null) ? 0 : r2.ingredients.size();
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
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
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
            filteredRecipes.setPredicate(currentPredicate);

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
        String[] words = query.toLowerCase().trim().split("\\s+");

        filteredRecipes.setPredicate(currentPredicate.and(recipe -> mattchesAllWords(recipe, words)));

        String msg;
        int matches = filteredRecipes.size();
        if (matches == 0) {
            msg = "No recipes match your search";
        } else if (matches == 1) {
            msg = "1 recipe found";
        } else {
            msg = matches + " recipes found";
        }

        searchStatusLabel.setText(msg);
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

        return sb.toString();
    }


    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed (button will be
     * removed later)
     */
    @FXML
    public void onRefresh() {
        editIngredientBox.getItems().setAll(
                new IngredientType("Create new ingredient type", null,
                        null, null)
        );
        editIngredientBox.getItems().addAll(server.getIngredientTypes());

        boolean empty = recipeListView.getItems().isEmpty();
        mainSeparator.getParent().setVisible(!empty);
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
        // reset the search query, otherwise 'new recipe' might not show, and it will break the app
        recipeSearchField.clear();
        applySearchFilter("");
        onRefresh();

        addRecipeToServer(new Recipe("New recipe"));
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
        for (Ingredient ingredient :  recipe.ingredients) {
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
        if (favouriteRecipeFilterBox.getSelectionModel().getSelectedItem().equals("Favourites")) {
            user.addFavouriteRecipe(recipe);
            user.saveUser();
        }

        onRefresh();

        allRecipes.add(recipe);
        // recipeListView.getSelectionModel().select(recipe);
        recipeListView.getSelectionModel().select(
                recipeListView.getItems().size() - 1
        );

        newRecipe = true;
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
        doc.add(new Paragraph("Recipe: " + title, titleFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(servings));
        doc.add(new Paragraph("Ingredients:", sectinFont));
        for (Ingredient i : ingredients) {
            doc.add(new Paragraph(" • " + i, bodyFont));
        }
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Preparation:", sectinFont));
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
        chooser.setTitle("Save recipe as PDF");

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

    @FXML
    private void onToggleOverviewButton() {
        mainCtrl.showIngredientTypeOverview();
    }

    @FXML
    private void onHomeButton() {
        mainCtrl.showHomeScreen();
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
    }

    /**
     * On action method for the Cancel Edit Button
     * The original recipe is not changed
     */
    @FXML
    private void onCancelEditButton() {
        if (newRecipe) {
            Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
            server.deleteRecipe(recipe.id);
        }
        onRefresh();
        changeViewEditMode(false);
        newRecipe = false;
    }

    /**
     * On action method for the Done Edit button
     * All the changes are added to the selected recipe
     */
    @FXML
    private void onDoneEditButton() throws JsonProcessingException {
        if (recipeTitleField.getText().isBlank()) {
            System.out.println("The recipe needs a name");
            return;
        }

        int index = recipeListView.getSelectionModel().getSelectedIndex();
        Recipe recipe = recipeListView.getItems().get(index);

        recipe.name = recipeTitleField.getText();
        recipe.servings = Integer.parseInt(servingsLabel.getText());
        recipe.ingredients = ingredientListView.getItems().stream().toList();
        recipeUtils.normalizeIngredients(recipe.ingredients);
        recipeUtils.commitLocalIngredientTypes(recipe, server);
        recipe.steps = preparationStepListView.getItems().stream().toList();

        System.out.println(new ObjectMapper().writeValueAsString(recipe));

        allRecipes.set(index, server.updateRecipe(recipe.id, recipe));

        onRefresh();
        changeViewEditMode(false);
        newRecipe = false;

        // apply the search filter again, because the recipe might not
        // match anymore after a name change
        applySearchFilter(recipeSearchField.getText());
    }

    // Servings section

    @FXML
    private void onEditServingsButton() {
        editServingsField.setText(servingsLabel.getText());
        changeServingsViewEditMode(true);
    }

    @FXML
    private void onCancelEditServingsButton() {
        changeServingsViewEditMode(false);
    }

    @FXML
    private void onDoneEditServingsButton() {
        if (editServingsField.getText().isBlank()) {
            System.out.println("Enter a valid amount.");
            return;
        }
        if (Integer.parseInt(editServingsField.getText()) <= 0) {
            System.out.println("The amount of servings needs to " +
                    "be a positive integer.");
            return;
        }

        try {
            Integer.parseInt(editServingsField.getText());
            servingsLabel.setText(editServingsField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
            return;
        }

        changeServingsViewEditMode(false);
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
        recipe.ingredients.remove(ingredient);
        server.updateRecipe(recipe.id, recipe);
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
        editIngredientNameField.clear();
        editIngredientAmountField.clear();
        editUnitBox.setValue("");

        editIngredientNameField.requestFocus(); // Put the cursor in the name field
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
        editIngredientNameField.setText("");
        editUnitBox.getSelectionModel().select(0);

        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        if (ingredient.ingredientType != null) {
            editIngredientBox.setValue(ingredient.ingredientType);
            editIngredientNameField.setText(ingredient.ingredientType.name);
        } else {
            editIngredientBox.getSelectionModel().select(0);
        }

        editIngredientBox.getSelectionModel().selectedItemProperty()
                .addListener((observable,
                              oldValue, newValue) -> {
                    if (newValue == null) return;
                    if (newValue.name.equals("Create new ingredient type")) {
                        editIngredientNameField.setDisable(false);
                        editIngredientNameField.setText("");
                    } else {
                        editIngredientNameField.setDisable(true);
                        editIngredientNameField.setText(editIngredientBox
                                .getValue().name);
                    }
                });

        cancelEditButton.setVisible(false);
        doneEditButton.setVisible(false);

        // Force focus into the IngredientType name box
        Platform.runLater(() -> editIngredientBox.requestFocus());
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
    }

    /**
     * On action method for the Next Edit Ingredient button
     * Switches the scene to edit ingredient part 2 (which is where the user inputs
     * the unit and amount of the ingredient)
     */
    @FXML
    private void onNextEditIngredientButton() {
        if (editIngredientNameField.getText().isEmpty()) {
            System.out.println("The ingredient type needs a name.");
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
    }

    /**
     * On action method for the Back Edit Ingredient button
     * Switches the scene back to edit ingredient part 1 (which is where the user
     * enters the name of the ingredient type
     */
    @FXML
    private void onBackEditIngredientButton() {
        changeIngredientTypeViewEditMode(1);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() throws JsonProcessingException {
        if (editUnitBox.getValue().equals("Select a unit")) {
            System.out.println("Select a unit.");
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

        if (editIngredientBox.getValue().name.equals("Create new ingredient type")) {
            // Create a LOCAL type only (id stays 0 / null)
            ingredient.ingredientType = new IngredientType(
                    editIngredientNameField.getText(),
                    null, new ArrayList<>(), null
            );
        } else {
            ingredient.ingredientType = editIngredientBox.getValue();
        }

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

        System.out.println(new ObjectMapper().writeValueAsString(ingredient));

        editIngredientAmountField.setText("");
        editUnitBox.getSelectionModel().select(0);

        changeIngredientTypeViewEditMode(0);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
        newIngredient = false;
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

        cancelEditButton.setVisible(false);
        doneEditButton.setVisible(false);

        // Force focus into the step field
        editStepField.requestFocus();
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
        newStep = false;
    }

    /**
     * On action method for the Bag button
     * A new window with shopping list is opened
     */
    @FXML
    private void onShoppingListButton() {
        mainCtrl.showShoppingList(true);
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
     * Sets up the favourite recipe filter choice box
     */
    private void setupFavouriteRecipeFilter() {
        favouriteRecipeFilterBox.getItems().addAll("All recipes", "Favourites");
        favouriteRecipeFilterBox.getSelectionModel().select(0);
        favouriteRecipeFilterBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    recipeSearchField.clear();
                    applySearchFilter("");
                    if (newValue.equals("All recipes")) {
                        currentPredicate = recipe -> true;
                        filteredRecipes.setPredicate(currentPredicate);
                    } else if (newValue.equals("Favourites")) {
                        currentPredicate = recipe -> user.isFavouriteRecipe(recipe);
                        filteredRecipes.setPredicate(currentPredicate);
                    }
                    recipeListView.getSelectionModel().clearSelection();
                    onRefresh();
                }
        );
    }

    /**
     * Show alert if some of user's favourite recipes have been deleted
     * @param n Number of favourite recipes deleted
     */
    private void showDeletedFavouritesAlert(int n) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Some of your favourite recipes have been deleted");
        alert.setHeaderText(null);
        alert.setContentText(n + " of your favourite recipes have been deleted by others :(");
        alert.show();
    }
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