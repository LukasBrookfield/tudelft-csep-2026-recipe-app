package client;

import client.scenes.IngredientTypeOverviewCtrl;
import client.scenes.ShoppingListCtrl;
import client.utils.RecipeUtils;
import client.utils.ServerUtils;
import client.utils.UserConfig;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import client.scenes.RecipeOverviewCtrl;
import client.scenes.MainCtrl;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(RecipeOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(IngredientTypeOverviewCtrl.class).in(Scopes.SINGLETON);

        binder.bind(Client.class).toInstance(ClientBuilder.newClient(new ClientConfig()));
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(UserConfig.class).in(Scopes.SINGLETON);
        binder.bind(ShoppingListCtrl.class);
        binder.bind(RecipeUtils.class).in(Scopes.SINGLETON);
    }
}