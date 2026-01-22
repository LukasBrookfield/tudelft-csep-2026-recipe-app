package client.scenes;

import client.MyFXML;
import client.utils.RecipeUtils;
import client.utils.ServerUtility;
import client.utils.UserConfig;
import com.google.inject.Injector;
import commons.*;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IngredientTypeOverviewCtrlTest {
    // ingredient overview utils
    private IngredientTypeOverviewCtrl sut;
    private ServerUtility server;
    private UserConfig user;
    private RecipeUtils recipeUtils;

    // FXML components
    private TextField ingredientTypeSearchField;
    private ListView<IngredientType> ingredientTypeListView;
    private Button removeIngredientTypeButton;
    private Button addIngredientTypeButton;
    private Button toggleOverviewButton;
    private Button homeButton;
    private Label ingredientTypeTitleLabel;
    private Button editIngredientTypeButton;
    private Button cancelEditButton;
    private Button doneEditButton;
    private Separator mainSeparator;
    private Label nameLabel;
    private Button editDetailsButton;
    private TextField editNameField;
    private Button cancelEditDetailsButton;
    private Button doneEditDetailsButton;
    private Button editDensityButton;
    private TextField editDensityField;
    private Button cancelEditDensityButton;
    private Button doneEditDensityButton;
    private Label densityLabel;
    private Label proteinLabel;
    private Label fatLabel;
    private Label carbsLabel;
    private Label kcalLabel;
    private TextField proteinTextField;
    private TextField fatTextField;
    private TextField carbsTextField;
    private Button editNutritionButton;
    private Button cancelEditNutritionButton;
    private Button doneEditNutritionButton;
    private Label usedInRecipesLabel;
    private ChoiceBox<Category> categoryBox;
    private ChoiceBox<String> sortChoiceBox;

    @Start
    private void start(Stage stage) {
        Injector injector = createInjector(new TestModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<IngredientTypeOverviewCtrl, Parent> loaded = fxml.load(
                IngredientTypeOverviewCtrl.class,
                "client", "scenes", "IngredientOverview.fxml"
        );

        sut = loaded.getKey();

        var scene = new Scene(loaded.getValue());
        stage.setScene(scene);
        stage.show();
        stage.toFront();

        // initialize utils using the injector
        server = injector.getInstance(ServerUtility.class);
        user = injector.getInstance(UserConfig.class);
        recipeUtils = injector.getInstance(RecipeUtils.class);

        // <-- initialize all fields using the lookup method -->
        // sidebar
        ingredientTypeSearchField = lookup(scene, "#ingredientTypeSearchField");
        ingredientTypeListView = lookup(scene, "#ingredientTypeListView");
        removeIngredientTypeButton = lookup(scene, "#removeIngredientTypeButton");
        addIngredientTypeButton = lookup(scene, "#addIngredientTypeButton");

        // navigation buttons
        toggleOverviewButton = lookup(scene, "#toggleOverviewButton");
        homeButton = lookup(scene, "#homeButton");

        // ingredient type body
        ingredientTypeTitleLabel = lookup(scene, "#ingredientTypeTitleLabel");
        mainSeparator = lookup(scene, "#mainSeparator");
        nameLabel = lookup(scene, "#nameLabel");
        densityLabel = lookup(scene, "#densityLabel");
        proteinLabel = lookup(scene, "#proteinLabel");
        fatLabel = lookup(scene, "#fatLabel");
        carbsLabel = lookup(scene, "#carbsLabel");
        kcalLabel = lookup(scene, "#kcalLabel");
        usedInRecipesLabel = lookup(scene, "#usedInRecipesLabel");

        // edit buttons
        editNameField = lookup(scene, "#editNameField");
        editDetailsButton = lookup(scene, "#editDetailsButton");
        editIngredientTypeButton = lookup(scene, "#editIngredientTypeButton");
        editDensityButton = lookup(scene, "#editDensityButton");
        editNutritionButton = lookup(scene, "#editNutritionButton");

        // done edit buttons
        doneEditButton = lookup(scene, "#doneEditButton");
        doneEditDetailsButton = lookup(scene, "#doneEditDetailsButton");
        doneEditDensityButton = lookup(scene, "#doneEditDensityButton");
        doneEditNutritionButton = lookup(scene, "#doneEditNutritionButton");

        // cancel edit buttons
        cancelEditButton = lookup(scene, "#cancelEditButton");
        cancelEditDetailsButton = lookup(scene, "#cancelEditDetailsButton");
        cancelEditDensityButton = lookup(scene, "#cancelEditDensityButton");
        cancelEditNutritionButton = lookup(scene, "#cancelEditNutritionButton");

        // text fields
        proteinTextField = lookup(scene, "#proteinTextField");
        fatTextField = lookup(scene, "#fatTextField");
        editDensityField = lookup(scene, "#editDensityField");
        carbsTextField = lookup(scene, "#carbsTextField");

        categoryBox = lookup(scene, "#categoryBox");
        sortChoiceBox = lookup(scene, "#sortChoiceBox");
    }

    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    private void addIngredientType(FxRobot robot, String name) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDetailsButton);
        robot.clickOn(editNameField);
        robot.write(name);
        robot.clickOn(doneEditDetailsButton);
    }

    @Test
    void addIngredientTypeTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        IngredientType type = new IngredientType("New ingredient", null, new ArrayList<>(), null);
        type.id = 1;
        // check if ingredient type has been added to the list view
        assertTrue(ingredientTypeListView.getItems().contains(type));

        // check all labels are correct
        assertEquals("New ingredient", ingredientTypeTitleLabel.getText());
        assertEquals("New ingredient", nameLabel.getText());
        assertEquals("-", densityLabel.getText());
        assertEquals("-", proteinLabel.getText());
        assertEquals("-", fatLabel.getText());
        assertEquals("-", carbsLabel.getText());
        assertEquals("-", kcalLabel.getText());
        assertEquals("This ingredient type is used in 0 recipes", usedInRecipesLabel.getText());
    }

    @Test
    void removeIngredientTypeTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");
        robot.clickOn(doneEditButton);
        robot.clickOn(removeIngredientTypeButton);

        // check if ingredient type has been removed from list view
        assertTrue(ingredientTypeListView.getItems().isEmpty());
    }

    @Test
    void removeIngredientUsedInRecipeTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");
        robot.clickOn(doneEditButton);

        // add recipe to server that uses this ingredient type
        Recipe recipe = new Recipe("Test recipe");
        IngredientType type = ingredientTypeListView.getItems().getFirst();
        recipe.ingredients.add(new Ingredient(type, 100.0, Unit.ML, recipe));
        server.addRecipe(recipe);

        // check if a warning appears if we try to remove the ingredient type
        robot.clickOn(removeIngredientTypeButton);
        robot.clickOn("OK");
        assertTrue(ingredientTypeListView.getItems().isEmpty());
    }

    @Test
    void cancelIngredientTypeTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");
        robot.clickOn(cancelEditButton);

        // check if new ingredient type has been cancelled
        assertTrue(ingredientTypeListView.getItems().isEmpty());
    }

    @Test
    void editIngredientTypeTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");
        robot.clickOn(doneEditButton);
        robot.clickOn(editIngredientTypeButton);
        robot.clickOn(editDetailsButton);
        robot.clickOn(editNameField);
        robot.push(KeyCode.CONTROL, KeyCode.A);
        robot.type(KeyCode.BACK_SPACE);
        robot.write("Edited ingredient type");
        robot.clickOn(doneEditDetailsButton);
        robot.clickOn(doneEditButton);

        // check if ingredient type has been correctly edited
        assertEquals("Edited ingredient type", ingredientTypeTitleLabel.getText());
        assertEquals("Edited ingredient type", nameLabel.getText());
    }

    @Test
    void editNameTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");

        // check if name label has been updated
        assertEquals("Test ingredient type", nameLabel.getText());
    }

    @Test
    void cancelEditNameTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDetailsButton);
        robot.clickOn(editNameField);
        robot.write("New ingredient type");
        robot.clickOn(cancelEditDetailsButton);

        // check if edit has been cancelled
        assertEquals("New ingredient", nameLabel.getText());
        assertEquals("New ingredient", ingredientTypeTitleLabel.getText());
    }

    @Test
    void invalidNameTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDetailsButton);
        robot.clickOn(editNameField);
        robot.write("    ");
        robot.clickOn(doneEditDetailsButton);

        // check that edit hasn't gone through
        assertEquals("New ingredient", nameLabel.getText());
    }

    @Test
    void editDensityTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDensityButton);
        robot.clickOn(editDensityField);
        robot.write("10");
        robot.clickOn(doneEditDensityButton);

        // check if density label has been updated
        assertEquals("10.0", densityLabel.getText());
    }

    @Test
    void cancelEditDensityTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDensityButton);
        robot.clickOn(editDensityField);
        robot.write("10");
        robot.clickOn(cancelEditDensityButton);

        // check if edit has been cancelled
        assertEquals("-", densityLabel.getText());
    }

    @Test
    void invalidDensityTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDensityButton);
        robot.clickOn(editDensityField);
        robot.write("abcdefg");
        robot.clickOn(doneEditDensityButton);
        robot.clickOn("OK");
        // check that edit has not gone through
        assertEquals("", densityLabel.getText());
    }

    @Test
    void blankDensityTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDensityButton);
        robot.clickOn(editDensityField);
        robot.write("");
        robot.clickOn(doneEditDensityButton);

        // check if the app allows no density
        assertEquals("", densityLabel.getText());
        assertFalse(doneEditDensityButton.isVisible());
        assertFalse(cancelEditDensityButton.isVisible());
        assertTrue(editDensityButton.isVisible());
    }

    @Test
    void editNutritionTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");
        robot.clickOn(editNutritionButton);
        robot.clickOn(proteinTextField);
        robot.write("15.5");
        robot.clickOn(fatTextField);
        robot.write("2.2");
        robot.clickOn(carbsTextField);
        robot.write("20");
        robot.clickOn(doneEditNutritionButton);
        robot.clickOn(doneEditButton);

        // check if all nutrition labels have been updated
        assertEquals("15.5g", proteinLabel.getText().replaceAll(",","."));
        assertEquals("2.2g", fatLabel.getText().replaceAll(",","."));
        assertEquals("20g", carbsLabel.getText().replaceAll(",","."));

        Nutrition n = new Nutrition(20.0, 15.5, 2.2);
        assertEquals(Math.round(recipeUtils.getCaloriesPer100g(
                        new IngredientType("New ingredient", n, new ArrayList<>(), null))),
                Double.valueOf(kcalLabel.getText()));
    }

    @Test
    void cancelEditNutritionTest(FxRobot robot) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editNutritionButton);
        robot.clickOn(proteinTextField);
        robot.write("40.75");
        robot.clickOn(cancelEditNutritionButton);

        // check if edit has been cancelled
        assertEquals("-", proteinLabel.getText());
        assertEquals("-", fatLabel.getText());
        assertEquals("-", carbsLabel.getText());
        assertEquals("-", kcalLabel.getText());
    }

    @Test
    void editCategoryTest(FxRobot robot) {
        addIngredientType(robot, "Test ingredient type");
        robot.interact(() -> categoryBox.getSelectionModel().select(Category.Dairy));
        robot.clickOn(doneEditButton);

        // check category has been set correctly
        IngredientType type = new IngredientType("Test ingredient type", null, new ArrayList<>(), null);
        type.id = 1;
        type.setCategory(Category.Dairy);
        assertTrue(ingredientTypeListView.getItems().contains(type));
    }
}
