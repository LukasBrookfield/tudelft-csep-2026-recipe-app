package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.google.inject.Guice.createInjector;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import com.google.inject.Injector;

import client.MyFXML;
import client.MyModule;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Pair;

public class HomeCtrlTest extends ApplicationTest{

    private HomeCtrl sut;

    @Override
    public void start(Stage stage) throws Exception {

        Injector injector = createInjector(new MyModule());
        MyFXML fxml = new MyFXML(injector);

        Pair<HomeCtrl, Parent> loaded = fxml.load(
                HomeCtrl.class,
                "client", "scenes", "HomeUI.fxml"
        );

        sut = loaded.getKey();

        stage.setScene(new Scene(loaded.getValue()));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setupTest() {}

}
