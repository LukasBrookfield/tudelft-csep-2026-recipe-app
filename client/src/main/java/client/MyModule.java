package client;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import client.scenes.HomeCtrl;
import client.scenes.MainCtrl;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(HomeCtrl.class).in(Scopes.SINGLETON);
    }
}