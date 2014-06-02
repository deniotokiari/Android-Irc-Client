package by.deniotokiari.irc.client.irc.client.provider;

import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.ChannelMessages;
import by.deniotokiari.irc.client.irc.client.model.ChannelUsers;
import by.deniotokiari.irc.client.irc.client.model.Message;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.deniotokiari.irc.client.irc.client.model.ServerChannels;
import by.deniotokiari.irc.client.irc.client.model.User;
import by.istin.android.xcore.provider.DBContentProvider;

public class AppProvider extends DBContentProvider {

    public static final Class<?>[] ENTITIES = {
            Server.class,
            Channel.class,
            Message.class,
            User.class,
            ServerChannels.class,
            ChannelMessages.class,
            ChannelUsers.class,
    };

    @Override
    public Class<?>[] getEntities() {
        return ENTITIES;
    }

}
