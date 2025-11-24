package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Person;
import commons.Quote;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    // Root
    @FXML
    private AnchorPane rootPane;

    // Top bar
    @FXML
    private Button refreshButton;

    // Sidebar
    @FXML
    private TextField recipeSearchField;

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private Button addRecipeButton;

    @FXML
    private Button removeRecipeButton;

    // Title row
    @FXML
    private Label recipeTitleLabel;

    @FXML
    private Button editRecipeTitleButton;

    // Ingredients
    @FXML
    private Label ingredientsHeaderLabel;

    @FXML
    private ListView<String> ingredientListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button editIngredientButton;

    // Preparation
    @FXML
    private Label preparationHeaderLabel;

    @FXML
    private ListView<String> preparationStepListView;

    @FXML
    private Button removeStepButton;

    @FXML
    private Button addStepButton;

    @FXML
    private Button editStepButton;


    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    private void initialize() {
        // Dummy content
        recipeListView.getItems().setAll(
                "Tomato Sauce",
                "Pizza Dough",
                "Sebas' Stew",
                "Potato Salad",
                "Soup"
        );

        ingredientListView.getItems().setAll(
                "100g Sugar",
                "A pinch of Salt"
        );

        preparationStepListView.getItems().setAll(
                "Do this and that",
                "Heat up"
        );

        recipeTitleLabel.setText("[Dummy recipe selected]");
    }

    @FXML
    private void onRefresh() {
        // MISSING
    }

    @FXML
    private void onAddRecipe() {}

    @FXML
    private void onRemoveRecipe() {}

    // MAYBE KEEP SOMETHING LIKE THIS FROM THE PROJECT TEMPLATE:
//    public void keyPressed(KeyEvent e) {
//        switch (e.getCode()) {
//            case ENTER:
//                ok();
//                break;
//            case ESCAPE:
//                cancel();
//                break;
//            default:
//                break;
//        }
//    }
}