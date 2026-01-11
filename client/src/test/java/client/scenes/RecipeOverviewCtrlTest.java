package client.scenes;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.MyFXML;
import client.utils.LanguageService;
import client.utils.RecipeUtils;
import client.utils.ServerUtility;
import client.utils.UserConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import commons.Unit;
import javafx.scene.Node;
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
    private LanguageService languageService;

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
//        starTooltip = lookup(scene, "#starTooltip");
        starTooltip = starRecipeButton.getTooltip();
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
    }

    private void addStep(FxRobot robot, String step) {
        robot.clickOn(addStepButton);
        robot.write(step);
        robot.clickOn(doneEditStepButton);
    }

    private void addIngredient(FxRobot robot, String ingredientType, double amount, Unit unit) {
        robot.clickOn(addIngredientButton);
        robot.clickOn(editIngredientNameField);
        robot.write(ingredientType);
        robot.clickOn(nextEditIngredientButton);
        robot.interact(() -> editUnitBox.getSelectionModel().select(unit.toString()));
        robot.clickOn(editIngredientAmountField);
        robot.write(String.valueOf(amount));
        robot.clickOn(doneEditIngredientButton);
    }

    @Test
    void addRecipeButtonTest(FxRobot robot) {
        robot.clickOn(addRecipeButton);
        Recipe recipe = new Recipe("New recipe");

        // check if the recipe has been added to the server
        assertTrue(server.getRecipes().contains(recipe));

        // check if the recipe has been added to the list view
        assertTrue(recipeListView.getItems().contains(recipe));

        // check if label is correct
        assertEquals("New recipe", recipeTitleLabel.getText());
    }

    @Test
    void removeRecipeButtonTest(FxRobot robot) {
        // add and then remove a recipe
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(doneEditButton);
        robot.clickOn(removeRecipeButton);

        // check if the recipe has been deleted from the server
        assertTrue(server.getRecipes().isEmpty());

        // check if the recipe has been removed from the list view
        assertTrue(recipeListView.getItems().isEmpty());
    }
    
    @Test
    void cloneRecipeButtonTest(FxRobot robot) {
        // add a recipe and then clone it
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(doneEditButton);
        robot.clickOn(cloneRecipeButton);
        robot.clickOn(recipeTitleField);
        robot.write("Test recipe clone");
        robot.clickOn(doneEditButton);

        Recipe expected = new Recipe("Test recipe clone");
        expected.servings = 2;

        // check if the recipe has been added to the server
        assertTrue(server.getRecipes().contains(expected));

        // check if the recipe has been added to the list view
        assertTrue(recipeListView.getItems().contains(expected));

        // check if the cloned recipe is the same as the original
        expected.name = "Test recipe";
        assertEquals(expected, recipeListView.getItems().getFirst());
    }

    @Test
    void doneEditRecipeButtonTest(FxRobot robot) {

    }

    @Test
    void cancelEditRecipeButtonTest(FxRobot robot) {

    }

    @Test
    void addStepButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(addStepButton);

        // check if "New step" has been added to the steps list view
        assertTrue(preparationStepListView.getItems().contains("New step"));
    }

    @Test
    void doneEditStepButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(addStepButton);
        robot.write("Test step");
        robot.clickOn(doneEditStepButton);

        // check if step has been added to the list view
        assertTrue(preparationStepListView.getItems().contains("Test step"));
    }

    @Test
    void cancelEditStepButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(addStepButton);
        robot.clickOn(cancelEditStepButton);

        // check if step has been cancelled
        assertTrue(preparationStepListView.getItems().isEmpty());
    }

    @Test
    void removeStepButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        addStep(robot, "Test step");
        robot.clickOn("Test step");
        robot.clickOn(removeStepButton);

        // check if step has been removed
        assertTrue(preparationStepListView.getItems().isEmpty());
    }

//    @Test
//    void editStepButtonTest(FxRobot robot) {
//        addRecipe(robot, "Test recipe", 2);
//        addStep(robot, "Test step");
//        robot.clickOn("Test step");
//        robot.clickOn(editStepButton);
//        robot.write("Edit test step");
//        robot.clickOn(doneEditStepButton);
//
//        // check if step has been edited
//        assertEquals("Edit test step", preparationStepListView.getItems().getFirst());
//    }

    @Test
    void addIngredientButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(addIngredientButton);

        // check if new ingredient has been added to list view
        Ingredient newIngredient = new Ingredient(null, null, null,
                recipeListView.getItems().getFirst());
        assertTrue(ingredientListView.getItems().contains(newIngredient));
    }

    @Test
    void doneEditIngredientButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        addIngredient(robot, "tomato", 5.5, Unit.G);

        // check if ingredient has been added to list view
        Ingredient newIngredient = new Ingredient(
                new IngredientType("tomato", null, new ArrayList<>(), null),
                5.5,
                Unit.G,
                recipeListView.getSelectionModel().getSelectedItem());
        assertTrue(ingredientListView.getItems().contains(newIngredient));
    }

    @Test
    void cancelEditIngredientButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(addIngredientButton);
        robot.clickOn(cancelEditIngredientButton);

        // check if ingredient has been cancelled
        assertTrue(ingredientListView.getItems().isEmpty());
    }

    @Test
    void removeIngredientButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        addIngredient(robot, "potato", 37.5, Unit.G);
        Node cell = robot.from(ingredientListView).lookup(".list-cell").nth(0).query();
        robot.clickOn(cell);
        robot.clickOn(removeIngredientButton);

        // check if ingredient has been removed from list view
        assertTrue(ingredientListView.getItems().isEmpty());
    }

    @Test
    void editServingsButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(editServingsButton);
        robot.clickOn(editServingsField);
        robot.eraseText(1);
        robot.write("6");
        robot.clickOn(doneEditServingsButton);

        // check if servings have been edited
        assertEquals("6", servingsLabel.getText());
    }

    @Test
    void cancelEditServingsButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(editServingsButton);
        robot.clickOn(editServingsField);
        robot.eraseText(1);
        robot.write("6");
        robot.clickOn(cancelEditServingsButton);

        // check that servings haven't been changed
        assertEquals("2", servingsLabel.getText());
    }

    @Test
    void starRecipeButtonTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        robot.clickOn(doneEditButton);
        robot.clickOn(starRecipeButton);

        // check if recipe is now part of user's favourite recipes
        assertTrue(user.isFavouriteRecipe(recipeListView.getSelectionModel().getSelectedItem()));

        // now unstar recipe
        robot.clickOn(starRecipeButton);

        // check if recipe is now not a favourite
        assertFalse(user.isFavouriteRecipe(recipeListView.getSelectionModel().getSelectedItem()));
    }

    @Test
    void searchRecipesTest(FxRobot robot) {
        addRecipe(robot, "abc 123", 2);
        robot.clickOn(doneEditButton);
        addRecipe(robot, "123abc456", 3);
        robot.clickOn(doneEditButton);
        addRecipe(robot, "ab1c", 2);
        robot.clickOn(doneEditButton);
        robot.clickOn(recipeSearchField);
        robot.write("abc");

        // check if the right recipes show up after searching
        List<Recipe> recipes = recipeListView.getItems();
        Recipe recipe1 = new Recipe("abc 123", new ArrayList<>(), new ArrayList<>(), 2);
        assertTrue(recipes.contains(recipe1));
        Recipe recipe2 = new Recipe("123abc456", new ArrayList<>(), new ArrayList<>(), 3);
        assertTrue(recipes.contains(recipe2));
        Recipe recipe3 = new Recipe("ab1c", new ArrayList<>(), new ArrayList<>(), 2);
        assertFalse(recipes.contains(recipe3));
    }

    @Test
    void sortRecipesTest(FxRobot robot) {
        addRecipe(robot, "bbb", 3);
        robot.clickOn(doneEditButton);
        addRecipe(robot, "bbz", 2);
        robot.clickOn(doneEditButton);
        addRecipe(robot, "aaa", 2);
        robot.clickOn(doneEditButton);
        robot.interact(() -> sortChoiceBox.getSelectionModel().select("Name (A-Z)"));

        // check if the recipes are in the right order (alphabetical order in this case)
        List<Recipe> recipes = recipeListView.getItems();
        Recipe recipe1 = new Recipe("aaa", new ArrayList<>(), new ArrayList<>(), 2);
        assertEquals(recipe1, recipes.getFirst());
        Recipe recipe2 = new Recipe("bbb", new ArrayList<>(), new ArrayList<>(), 3);
        assertEquals(recipe2, recipes.get(1));
        Recipe recipe3 = new Recipe("bbz", new ArrayList<>(), new ArrayList<>(), 2);
        assertEquals(recipe3, recipes.get(2));
    }

    @Test
    void filterFavouriteRecipesTest(FxRobot robot) {
        addRecipe(robot, "Test recipe 1", 3);
        robot.clickOn(doneEditButton);
        robot.clickOn(starRecipeButton);
        addRecipe(robot, "Test recipe 2", 5);
        robot.clickOn(doneEditButton);
        robot.interact(() -> favouriteRecipeFilterBox.getSelectionModel().select("Favourites"));

        // check that the favourite recipes have been correctly filtered
        List<Recipe> recipes = recipeListView.getItems();
        Recipe recipe1 = new Recipe("Test recipe 1", new ArrayList<>(), new ArrayList<>(), 3);
        assertTrue(recipes.contains(recipe1));
        Recipe recipe2 = new Recipe("Test recipe 2", new ArrayList<>(), new ArrayList<>(), 5);
        assertFalse(recipes.contains(recipe2));
    }

    @Test
    void moveStepsTest(FxRobot robot) {
        addRecipe(robot, "Test recipe", 2);
        addStep(robot, "Step 1");
        addStep(robot, "Step 2");

        // test moving step up
        robot.clickOn(moveStepUpButton);
        List<String> steps = preparationStepListView.getItems();
        assertEquals("Step 2", steps.getFirst());
        assertEquals("Step 1", steps.getLast());

        // test moving step down
        robot.clickOn(moveStepDownButton);
        steps = preparationStepListView.getItems();
        assertEquals("Step 1", steps.getFirst());
        assertEquals("Step 2", steps.getLast());
    }
}
