package client.scenes;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import client.MyFXML;
import client.utils.RecipeUtils;
import client.utils.ServerUtility;
import client.utils.UserConfig;
import com.google.inject.Injector;
import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class RecipeOverviewCtrlTest {
    // recipe overview stuff
    private RecipeOverviewCtrl sut;
    private ServerUtility server;
    private UserConfig user;
    private RecipeUtils recipeUtils;

    // FXML components
    private AnchorPane rootPane;
    private TextField recipeSearchField;
    private ChoiceBox<String> favouriteRecipeFilterBox;
    private ListView<Recipe> recipeListView;
    private Button removeRecipeButton;
    private Button addRecipeButton;
    private Button cloneRecipeButton;
    private Button shoppingListButton;
    private Button starRecipeButton;
    private Tooltip starTooltip;
    private Button downloadRecipeButton;
    private Button printRecipeButton;
    private Button toggleOverviewButton;
    private Button homeButton;
    private Label recipeTitleLabel;
    private TextField recipeTitleField;
    private Button editRecipeButton;
    private Button cancelEditButton;
    private Button doneEditButton;
    private Separator mainSeparator;
    private Label servingsLabel;
    private TextField editServingsField;
    private Button editServingsButton;
    private Button cancelEditServingsButton;
    private Button doneEditServingsButton;
    private Label ingredientsHeaderLabel;
    private ListView<Ingredient> ingredientListView;
    private Button removeIngredientButton;
    private Button addIngredientButton;
    private Button editIngredientButton;
    private ChoiceBox<IngredientType> editIngredientBox;
    private TextField editIngredientNameField;
    private Button cancelEditIngredientButton;
    private Button nextEditIngredientButton;
    private TextField editIngredientAmountField;
    private ChoiceBox<String> editUnitBox;
    private Button doneEditIngredientButton;
    private Button onBackEditIngredientButton;
    private Label preparationHeaderLabel;
    private ListView<String> preparationStepListView;
    private Button removeStepButton;
    private Button addStepButton;
    private Button editStepButton;
    private TextField editStepField;
    private Button moveStepUpButton;
    private Button moveStepDownButton;
    private Button cancelEditStepButton;
    private Button doneEditStepButton;
    private Label searchStatusLabel;
    private ChoiceBox<String> sortChoiceBox;
    private Label recipeKcalPer100gLabel;

    @Start
    private void start(Stage stage) throws IOException {
        Injector injector = createInjector(new TestModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<RecipeOverviewCtrl, Parent> loaded = fxml.load(
                RecipeOverviewCtrl.class,
                "client", "scenes", "RecipeOverview.fxml"
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

        // initialize all fields using the lookup method
        rootPane = lookup(scene, "#rootPane");
        recipeSearchField = lookup(scene, "#recipeSearchField");
        favouriteRecipeFilterBox = lookup(scene, "#favouriteRecipeFilterBox");
        recipeListView = lookup(scene, "#recipeListView");
        removeRecipeButton = lookup(scene, "#removeRecipeButton");
        addRecipeButton = lookup(scene, "#addRecipeButton");
        cloneRecipeButton = lookup(scene, "#cloneRecipeButton");
        shoppingListButton = lookup(scene, "#shoppingListButton");
        starRecipeButton = lookup(scene, "#starRecipeButton");
        starTooltip = lookup(scene, "#starTooltip");
        downloadRecipeButton = lookup(scene, "#downloadRecipeButton");
        printRecipeButton = lookup(scene, "#printRecipeButton");
        toggleOverviewButton = lookup(scene, "#toggleOverviewButton");
        homeButton = lookup(scene, "#homeButton");
        recipeTitleLabel = lookup(scene, "#recipeTitleLabel");
        recipeTitleField = lookup(scene, "#recipeTitleField");
        editRecipeButton = lookup(scene, "#editRecipeButton");
        cancelEditButton = lookup(scene, "#cancelEditButton");
        doneEditButton = lookup(scene, "#doneEditButton");
        mainSeparator = lookup(scene, "#mainSeparator");
        servingsLabel = lookup(scene, "#servingsLabel");
        editServingsField = lookup(scene, "#editServingsField");
        editServingsButton = lookup(scene, "#editServingsButton");
        cancelEditServingsButton = lookup(scene, "#cancelEditServingsButton");
        doneEditServingsButton = lookup(scene, "#doneEditServingsButton");
        ingredientsHeaderLabel = lookup(scene, "#ingredientsHeaderLabel");
        ingredientListView = lookup(scene, "#ingredientListView");
        removeIngredientButton = lookup(scene, "#removeIngredientButton");
        addIngredientButton = lookup(scene, "#addIngredientButton");
        editIngredientButton = lookup(scene, "#editIngredientButton");
        editIngredientBox = lookup(scene, "#editIngredientBox");
        editIngredientNameField = lookup(scene, "#editIngredientNameField");
        cancelEditIngredientButton = lookup(scene, "#cancelEditIngredientButton");
        nextEditIngredientButton = lookup(scene, "#nextEditIngredientButton");
        editIngredientAmountField = lookup(scene, "#editIngredientAmountField");
        editUnitBox = lookup(scene, "#editUnitBox");
        doneEditIngredientButton = lookup(scene, "#doneEditIngredientButton");
        onBackEditIngredientButton = lookup(scene, "#onBackEditIngredientButton");
        preparationHeaderLabel = lookup(scene, "#preparationHeaderLabel");
        preparationStepListView = lookup(scene, "#preparationStepListView");
        removeStepButton = lookup(scene, "#removeStepButton");
        addStepButton = lookup(scene, "#addStepButton");
        editStepButton = lookup(scene, "#editStepButton");
        editStepField = lookup(scene, "#editStepField");
        moveStepUpButton = lookup(scene, "#moveStepUpButton");
        moveStepDownButton = lookup(scene, "#moveStepDownButton");
        cancelEditStepButton = lookup(scene, "#cancelEditStepButton");
        doneEditStepButton = lookup(scene, "#doneEditStepButton");
        searchStatusLabel = lookup(scene, "#searchStatusLabel");
        sortChoiceBox = lookup(scene, "#sortChoiceBox");
        recipeKcalPer100gLabel = lookup(scene, "#recipeKcalPer100gLabel");
    }

    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    private void addRecipe(FxRobot robot, String name, int servings) {
        robot.clickOn(addRecipeButton);
        robot.write(name);
        robot.clickOn(editServingsButton);
        robot.clickOn(editServingsField);
        robot.eraseText(1);
        robot.write(String.valueOf(servings));
        robot.clickOn(doneEditServingsButton);
        robot.clickOn(doneEditButton);
    }

    @Test
    void addRecipeButtonTest(FxRobot robot) {
        robot.clickOn(addRecipeButton);
        Recipe recipe = new Recipe("New recipe");

        // test if the recipe has been added to the server
        assertTrue(server.getRecipes().contains(recipe));

        // test if the recipe has been added to the list view
        assertTrue(recipeListView.getItems().contains(recipe));

        // test if edit mode is activated when a recipe is added
        assertEquals("New recipe", recipeTitleLabel.getText());

        assertFalse(recipeTitleLabel.isVisible());
        assertTrue(recipeTitleField.isVisible());

        assertFalse(editRecipeButton.isVisible());

        assertTrue(editServingsButton.getParent().isVisible());
        assertTrue(removeIngredientButton.getParent().isVisible());
        assertTrue(removeStepButton.getParent().isVisible());

        assertTrue(cancelEditButton.isVisible());
        assertTrue(doneEditButton.isVisible());

        assertFalse(addRecipeButton.isVisible());
        assertFalse(removeRecipeButton.isVisible());
        assertFalse(cloneRecipeButton.isVisible());
        assertFalse(shoppingListButton.isVisible());

        assertTrue(recipeSearchField.isDisabled());
        assertTrue(sortChoiceBox.isDisabled());
        assertTrue(recipeListView.isDisabled());

        assertFalse(starRecipeButton.isVisible());
        assertFalse(downloadRecipeButton.isVisible());
        assertFalse(printRecipeButton.isVisible());
        assertFalse(toggleOverviewButton.isVisible());
        assertFalse(homeButton.isVisible());

        assertTrue(favouriteRecipeFilterBox.isDisabled());
    }

    @Test
    void removeRecipeButtonTest(FxRobot robot) {
        // add and then remove a recipe
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(removeRecipeButton);

        // test if the recipe has been deleted from the server
        assertTrue(server.getRecipes().isEmpty());

        // test if the recipe has been removed from the list view
        assertTrue(recipeListView.getItems().isEmpty());
    }
    
    @Test
    void cloneRecipeButtonTest(FxRobot robot) {
        // add a recipe and then clone it
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(cloneRecipeButton);
        robot.clickOn(recipeTitleField);
        robot.write("Test recipe clone");
        robot.clickOn(doneEditButton);

        Recipe expected = new Recipe("Test recipe clone");
        expected.servings = 2;

        // test if the recipe has been added to the server
        assertTrue(server.getRecipes().contains(expected));

        // test if the recipe has been added to the list view
        assertTrue(recipeListView.getItems().contains(expected));

        // check if the cloned recipe is the same as the original
        expected.name = "Test recipe";
        assertEquals(expected, recipeListView.getItems().getFirst());
    }
}
