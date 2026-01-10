package client;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import client.utils.LanguageService;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javafx.util.Pair;

@Singleton
public class MyFXML {

    private final Injector injector;
    private final LanguageService languages;

    @Inject
    public MyFXML(Injector injector, LanguageService languages) {
        this.injector = injector;
        this.languages = languages;
    }

    public <T> Pair<T, Parent> load(Class<T> c, String... parts) {
        try {
            var loader = new FXMLLoader(getLocation(parts), languages.bundle(), null, new MyFactory(), StandardCharsets.UTF_8);
            Parent parent = loader.load();
            T ctrl = loader.getController();
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getLocation(String... parts) {
//        var path = Path.of("", parts).toString();
//        return MyFXML.class.getClassLoader().getResource(path);
        var path = String.join("/", parts);
        var url = MyFXML.class.getClassLoader().getResource(path);
        System.out.println("FXML path = " + path);
        if (url == null) {
            throw new RuntimeException("Could not find resource: " + path);
        }
        return url;
    }

    private class MyFactory implements BuilderFactory, Callback<Class<?>, Object> {

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<?> getBuilder(Class<?> type) {
            return new Builder() {
                @Override
                public Object build() {
                    return injector.getInstance(type);
                }
            };
        }

        @Override
        public Object call(Class<?> type) {
            return injector.getInstance(type);
        }
    }
}