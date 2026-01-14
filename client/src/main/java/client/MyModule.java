package client;

import client.scenes.IngredientTypeOverviewCtrl;
import client.scenes.ShoppingListCtrl;
import client.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        binder.bind(RecipeOverviewCtrl.class);
        binder.bind(IngredientTypeOverviewCtrl.class);
        binder.bind(LanguageService.class).in(Scopes.SINGLETON);

        binder.bind(Client.class).toInstance(ClientBuilder.newClient(new ClientConfig()));
        binder.bind(ServerUtility.class).toInstance(new ServerUtils(ClientBuilder.newClient(new ClientConfig())));
        binder.bind(UserStorage.class).toInstance(new JsonUserStorage(
                "UserConfig.json", new ObjectMapper()));
        binder.bind(UserConfig.class).in(Scopes.SINGLETON);
        binder.bind(ShoppingListCtrl.class);
        binder.bind(RecipeUtils.class).in(Scopes.SINGLETON);
        binder.bind(ShoppingListUtils.class).in(Scopes.SINGLETON);
    }
}