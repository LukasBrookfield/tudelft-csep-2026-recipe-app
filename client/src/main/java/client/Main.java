package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import client.utils.ServerUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        var serverUtils = INJECTOR.getInstance(ServerUtils.class);
        if (!serverUtils.isServerAvailable()) {
            var msg = "Server needs to be started before the client, but it does not seem to be available. Shutting down.";
            System.err.println(msg);
            return;
        }

        var homeScreen = FXML.load(HomeScreenCtrl.class,
                "client", "scenes", "HomeScreen.fxml");
        var recipeOverview = FXML.load(RecipeOverviewCtrl.class,
                "client", "scenes", "RecipeOverview.fxml");
        var ingredientOverview = FXML.load(IngredientTypeOverviewCtrl.class,
                "client", "scenes", "IngredientOverview.fxml");
        var shoppingList = FXML.load(ShoppingListCtrl.class,
                "client", "scenes", "ShoppingList.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, homeScreen, recipeOverview, ingredientOverview, shoppingList);
    }
}