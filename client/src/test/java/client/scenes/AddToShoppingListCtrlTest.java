package client.scenes;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import client.MyFXML;
import client.utils.LanguageService;
import client.utils.ServerUtility;
import client.utils.ShoppingListUtils;
import client.utils.UserConfig;
import com.google.inject.Injector;
import commons.IngredientType;
import commons.ShoppingListItem;
import commons.Unit;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class AddToShoppingListCtrlTest {

    private AddToShoppingListCtrl sut;
    private ServerUtility server;
    private UserConfig user;
    private LanguageService languages;
    private ShoppingListUtils shoppingListUtils;

    private ListView<ShoppingListItem> ingredientListView;
    private Button removeIngredientButton;
    private TextField editIngredientAmountField;
    private ChoiceBox<String> editUnitBox;
    private Button addIngredientButton;
    private Button editIngredientButton;
    private Button cancelEditIngredientButton;
    private Button doneEditIngredientButton;
    private StackPane editPane;
    private HBox editIngredientBox;
    private HBox editIngredientTypeContainer;
    private ComboBox<IngredientType> editIngredientTypeBox;
    private Button nextButton;
    private Button exitButton;
    private Button confirmation;
    private Button backEditIngredientButton;

    @Start
    private void start(Stage stage) throws IOException {
        Injector injector = createInjector(new TestModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<AddToShoppingListCtrl, Parent> loaded = fxml.load(
                AddToShoppingListCtrl.class,
                "client", "scenes", "AddToShoppingList.fxml"
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
        shoppingListUtils = injector.getInstance(ShoppingListUtils.class);

        // initialize fields using lookup
        ingredientListView = lookup(scene, "#ingredientListView");
        removeIngredientButton = lookup(scene, "#removeIngredientButton");
        editIngredientAmountField = lookup(scene, "#editIngredientAmountField");
        editUnitBox = lookup(scene, "#editUnitBox");
        addIngredientButton = lookup(scene, "#addIngredientButton");
        editIngredientButton = lookup(scene, "#editIngredientButton");
        cancelEditIngredientButton = lookup(scene, "#cancelEditIngredientButton");
        doneEditIngredientButton = lookup(scene, "#doneEditIngredientButton");
        editPane = lookup(scene, "#editPane");
        editIngredientBox = lookup(scene, "#editIngredientBox");
        editIngredientTypeContainer = lookup(scene, "#editIngredientTypeContainer");
        editIngredientTypeBox = lookup(scene, "#editIngredientTypeBox");
        nextButton = lookup(scene, "#nextButton");
        exitButton = lookup(scene, "#exitButton");
        confirmation = lookup(scene, "#confirmation");
        backEditIngredientButton = lookup(scene, "#backEditIngredientButton");
    }

    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    private void addItem(FxRobot robot, String name, double amount, Unit unit) {
        robot.clickOn(addIngredientButton);
        robot.clickOn(editIngredientTypeBox);
        robot.write(name);
        robot.clickOn(nextButton);
        robot.interact(() -> editUnitBox.getSelectionModel().select(unit.toString()));
        robot.clickOn(editIngredientAmountField);
        robot.write(String.valueOf(amount));
        robot.clickOn(doneEditIngredientButton);
    }

    @Test
    void addItemTest(FxRobot robot) {
        robot.clickOn(addIngredientButton);

        // check a new item was added to the list view
        assertEquals(1, ingredientListView.getItems().size());
        assertNull(ingredientListView.getItems().get(0).getIngredient().ingredientType);
    }

    @Test
    void removeItemTest(FxRobot robot) {
        addItem(robot, "TestItem", 10.0, Unit.G);
        robot.clickOn(ingredientListView);
        robot.clickOn(removeIngredientButton);

        assertTrue(ingredientListView.getItems().isEmpty());
    }

    @Test
    void doneAddItemTest(FxRobot robot) {
        addItem(robot, "Carrot", 5.0, Unit.KG);

        ShoppingListItem item = ingredientListView.getItems().get(0);
        assertEquals("Carrot", item.getIngredient().ingredientType.name);
        assertEquals(5.0, item.getIngredient().amount);
        assertEquals(Unit.KG, item.getIngredient().unit);
    }

    @Test
    void cancelEditTest(FxRobot robot) {
        robot.clickOn(addIngredientButton);
        robot.clickOn(editIngredientTypeBox);
        robot.write("Tomato");
        robot.clickOn(cancelEditIngredientButton);

        assertTrue(ingredientListView.getItems().isEmpty());
    }

    @Test
    void editItemTest(FxRobot robot) {
        addItem(robot, "Potato", 2.0, Unit.KG);
        robot.clickOn(ingredientListView);
        robot.clickOn(editIngredientButton);
        robot.clickOn(editIngredientTypeBox);
        robot.eraseText(10);
        robot.write("Sweet Potato");
        robot.clickOn(nextButton);
        robot.clickOn(editIngredientAmountField);
        robot.eraseText(5);
        robot.write("3.0");
        robot.clickOn(doneEditIngredientButton);

        ShoppingListItem item = ingredientListView.getItems().get(0);
        assertEquals("Sweet Potato", item.getIngredient().ingredientType.name);
        assertEquals(3.0, item.getIngredient().amount);
    }
}