package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeScreenCtrl {
    private final MainCtrl mainCtrl;

    @FXML
    private Button recipeOverviewButton;

    @FXML
    private Button ingredientOverviewButton;

    @FXML
    private Button shoppingListButton;

    @Inject
    public HomeScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private void onRecipeOverviewButton() {
        mainCtrl.showRecipeOverview();
    }

    @FXML
    private void onIngredientOverviewButton() {
        mainCtrl.showIngredientTypeOverview();
    }

    @FXML
    private void onShoppingListButton() {
    }
}
