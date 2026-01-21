package client.scenes;

import client.utils.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import commons.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

public class TestModule implements Module {

    @Override
    public void configure(Binder binder) {
        // javafx scenes
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(RecipeOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(IngredientTypeOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ShoppingListCtrl.class);

        // other
        binder.bind(Client.class).toInstance(ClientBuilder.newClient(new ClientConfig()));
        binder.bind(ServerUtility.class).toInstance(new TestServerUtils());
        binder.bind(UserStorage.class).toInstance(new TestUserStorage(new User()));
        binder.bind(UserConfig.class).in(Scopes.SINGLETON);
        binder.bind(RecipeUtils.class).in(Scopes.SINGLETON);
        binder.bind(LanguageService.class).in(Scopes.SINGLETON);
        binder.bind(ShoppingListUtils.class).in(Scopes.SINGLETON);

    }
}
