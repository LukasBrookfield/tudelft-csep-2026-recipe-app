package client.scenes;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import client.utils.UserConfig;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
public class ShoppingListCtrlTest {

    private Button removeIngredientButton;
    private Button addIngredientButton;
    private Button editIngredientButton;
    private Button cancelEditIngredientButton;

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
    }

    private Scene getScene() throws IOException {
        File path = new File("src/main/resources/client/scenes/ShoppingList.fxml");
        URL url = path.toURI().toURL();
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
    void testCancelEditIngredientButton(FxRobot robot) {
        robot.clickOn(addIngredientButton);
        assertTrue(cancelEditIngredientButton.isVisible());
    }
}