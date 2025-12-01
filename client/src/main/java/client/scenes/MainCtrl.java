package client.scenes;

import commons.Ingredient;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private RecipeOverviewCtrl recipeOverviewCtrl;
    private Scene recipeOverviewScene;

    private IngredientOverviewCtrl ingredientOverviewCtrl;
    private Scene ingredientOverviewScene;

    public void initialize(Stage primaryStage,
                           Pair<RecipeOverviewCtrl, Parent> recipeOverview,
                           Pair<IngredientOverviewCtrl, Parent> ingredientOverview) {
        this.primaryStage = primaryStage;

        this.recipeOverviewCtrl = recipeOverview.getKey();
        this.recipeOverviewScene = new Scene(recipeOverview.getValue());

        this.ingredientOverviewCtrl = ingredientOverview.getKey();
        this.ingredientOverviewScene = new Scene(ingredientOverview.getValue());

        showRecipeOverview();
        primaryStage.show();
    }

    public void showRecipeOverview() {
        primaryStage.setTitle("FoodPal - Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
    }

    public void showIngredientOverview() {
        primaryStage.setTitle("FoodPal - Ingredient Overview");
        primaryStage.setScene(ingredientOverviewScene);
    }
}