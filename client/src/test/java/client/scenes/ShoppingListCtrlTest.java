package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import client.utils.ServerUtils;
import client.utils.TestUserStorage;
import client.utils.UserConfig;
import client.utils.UserStorage;
import commons.Ingredient;
import commons.User;
import jakarta.ws.rs.client.ClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private Button editIngredientButton;
    private Button addIngredientButton;
    private final List<Ingredient> shoppingList = new ArrayList<>();
    private final List<Long> favouriteRecipes = new ArrayList<>();
    private final User user = new User(favouriteRecipes, shoppingList);
    private final UserStorage userStorage = new TestUserStorage(user);
    private final UserConfig userConfig = new UserConfig(userStorage, user);

    @Start
    private void start(Stage shoppingListStage) throws IOException {
        // Load ShoppingList.fxml from the classpath
        var scene = getScene();

        shoppingListStage.setTitle("Shopping List");
        shoppingListStage.setScene(scene);
        shoppingListStage.show();

        // Initialize buttons (you can lookup buttons after loading the scene)
        addIngredientButton = lookup(scene, "#addIngredientButton");
        removeIngredientButton = lookup(scene, "#removeIngredientButton");
        editIngredientButton = lookup(scene, "#editIngredientButton");
    }

    private Scene getScene() throws IOException {
        URL url = getClass().getResource("/client/scenes/ShoppingList.fxml");
        FXMLLoader loader = new FXMLLoader(url);

        loader.setControllerFactory(type -> {
            if (type == ShoppingListCtrl.class) {
                return new ShoppingListCtrl(new ServerUtils(ClientBuilder.newClient()), new UserConfig(userStorage, user));
            }else{
                throw new RuntimeException();
            }
        });


        Parent parent = loader.load();
        return new Scene(parent);
    }

    @SuppressWarnings("unchecked")
    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    @Test
    void testBeforeAddingIngredientButton() {
        assertTrue(addIngredientButton.isVisible());
        assertFalse(removeIngredientButton.isVisible());
        assertFalse(editIngredientButton.isVisible());
    }
}