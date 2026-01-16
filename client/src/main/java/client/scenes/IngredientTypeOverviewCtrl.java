package client.scenes;

import client.utils.LanguageService;
import client.utils.RecipeUtils;
import client.utils.ServerUtility;
import client.utils.UserConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.IngredientType;
import commons.Nutrition;
import commons.Recipe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.*;

public class IngredientTypeOverviewCtrl {

    private final ServerUtility server;

    private final RecipeUtils recipeUtils;

    private UserConfig user;

    private final MainCtrl mainCtrl;

    boolean newIngredientType = false;

    private final LanguageService languages;

    private int usedInRecipesCount = 0;

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
    public ChoiceBox<String> sceneBox;

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

    @FXML private Label detailsHeaderLabel;
    @FXML private Label nameCaptionLabel;
    @FXML private Label densityCaptionLabel;
    @FXML private Label nutritionHeaderLabel;
    @FXML private Label proteinCaptionLabel;
    @FXML private Label fatCaptionLabel;
    @FXML private Label carbsCaptionLabel;
    @FXML private Label kcalCaptionLabel;

    // Bottom

    @FXML
    private Label usedInRecipesLabel;

    private List<TextInputControl> activeFieldsToClear = new ArrayList<>();

    private void setTooltip(Control c, String key){
        ResourceBundle b = languages.bundle();
        Tooltip t = c.getTooltip();
        if (t == null) {
            t = new Tooltip();
            c.setTooltip(t);
        }
        t.setText(b.getString(key));
    }

    private String formatUsedInRecipes(int count){
        ResourceBundle b = languages.bundle();
        String key = (count == 1) ? "ing.usedInRecipes.one" : "ing.usedInRecipes.many";
        return MessageFormat.format(b.getString(key), count);
    }

    public void applyTranslations() {
        ResourceBundle b = languages.bundle();

        ingredientTypeSearchField.setPromptText(b.getString("ing.search.prompt"));

        if ("New ingredient".equals(ingredientTypeTitleLabel.getText()) || ingredientTypeTitleLabel.getText().isBlank()) {
            ingredientTypeTitleLabel.setText(b.getString("ing.title.new"));
        }

        detailsHeaderLabel.setText(b.getString("ing.details.header"));
        nameCaptionLabel.setText(b.getString("ing.details.name"));
        densityCaptionLabel.setText(b.getString("ing.details.density"));
        nutritionHeaderLabel.setText(b.getString("ing.nutrition.header"));
        proteinCaptionLabel.setText(b.getString("ing.nutrition.protein"));
        fatCaptionLabel.setText(b.getString("ing.nutrition.fat"));
        carbsCaptionLabel.setText(b.getString("ing.nutrition.carbs"));
        kcalCaptionLabel.setText(b.getString("ing.nutrition.kcal"));

        editNameField.setPromptText(b.getString("ing.details.name.prompt"));
        editDensityField.setPromptText(b.getString("ing.details.density.prompt"));
        proteinTextField.setPromptText(b.getString("ing.nutrition.protein.prompt"));
        fatTextField.setPromptText(b.getString("ing.nutrition.fat.prompt"));
        carbsTextField.setPromptText(b.getString("ing.nutrition.carbs.prompt"));

        cancelEditDetailsButton.setText(b.getString("common.btn.cancel"));
        doneEditDetailsButton.setText(b.getString("common.btn.done"));

        cancelEditNutritionButton.setText(b.getString("common.btn.cancel"));
        doneEditNutritionButton.setText(b.getString("common.btn.done"));

        usedInRecipesLabel.setText(formatUsedInRecipes(usedInRecipesCount));

        // tooltips
        setTooltip(removeIngredientTypeButton, "common.tooltip.removeIngredient");
        setTooltip(addIngredientTypeButton, "common.tooltip.addIngredient");
        setTooltip(editIngredientTypeButton, "common.tooltip.editIngredient");
        setTooltip(editDetailsButton, "common.tooltip.editName");
        setTooltip(editDensityButton, "common.tooltip.editDensity");
        setTooltip(editNutritionButton, "common.tooltip.editNutrition");
    }

    // General
    @Inject
    public IngredientTypeOverviewCtrl(ServerUtility server,
                                      RecipeUtils recipeUtils,
                                      UserConfig user,
                                      MainCtrl mainCtrl, LanguageService languages) {
        this.server = server;
        this.recipeUtils = recipeUtils;
        this.user = user;
        this.mainCtrl = mainCtrl;
        this.languages = languages;
    }

    /**
     * Changes the scene between viewing and editing an ingredient type
     * @param value false for viewing mode, true for editing mode
     */
    @FXML
    private void changeViewEditMode(boolean value) {
        // While in edit mode, the user can't change to a different ingredient
        ingredientTypeSearchField.getParent().setDisable(value);

        sceneBox.getParent().setDisable(value);
        editIngredientTypeButton.setVisible(!value);
        cancelEditButton.getParent().setVisible(value);
        editDetailsButton.getParent().getParent().getParent().setVisible(value);

        editDensityButton.setVisible(value);
        editNutritionButton.getParent().getParent().getParent().setVisible(value);
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

        cancelEditButton.setDisable(value);
        doneEditButton.setDisable(value);
        editNutritionButton.setDisable(value);
        editDensityButton.setDisable(value);
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

        cancelEditButton.setDisable(value);
        doneEditButton.setDisable(value);
        editDetailsButton.setDisable(value);
        editDensityButton.setDisable(value);
    }

    /**
     * Counts in how many recipes an ingredient type is used
     * @param ingredientType the ingredient type to count for
     * @return the amount of recipes the ingredient type is used in
     */
    private List<Recipe> getUsedInRecipes(IngredientType ingredientType) {
        List<Recipe> res = new ArrayList<>();

        for (Recipe recipe : server.getRecipes()) {
            for (Ingredient ingredient : recipe.ingredients) {
                if (ingredient.ingredientType.id == ingredientType.id) {
                    res.add(recipe);
                    break;
                }
            }
        }
        return res;
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

        int usedInRecipes = getUsedInRecipes(ingredientType).size();
        usedInRecipesCount = usedInRecipes;
        usedInRecipesLabel.setText(formatUsedInRecipes(usedInRecipesCount));

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

        cancelEditButton.setDisable(value);
        doneEditButton.setDisable(value);
        editDetailsButton.setDisable(value);
        editNutritionButton.setDisable(value);

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

        applyTranslations();
        onRefresh();

        sceneBox.getItems().addAll("Home", "Recipe overview", "Ingredient overview",
                "Shopping list");

        ingredientTypeListView.getSelectionModel().selectedItemProperty().addListener(
                (observable,
                 oldIngredientType, newIngredientType) -> {
                    onRefresh();
                }
        );

        sceneBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> {
                    mainCtrl.showScene(newValue);
                });
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

        List<Recipe> usedInRecipes = getUsedInRecipes(ingredientType);

        if (usedInRecipes.isEmpty()) {
            server.deleteIngredientType(ingredientType.id);
            onRefresh();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL); // disables the main stage
        ResourceBundle b = languages.bundle();
        alert.setTitle(b.getString("ing.alert.removeUsed.title"));
        alert.setHeaderText(null);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/FoodPalLogo.png"))));

        int count = usedInRecipes.size();

        String messageKey = (count == 1) ? "ing.alert.removeUsed.one" : "ing.alert.removeUsed.many";

        Label content = new Label(MessageFormat.format(b.getString(messageKey), count));

//        Label content = new Label("This ingredient is used in " + usedInRecipes.size() + " " +
//                (usedInRecipes.size() == 1 ? "recipe" : "different recipes") + ". Removing " +
//                "it will remove it from the following recipe" + (usedInRecipes.size() == 1
//                ? ":" : "s:"));
        content.setWrapText(true);
        content.setMinHeight(50);

        ListView<Recipe> recipeListView = new ListView<>(FXCollections.observableList(usedInRecipes));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(content, recipeListView);
        vbox.setPrefWidth(400);
        vbox.setPrefHeight(200);
        vbox.setSpacing(5);
        alert.getDialogPane().setContent(vbox);

        ButtonType cancelButton = new ButtonType(b.getString("common.btn.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType okButton = new ButtonType(b.getString("common.btn.ok"), ButtonBar.ButtonData.OK_DONE);
        alert.getDialogPane().getButtonTypes().setAll(cancelButton, okButton);

        Optional<ButtonType> res = alert.showAndWait();

        if (res.isPresent() && res.get() == okButton) {
            for (Recipe recipe : usedInRecipes) {
                if (recipe.ingredients.removeIf(ingredient ->
                        ingredient.ingredientType.id == ingredientType.id)) {
                    server.updateRecipe(recipe.id, recipe);
                }
            }
            server.deleteIngredientType(ingredientType.id);
            onRefresh();
        }
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

        newIngredientType = true;
        onEditIngredientTypeButton();
        nameLabel.setText("-");
        editDetailsButton.requestFocus();
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
        if (newIngredientType) {
            IngredientType ingredientType = ingredientTypeListView.getSelectionModel().getSelectedItem();
            server.deleteIngredientType(ingredientType.id);
        }
        onRefresh();
        changeViewEditMode(false);
        newIngredientType = false;
    }

    /**
     * On action method for the Done Edit button
     * All the changes are added to the selected ingredient type
     */
    @FXML
    private void onDoneEditButton() throws JsonProcessingException {
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

        System.out.println("Ingredient type to be updated: " +
                new ObjectMapper().writeValueAsString(ingredientType));
        server.updateIngredientType(ingredientType.id, ingredientType);

        onRefresh();
        changeViewEditMode(false);

        // Update all recipes that use this ingredient type
        for (Recipe recipe : getUsedInRecipes(ingredientType)) {
            server.updateRecipe(recipe.id, recipe);
        }

        newIngredientType = false;
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
        List<TextInputControl> textFields = new ArrayList<>();
        textFields.add(editNameField);
        String inputName;
        if(!editNameField.getText().isEmpty()) {
            inputName = editNameField.getText().trim();
        } else {
            inputName = null;
        }

        if (inputName == null || inputName.isBlank()) {
            System.out.println("The ingredient type needs a name.");
            recipeUtils.displayAlertInputWarning("ingredient.warning.empty.name", textFields);

            return;
        }

        if(inputName != null && inputName.length()>50) {
            System.out.println("The ingredient type name exceeds 50 characters!");
            recipeUtils.displayAlertInputWarning("ingredient.warning.name.exceeds.limit", textFields);
            return;
        }

        if (Character.isDigit(inputName.trim().charAt(0))) {
            System.out.println("The ingredient type name cannot start with a digit.");
            recipeUtils.displayAlertInputWarning("ingredient.warning.number", textFields);
            return;
        }

        // check if ingredient type is unique
        boolean isDuplicate = ingredientTypeListView.getItems().stream().anyMatch(
                x -> x.name.equals(inputName));
        if (isDuplicate) {
            System.out.println("The name of the ingredient type must be unique!");
            recipeUtils.displayAlertInputWarning("recipe.warning.ing.duplicate", null);
            return;
        }

        nameLabel.setText(inputName.trim());
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
        List<TextInputControl> textFields = new ArrayList<>();
        textFields.add(editDensityField);
        editDensityField.setText(editDensityField.getText().trim());
        if (editDensityField.getText().isBlank()) {
            densityLabel.setText("");
            changeDensityViewEditMode(false);
            return;
        }
        double density;

        if(!editDensityField.getText().isBlank() && editDensityField.getText().length()>6) {
            System.out.println("The density exceeds 8 characters!");
            recipeUtils.displayAlertInputWarning("ingredient.warning.density.exceeds.limit", textFields);
            return;
        }
        try {
            density = Double.parseDouble(editDensityField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Density must be a double");
            recipeUtils.displayAlertInputWarning("ingredient.warning.double.density", textFields);
            return;
        }
        //Check if ingredient density is larger than the density of Osmium
        if(density > 22.6) {
            System.out.println("The density is out of this world");
            recipeUtils.displayAlertInputWarning("ingredient.warning.density.exceeds.limit", textFields);
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
        List<TextInputControl> textFields = new ArrayList<>();

        String pText = proteinTextField.getText().trim();
        if (!pText.isEmpty()) {
            try {
                if(pText.length()>6)
                    textFields.add(proteinTextField);
                if (Double.parseDouble(pText) < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                textFields.add(proteinTextField);
            }
        }

        String fText = fatTextField.getText().trim();
        if (!fText.isEmpty()) {
            try {
                if(fText.length()>6)
                    textFields.add(fatTextField);
                if (Double.parseDouble(fText) < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                textFields.add(fatTextField);
            }
        }

        String cText = carbsTextField.getText().trim();
        if (!cText.isEmpty()) {
            try {
                if(cText.length()>6)
                    textFields.add(carbsTextField);
                if (Double.parseDouble(cText) < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                textFields.add(carbsTextField);
            }
        }

        if (!textFields.isEmpty()) {
            recipeUtils.displayAlertInputWarning("ingredient.warning.nutrition", textFields);
            System.out.println("Invalid nutritional input detected in specific fields.");
            return;
        }

        proteinLabel.setText(pText.isEmpty() ? "-" : pText + "g");
        fatLabel.setText(fText.isEmpty() ? "-" : fText + "g");
        carbsLabel.setText(cText.isEmpty() ? "-" : cText + "g");
        kcalLabel.setText("-");
        changeNutritionViewEditMode(false);
    }
}
