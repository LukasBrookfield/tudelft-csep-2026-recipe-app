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

    public void initialize(Stage primaryStage,
                           Pair<RecipeOverviewCtrl, Parent> recipeOverview,
                           Pair<IngredientTypeOverviewCtrl, Parent> ingredientOverview) {
        this.primaryStage = primaryStage;

        this.recipeOverviewCtrl = recipeOverview.getKey();
        this.recipeOverviewScene = new Scene(recipeOverview.getValue());

        this.ingredientTypeOverviewCtrl = ingredientOverview.getKey();
        this.ingredientTypeOverviewScene = new Scene(ingredientOverview.getValue());

        showRecipeOverview();
        primaryStage.show();
    }

    public void showRecipeOverview() {
        primaryStage.setTitle("FoodPal - Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
    }

    public void showIngredientTypeOverview() {
        primaryStage.setTitle("FoodPal - Ingredient Overview");
        primaryStage.setScene(ingredientTypeOverviewScene);
    }
}