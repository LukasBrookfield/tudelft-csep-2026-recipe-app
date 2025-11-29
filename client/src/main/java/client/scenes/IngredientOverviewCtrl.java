package client.scenes;

import client.utils.ServerUtils;
import client.utils.UserConfig;
import com.google.inject.Inject;

public class IngredientOverviewCtrl {

    private final ServerUtils server;

    private UserConfig user;

    private final MainCtrl mainCtrl;

    @Inject
    public IngredientOverviewCtrl(ServerUtils server, UserConfig user, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.user = user;
    }
}
