package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.Objects;

public class MainCtrl {

    private Stage primaryStage;

    private HomeScreenCtrl homeScreenCtrl;
    private Scene homeScreenScene;

    private RecipeOverviewCtrl recipeOverviewCtrl;
    private Scene recipeOverviewScene;

    private IngredientTypeOverviewCtrl ingredientTypeOverviewCtrl;
    private Scene ingredientTypeOverviewScene;

    private ShoppingListCtrl shoppingListCtrl;
    private Scene shoppingListScene;

    private AddToShoppingListCtrl addToShoppingListCtrl;
    private Scene addToShoppingListScene;

    public void initialize(Stage primaryStage,
                           Pair<HomeScreenCtrl, Parent> homeScreen,
                           Pair<RecipeOverviewCtrl, Parent> recipeOverview,
                           Pair<IngredientTypeOverviewCtrl, Parent> ingredientOverview,
                           Pair<ShoppingListCtrl, Parent> shoppingList,
                           Pair<AddToShoppingListCtrl, Parent> addToShoppingList) {

        this.primaryStage = primaryStage;

        this.homeScreenCtrl = homeScreen.getKey();
        this.homeScreenScene = new Scene(homeScreen.getValue());

        this.recipeOverviewCtrl = recipeOverview.getKey();
        this.recipeOverviewScene = new Scene(recipeOverview.getValue());

        this.ingredientTypeOverviewCtrl = ingredientOverview.getKey();
        this.ingredientTypeOverviewScene = new Scene(ingredientOverview.getValue());

        this.shoppingListCtrl = shoppingList.getKey();
        this.shoppingListScene = new Scene(shoppingList.getValue());

        this.addToShoppingListCtrl = addToShoppingList.getKey();
        this.addToShoppingListScene = new Scene(addToShoppingList.getValue());

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/FoodPalLogo.png"))));

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

    public void showShoppingList(boolean currentScene) {
        primaryStage.setTitle("FoodPal - Shopping List");
        primaryStage.setScene(shoppingListScene);

        shoppingListCtrl.lastScene = currentScene;
    }

    public void showAddToShoppingList() {
        primaryStage.setTitle("FoodPal - Add to Shopping List");
        primaryStage.setScene(addToShoppingListScene);
        addToShoppingListCtrl.onRefresh();
    }
}