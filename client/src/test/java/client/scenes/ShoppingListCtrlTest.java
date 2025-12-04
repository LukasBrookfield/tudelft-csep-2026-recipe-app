package client.scenes;

import java.io.IOException;
import java.net.URL;

import client.utils.UserConfig;
import commons.Ingredient;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
public class ShoppingListCtrlTest {

    private Button removeIngredientButton;
    private Button addIngredientButton;
    private Button editIngredientButton;
    private Button cancelEditIngredientButton;
    private ListView<Ingredient> ingredientListView;
    private TextField editIngredientNameField;
    private TextField editIngredientAmountField;
    private ChoiceBox<String> editUnitBox;
    private Button doneEditIngredientButton;

//    m
    @Start
    private void start(Stage shoppingListStage) throws IOException {
        // Load ShoppingList.fxml from the classpath
        var scene = getScene();

        shoppingListStage.setTitle("Shopping List");
        shoppingListStage.setScene(scene);
        shoppingListStage.show();

        // Initialize buttons (you can lookup buttons after loading the scene)
        removeIngredientButton = lookup(scene, "#removeIngredientButton");
        addIngredientButton = lookup(scene, "#addIngredientButton");
        editIngredientButton = lookup(scene, "#editIngredientButton");
        cancelEditIngredientButton = lookup(scene, "#cancelEditIngredientButton");
        ingredientListView = lookup(scene, "#ingredientListView");
        editUnitBox = lookup(scene, "#editUnitBox");
        editIngredientNameField = lookup(scene, "#editIngredientNameField");
        editIngredientAmountField = lookup(scene, "#editIngredientAmountField");
        doneEditIngredientButton =  lookup(scene, "#doneEditIngredientButton");
    }

    private Scene getScene() throws IOException {
        URL url = getClass().getResource("/client/scenes/ShoppingList.fxml");
        FXMLLoader loader = new FXMLLoader(url);

        loader.setControllerFactory(type -> {
            if (type == ShoppingListCtrl.class) {
                return new ShoppingListCtrl(null, new UserConfig("src/test/java/client/scenes"));
            }else{
                throw new RuntimeException();
            }
        });


        Parent parent = loader.load();
        var scene = new Scene(parent);
        return scene;
    }

    @SuppressWarnings("unchecked")
    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    @Test
    void testBeforeAddingIngredientButton(FxRobot robot) {
        assertTrue(addIngredientButton.isVisible());
        assertFalse(removeIngredientButton.isVisible());
        assertFalse(editIngredientButton.isVisible());
        assertFalse(cancelEditIngredientButton.isVisible());
        assertFalse(editIngredientAmountField.isVisible());
        assertFalse(editUnitBox.isVisible());
        assertFalse(editIngredientNameField.isVisible());
        assertFalse(doneEditIngredientButton.isVisible());
    }

    @Test
    void testAfterAddIngredientButton(FxRobot robot) {
        robot.clickOn(addIngredientButton);

        assertFalse(addIngredientButton.isVisible());
        assertFalse(removeIngredientButton.isVisible());
        assertFalse(editIngredientButton.isVisible());
        assertTrue(cancelEditIngredientButton.isVisible());
        assertTrue(editIngredientAmountField.isVisible());
        assertTrue(editUnitBox.isVisible());
        assertTrue(editIngredientNameField.isVisible());
        assertTrue(doneEditIngredientButton.isVisible());
    }

    @Test
    void testAfterCancelEditIngredientButton(FxRobot robot) {
        robot.clickOn(addIngredientButton);
        robot.clickOn(cancelEditIngredientButton);

        assertTrue(addIngredientButton.isVisible());
        assertTrue(removeIngredientButton.isVisible());
        assertTrue(editIngredientButton.isVisible());
        assertFalse(cancelEditIngredientButton.isVisible());
        assertFalse(editIngredientAmountField.isVisible());
        assertFalse(editUnitBox.isVisible());
        assertFalse(editIngredientNameField.isVisible());
        assertFalse(doneEditIngredientButton.isVisible());
    }
}