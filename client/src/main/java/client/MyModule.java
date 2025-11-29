package client;

import client.utils.ServerUtils;
import client.utils.UserConfig;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import client.scenes.HomeCtrl;
import client.scenes.MainCtrl;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(HomeCtrl.class).in(Scopes.SINGLETON);

        binder.bind(Client.class).toInstance(ClientBuilder.newClient(new ClientConfig()));
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(UserConfig.class).in(Scopes.SINGLETON);
    }
}