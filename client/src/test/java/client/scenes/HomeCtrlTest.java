package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.google.inject.Guice.createInjector;

import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;

import com.google.inject.Injector;

import client.MyFXML;
import client.MyModule;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class HomeCtrlTest extends ApplicationTest{

    private RecipeOverviewCtrl sut;

    @Override
    public void start(Stage stage) throws Exception {

        Injector injector = createInjector(new MyModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<RecipeOverviewCtrl, Parent> loaded = fxml.load(
                RecipeOverviewCtrl.class,
                "client", "scenes", "RecipeOverview.fxml"
        );

        sut = loaded.getKey();

        stage.setScene(new Scene(loaded.getValue()));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setupTest() {}

}
