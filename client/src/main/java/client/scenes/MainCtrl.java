package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private RecipeOverviewCtrl recipeOverviewCtrl;
    private Scene recipeOverviewScene;

    private IngredientTypeOverviewCtrl ingredientTypeOverviewCtrl;
    private Scene ingredientTypeOverviewScene;

    private HomeScreenCtrl homeScreenCtrl;
    private Scene homeScreenScene;

    public void initialize(Stage primaryStage,
                           Pair<RecipeOverviewCtrl, Parent> recipeOverview,
                           Pair<IngredientTypeOverviewCtrl, Parent> ingredientOverview,
                           Pair<HomeScreenCtrl, Parent> homeScreen) {
        this.primaryStage = primaryStage;

        this.recipeOverviewCtrl = recipeOverview.getKey();
        this.recipeOverviewScene = new Scene(recipeOverview.getValue());

        this.ingredientTypeOverviewCtrl = ingredientOverview.getKey();
        this.ingredientTypeOverviewScene = new Scene(ingredientOverview.getValue());

        this.homeScreenCtrl = homeScreen.getKey();
        this.homeScreenScene = new Scene(homeScreen.getValue());

        showHomeScreen();
        primaryStage.show();
    }

    public void showHomeScreen() {
        primaryStage.setTitle("FoodPal - Home Screen");
        primaryStage.setScene(homeScreenScene);
    }

    public void showRecipeOverview() {
        primaryStage.setTitle("FoodPal - Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
        recipeOverviewCtrl.onRefresh();
    }

    public void showIngredientTypeOverview() {
        primaryStage.setTitle("FoodPal - Ingredient Overview");
        primaryStage.setScene(ingredientTypeOverviewScene);
        ingredientTypeOverviewCtrl.onRefresh();
    }
}