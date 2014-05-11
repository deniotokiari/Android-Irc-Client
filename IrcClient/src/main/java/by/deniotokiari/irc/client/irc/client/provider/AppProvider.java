package by.deniotokiari.irc.client.irc.client.provider;

import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.provider.DBContentProvider;

public class AppProvider extends DBContentProvider {

    public static final Class<?>[] ENTITIES = {
            Server.class,
            Channel.class,
    };

    @Override
    public Class<?>[] getEntities() {
        return ENTITIES;
    }

}
