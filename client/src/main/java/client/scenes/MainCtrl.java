package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private Stage shoppingListStage;
    private Scene shoppingList;

    private HomeCtrl homeCtrl;
    private Scene home;
    private ShoppingListCtrl shoppingListCtrl;

    public void initialize(Stage primaryStage, Pair<HomeCtrl, Parent> home) {
        this.primaryStage = primaryStage;

        this.homeCtrl = home.getKey();
        this.home = new Scene(home.getValue());

        showHome();
        primaryStage.show();
    }

    public void showHome() {
        primaryStage.setTitle("FoodPal");
        primaryStage.setScene(home);
    }

    public void initializeShoppingList(Stage shoppingListStage, Pair<ShoppingListCtrl, Parent> shoppingList) {
        this.shoppingListStage = shoppingListStage;

        this.shoppingListCtrl = shoppingList.getKey();
        this.shoppingList =  new Scene(shoppingList.getValue());

        showShoppingList();
        shoppingListStage.show();
    }

    public void showShoppingList() {
        shoppingListStage.setTitle("Shopping List");
        shoppingListStage.setScene(shoppingList);
    }
}