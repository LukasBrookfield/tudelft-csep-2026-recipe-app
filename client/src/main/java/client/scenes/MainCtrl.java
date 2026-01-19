package client.scenes;

import client.utils.LanguageService;
import com.google.inject.Inject;
import commons.Recipe;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.image.Image;
import org.hibernate.service.spi.InjectService;

import java.awt.*;
import java.util.Objects;
import java.util.ResourceBundle;

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

    private final LanguageService languages;

    @Inject
    public MainCtrl(LanguageService languages) {
        this.languages = languages;
    }

    public MainCtrl() {
        this(LanguageService.defaultService());
    }

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

    /**
     * Switches scenes based on the selection of the scene drop down
     * @param scene 0 = home, 1 = recipe overview, 2 = ingredient overview, 3 = shopping list
     */
    public void showScene(int scene) {
        switch (scene) {
            case 0:
                showHomeScreen();
                break;
            case 1:
                recipeOverviewCtrl.sceneBox.getSelectionModel().select(1);
                showRecipeOverview();
                break;
            case 2:
                ingredientTypeOverviewCtrl.sceneBox.getSelectionModel().select(2);
                showIngredientTypeOverview();
                break;
            case 3:
                shoppingListCtrl.sceneBox.getSelectionModel().select(3);
                showShoppingList();
        }
    }

    public void showHomeScreen() {
        primaryStage.setTitle(languages.bundle().getString("title.home"));
//        primaryStage.setTitle("FoodPal - Home");
        primaryStage.setScene(homeScreenScene);
    }

    public void showRecipeOverview() {
        primaryStage.setTitle(languages.bundle().getString("title.recipes"));
//        primaryStage.setTitle("FoodPal - Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
        recipeOverviewCtrl.onRefresh();
    }

    public void showIngredientTypeOverview() {
        primaryStage.setTitle(languages.bundle().getString("title.ingredients"));
//        primaryStage.setTitle("FoodPal - Ingredient Overview");
        primaryStage.setScene(ingredientTypeOverviewScene);
        ingredientTypeOverviewCtrl.onRefresh();
    }

    public void showShoppingList() {
        primaryStage.setTitle(languages.bundle().getString("title.shopping"));
//        primaryStage.setTitle("FoodPal - Shopping List");
        primaryStage.setScene(shoppingListScene);

        shoppingListCtrl.set();
    }

    public void showAddToShoppingList(Recipe recipe, double scaleFactor) {
        primaryStage.setTitle(languages.bundle().getString("title.addToShopping"));
//        primaryStage.setTitle("FoodPal - Add to Shopping List");
        primaryStage.setScene(addToShoppingListScene);
        addToShoppingListCtrl.setFields(recipe, scaleFactor);
    }

    public void applyTranslationsToAllScreens() {

        if (homeScreenCtrl != null) homeScreenCtrl.applyTranslations();
        if (recipeOverviewCtrl != null) recipeOverviewCtrl.applyTranslations();
        if (ingredientTypeOverviewCtrl != null) ingredientTypeOverviewCtrl.applyTranslations();
        if (shoppingListCtrl != null) shoppingListCtrl.applyTranslations();
        if (addToShoppingListCtrl != null) addToShoppingListCtrl.applyTranslations();

        updateStageTitleForCurrentScene();
    }

    private void updateStageTitleForCurrentScene() {
        if (primaryStage == null) return;
        ResourceBundle b = languages.bundle();
        Scene s = primaryStage.getScene();
        if (s == homeScreenScene) primaryStage.setTitle(b.getString("title.home"));
        else if (s == recipeOverviewScene) primaryStage.setTitle(b.getString("title.recipes"));
        else if (s == ingredientTypeOverviewScene) primaryStage.setTitle(b.getString("title.ingredients"));
        else if (s == shoppingListScene) primaryStage.setTitle(b.getString("title.shopping"));
        else if (s == addToShoppingListScene) primaryStage.setTitle(b.getString("title.addToShopping"));
    }

}