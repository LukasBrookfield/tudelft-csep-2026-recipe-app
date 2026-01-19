package client.scenes;

import java.io.IOException;
import java.util.ArrayList;

import client.MyFXML;
import client.utils.*;
import com.google.inject.Injector;
import commons.Ingredient;
import commons.IngredientType;
import commons.ShoppingListItem;
import commons.Unit;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class ShoppingListCtrlTest {
    // shopping list utils
    private ShoppingListCtrl sut;
    private ServerUtility server;
    private UserConfig user;
    private LanguageService languages;

    // FXML components
    private ListView<Ingredient> ingredientListView;
    private Button removeIngredientButton;
    private TextField editIngredientNameField;
    private TextField editIngredientAmountField;
    private ChoiceBox<String> editUnitBox;
    private Button addIngredientButton;
    private Button editIngredientButton;
    private Button cancelEditIngredientButton;
    private Button doneEditIngredientButton;
    private StackPane editPane;
    private HBox editIngredientBox;
    private ComboBox<IngredientType> editIngredientTypeBox;
    private ChoiceBox<IngredientType> editIngredientChoiceBox;
    private Button nextButton;
    private Button exitButton;
    private Label shoppingListHeaderLabel;
    private Button backEditIngredientButton;


    @Start
    private void start(Stage stage) throws IOException {
        Injector injector = createInjector(new TestModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<ShoppingListCtrl, Parent> loaded = fxml.load(
                ShoppingListCtrl.class,
                "client", "scenes", "ShoppingList.fxml"
        );

        sut = loaded.getKey();

        var scene = new Scene(loaded.getValue());
        stage.setScene(scene);
        stage.show();
        stage.toFront();

        // initialize utils using the injector
        server = injector.getInstance(ServerUtility.class);
        user = injector.getInstance(UserConfig.class);
        languages = injector.getInstance(LanguageService.class);

        // Initialize buttons (you can lookup buttons after loading the scene)
        ingredientListView = lookup(scene, "#ingredientListView");
        removeIngredientButton = lookup(scene, "#removeIngredientButton");
        editIngredientNameField = lookup(scene, "#editIngredientNameField");
        editIngredientAmountField = lookup(scene, "#editIngredientAmountField");
        editUnitBox = lookup(scene, "#editUnitBox");
        addIngredientButton = lookup(scene, "#addIngredientButton");
        editIngredientButton = lookup(scene, "#editIngredientButton");
        cancelEditIngredientButton = lookup(scene, "#cancelEditIngredientButton");
        doneEditIngredientButton = lookup(scene, "#doneEditIngredientButton");
        editPane = lookup(scene, "#editPane");
        editIngredientBox = lookup(scene, "#editIngredientBox");
        editIngredientTypeBox = lookup(scene, "#editIngredientTypeBox");
        editIngredientChoiceBox = lookup(scene, "#editIngredientChoiceBox");
        nextButton = lookup(scene, "#nextButton");
        exitButton = lookup(scene, "#exitButton");
        backEditIngredientButton = lookup(scene, "#backEditIngredientButton");
        shoppingListHeaderLabel = lookup(scene, "#shoppingListHeaderLabel");
    }

    @SuppressWarnings("unchecked")
    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    private void addItem(FxRobot robot, String ingredientType, Unit unit, double amount) {
        robot.clickOn(addIngredientButton);
        robot.clickOn(editIngredientTypeBox);
        robot.write(ingredientType);
        robot.clickOn(nextButton);
        robot.interact(() -> editUnitBox.getSelectionModel().select(unit.toString()));
        robot.clickOn(editIngredientAmountField);
        robot.write(String.valueOf(amount));
    }

    @Test
    void testBeforeAddingItemButton() {
        assertTrue(addIngredientButton.isVisible());
        assertTrue(removeIngredientButton.isVisible());
        assertTrue(editIngredientButton.isVisible());
    }

    @Test
    void testAfterAddingItemButton(FxRobot robot) {
        robot.clickOn(addIngredientButton);

        assertFalse(addIngredientButton.isVisible());
        assertFalse(removeIngredientButton.isVisible());
        assertFalse(editIngredientButton.isVisible());
    }

    @Test
    void addItemTest(FxRobot robot) {
        robot.clickOn(addIngredientButton);

        // check if new item has been added to list view
        assertTrue(ingredientListView.getItems().contains(
                new ShoppingListItem(new Ingredient(null, null, null, null))));
    }

    @Test
    void doneAddItemTest(FxRobot robot) {
        addItem(robot, "Test ingredient", Unit.G, 125.5);
        robot.clickOn(doneEditIngredientButton);

        Ingredient ing = new Ingredient(
                new IngredientType("Test ingredient", null, new ArrayList<>(), null),
                125.5, Unit.G, null);
        ShoppingListItem item = new ShoppingListItem(ing);

        // check if item has been edited in list view
        assertTrue(ingredientListView.getItems().contains(item));

        // check if item has been saved to the local user file
        assertTrue(user.getShoppingList().contains(item));
    }

    @Test
    void cancelAddItemTest(FxRobot robot) {
        addItem(robot, "Test ingredient", Unit.G, 125.5);
        robot.clickOn(backEditIngredientButton);
        robot.clickOn(cancelEditIngredientButton);

        // check if item has been removed from list view
        assertTrue(ingredientListView.getItems().isEmpty());

        // check if item has not been added to the user config file
        assertTrue(user.getShoppingList().isEmpty());
    }

    @Test
    void removeItemTest(FxRobot robot) {
        addItem(robot, "Test ingredient", Unit.ML, 44.4);
        robot.clickOn(doneEditIngredientButton);
        robot.clickOn(removeIngredientButton);

        // check if item has been removed from list view
        assertTrue(ingredientListView.getItems().isEmpty());

        // check if item has been removed from local user file
        assertTrue(user.getShoppingList().isEmpty());
    }

    @Test
    void editItemTest(FxRobot robot) {
        addItem(robot, "Test ingredient", Unit.G, 125.5);
        robot.clickOn(doneEditIngredientButton);
        robot.clickOn(editIngredientButton);
        robot.clickOn(editIngredientTypeBox).type(KeyCode.END);
        robot.eraseText(20);
        robot.write("EDIT");
        robot.clickOn(nextButton);
        robot.clickOn(doneEditIngredientButton);

        Ingredient ing = new Ingredient(
                new IngredientType("EDIT", null, new ArrayList<>(), null),
                125.5, Unit.G, null);
        ShoppingListItem item =  new ShoppingListItem(ing);

        // check if item has been edited in list view
        assertTrue(ingredientListView.getItems().contains(item));

        // check if item has been edited in local user file
        assertTrue(user.getShoppingList().contains(item));
    }
}
