package client;

import client.utils.*;

import static com.google.inject.Guice.createInjector;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import client.scenes.*;
import client.utils.JsonUserStorage;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = INJECTOR.getInstance(MyFXML.class);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> params = getParameters().getUnnamed();
        System.out.println("Command line parameters:" + params);
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).equals("-cfg") && i + 1 < params.size()) {
                String path = getParameters().getUnnamed().get(i + 1);
                try {
                    new File(path).getCanonicalPath();
                } catch (IOException | NullPointerException e) {
                    break;
                }
                if (!Files.isDirectory(Path.of(path))) {
                    break;
                }
                UserStorage storage = INJECTOR.getInstance(UserStorage.class);
                if (storage.getClass() == JsonUserStorage.class) {
                    ((JsonUserStorage) storage).setFilePath(path + JsonUserStorage.FILE_NAME);
                }
                break;
            }
        }

        var serverUtils = INJECTOR.getInstance(ServerUtility.class);
        if (!serverUtils.isServerAvailable()) {
            var msg = "Server needs to be started before the client, but it does not seem to be available. Shutting down.";
            System.err.println(msg);
            return;
        }

        var homeScreen = FXML.load(HomeScreenCtrl.class,
                "client" , "scenes", "HomeScreen.fxml");
        var recipeOverview = FXML.load(RecipeOverviewCtrl.class,
                "client" , "scenes", "RecipeOverview.fxml");
        var ingredientOverview = FXML.load(IngredientTypeOverviewCtrl.class,
                "client" , "scenes", "IngredientOverview.fxml");
        var shoppingList = FXML.load(ShoppingListCtrl.class,
                "client", "scenes", "ShoppingList.fxml");
        var addToShoppingList = FXML.load(AddToShoppingListCtrl.class,
                "client", "scenes", "AddToShoppingList.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, homeScreen, recipeOverview, ingredientOverview, shoppingList, addToShoppingList);
    }
}