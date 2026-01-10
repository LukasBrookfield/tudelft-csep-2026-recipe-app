package client.scenes;

import client.MyFXML;
import client.utils.RecipeUtils;
import client.utils.ServerUtility;
import client.utils.UserConfig;
import com.google.inject.Injector;
import commons.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IngredientTypeOverviewCtrlTest {
    // ingredient overview utils
    private IngredientTypeOverviewCtrl sut;
    private ServerUtility server;
    private UserConfig user;
    private RecipeUtils recipeUtils;

    // FXML components
    private TextField ingredientTypeSearchField;
    private ListView<IngredientType> ingredientTypeListView;
    private Button removeIngredientTypeButton;
    private Button addIngredientTypeButton;
    private Button toggleOverviewButton;
    private Button homeButton;
    private Label ingredientTypeTitleLabel;
    private Button editIngredientTypeButton;
    private Button cancelEditButton;
    private Button doneEditButton;
    private Separator mainSeparator;
    private Label nameLabel;
    private Button editDetailsButton;
    private TextField editNameField;
    private Button cancelEditDetailsButton;
    private Button doneEditDetailsButton;
    private Button editDensityButton;
    private TextField editDensityField;
    private Button cancelEditDensityButton;
    private Button doneEditDensityButton;
    private Label densityLabel;
    private Label proteinLabel;
    private Label fatLabel;
    private Label carbsLabel;
    private Label kcalLabel;
    private TextField proteinTextField;
    private TextField fatTextField;
    private TextField carbsTextField;
    private Button editNutritionButton;
    private Button cancelEditNutritionButton;
    private Button doneEditNutritionButton;
    private Label usedInRecipesLabel;

    @Start
    private void start(Stage stage) {
        Injector injector = createInjector(new TestModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<IngredientTypeOverviewCtrl, Parent> loaded = fxml.load(
                IngredientTypeOverviewCtrl.class,
                "client", "scenes", "IngredientOverview.fxml"
        );

        sut = loaded.getKey();

        var scene = new Scene(loaded.getValue());
        stage.setScene(scene);
        stage.show();
        stage.toFront();

        // initialize utils using the injector
        server = injector.getInstance(ServerUtility.class);
        user = injector.getInstance(UserConfig.class);
        recipeUtils = injector.getInstance(RecipeUtils.class);

        // <-- initialize all fields using the lookup method -->
        // sidebar
        ingredientTypeSearchField = lookup(scene, "#ingredientTypeSearchField");
        ingredientTypeListView = lookup(scene, "#ingredientTypeListView");
        removeIngredientTypeButton = lookup(scene, "#removeIngredientTypeButton");
        addIngredientTypeButton = lookup(scene, "#addIngredientTypeButton");

        // navigation buttons
        toggleOverviewButton = lookup(scene, "#toggleOverviewButton");
        homeButton = lookup(scene, "#homeButton");

        // ingredient type body
        ingredientTypeTitleLabel = lookup(scene, "#ingredientTypeTitleLabel");
        mainSeparator = lookup(scene, "#mainSeparator");
        nameLabel = lookup(scene, "#nameLabel");
        densityLabel = lookup(scene, "#densityLabel");
        proteinLabel = lookup(scene, "#proteinLabel");
        fatLabel = lookup(scene, "#fatLabel");
        carbsLabel = lookup(scene, "#carbsLabel");
        kcalLabel = lookup(scene, "#kcalLabel");
        usedInRecipesLabel = lookup(scene, "#usedInRecipesLabel");

        // edit buttons
        editNameField = lookup(scene, "#editNameField");
        editDetailsButton = lookup(scene, "#editDetailsButton");
        editIngredientTypeButton = lookup(scene, "#editIngredientTypeButton");
        editDensityButton = lookup(scene, "#editDensityButton");
        editNutritionButton = lookup(scene, "#editNutritionButton");

        // done edit buttons
        doneEditButton = lookup(scene, "#doneEditButton");
        doneEditDetailsButton = lookup(scene, "#doneEditDetailsButton");
        doneEditDensityButton = lookup(scene, "#doneEditDensityButton");
        doneEditNutritionButton = lookup(scene, "#doneEditNutritionButton");

        // cancel edit buttons
        cancelEditButton = lookup(scene, "#cancelEditButton");
        cancelEditDetailsButton = lookup(scene, "#cancelEditDetailsButton");
        cancelEditDensityButton = lookup(scene, "#cancelEditDensityButton");
        cancelEditNutritionButton = lookup(scene, "#cancelEditNutritionButton");

        // text fields
        proteinTextField = lookup(scene, "#proteinTextField");
        fatTextField = lookup(scene, "#fatTextField");
        editDensityField = lookup(scene, "#editDensityField");
        carbsTextField = lookup(scene, "#carbsTextField");
    }

    private <T> T lookup(Scene scene, String id) {
        return (T) scene.lookup(id);
    }

    private void addIngredientType(FxRobot robot, String name) {
        robot.clickOn(addIngredientTypeButton);
        robot.clickOn(editDetailsButton);
        robot.write(name);
        robot.clickOn(doneEditDetailsButton);
    }

}
