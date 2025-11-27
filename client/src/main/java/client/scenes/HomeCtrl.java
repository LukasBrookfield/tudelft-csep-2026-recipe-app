package client.scenes;

import client.utils.UserConfig;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

public class HomeCtrl {

    private final ServerUtils server;

    private UserConfig user;

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
    private ListView<Recipe> recipeListView;

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
    private ListView<Ingredient> ingredientListView;

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
    public HomeCtrl(ServerUtils server, UserConfig user, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.user = user;
        user.getShoppingList().add(new Ingredient("Potato", 100.0, Unit.G));
        user.saveUser();
        System.out.println(user.getShoppingList());
        System.out.println(user.getFavouriteRecipes());
    }

    @FXML
    private void initialize() {
        // Dummy content
        Ingredient tomato = new Ingredient("tomato", 400.0, Unit.G);
        Ingredient oliveOil = new Ingredient("olive oil", 30.0, Unit.ML);
        Ingredient onion = new Ingredient("onion", 100.0, Unit.G);
        List<Ingredient> tomatoSauceIngredients = new ArrayList<>(List.of(tomato, oliveOil, onion));
        Recipe tomatoSauce = new Recipe("Tomato Sauce", tomatoSauceIngredients,
                List.of("Do this and that","Heat up"));

        recipeListView.getItems().setAll(tomatoSauce);

        ingredientListView.getItems().setAll(tomato, oliveOil, onion);

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