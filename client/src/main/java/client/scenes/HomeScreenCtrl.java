package client.scenes;

import client.utils.LanguageService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ResourceBundle;

public class HomeScreenCtrl {
    private final MainCtrl mainCtrl;
    private final LanguageService languages;

    @FXML
    private Button recipeOverviewButton;

    @FXML
    private Button ingredientOverviewButton;

    @FXML
    private Button shoppingListButton;

    @Inject
    public HomeScreenCtrl(MainCtrl mainCtrl, LanguageService languages) {
        this.mainCtrl = mainCtrl;
        this.languages = languages;
    }

    @FXML
    private void onRecipeOverviewButton() {
        mainCtrl.showScene("Recipe overview");
    }

    @FXML
    private void onIngredientOverviewButton() {
        mainCtrl.showScene("Ingredient overview");
    }

    @FXML
    private void onShoppingListButton() {
        mainCtrl.showScene("Shopping list");
    }

    public void applyTranslations() {
        ResourceBundle bundle = languages.bundle();
        recipeOverviewButton.setText(bundle.getString("home.btn.recipeOverview"));
        ingredientOverviewButton.setText(bundle.getString("home.btn.ingredientOverview"));
        shoppingListButton.setText(bundle.getString("home.btn.shoppingList"));
    }

}
