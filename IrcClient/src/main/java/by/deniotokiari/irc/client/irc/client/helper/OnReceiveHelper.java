package by.deniotokiari.irc.client.irc.client.helper;

import android.content.Context;
import android.util.Pair;

import by.deniotokiari.irc.client.irc.client.irc.Command;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;
import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.ChannelUsers;
import by.deniotokiari.irc.client.irc.client.model.Message;
import by.deniotokiari.irc.client.irc.client.model.ServerChannels;
import by.deniotokiari.irc.client.irc.client.model.User;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.StringUtil;

public class OnReceiveHelper {

    @SuppressWarnings("unchecked")
    private static Pair getStatus(String text) {
        if (text.startsWith("~")) {
            return new Pair(0, "~");
        } else if (text.startsWith("&")) {
            return new Pair(1, "&");
        } else if (text.startsWith("@")) {
            return new Pair(2, "@");
        } else if (text.startsWith("%")) {
            return new Pair(3, "%");
        } else if (text.startsWith("+")) {
            return new Pair(4, "+");
        } else {
            return new Pair(5, "");
        }
    }

    public static void processNICK(Context context, IrcClient ircClient, String text) {
        String nick = text.split(":")[1].split("!")[0];
        long userId = User.generateId(nick, ircClient.getServerId());
        String newNick = text.split(Command.NICK.name() + " :")[1];

        User.updateUser(context, userId, newNick, ircClient.getServerId());
        context.getContentResolver().notifyChange(ModelContract.getUri(ChannelUsers.class), null);
    }

    @SuppressWarnings("unchecked")
    public static void processNAMES(Context context, IrcClient ircClient, String text) {
        String channel = text.split(" = ")[1].split(" :")[0];
        long channelId = Channel.getChannelId(context, ircClient.getServerId(), channel);

        String[] users = text.split(" :")[1].split(" ");
        Pair[] result = new Pair[users.length];

        for (int i = 0; i < users.length; i++) {
            Pair<Integer, String> status = getStatus(users[i]);
            String nick = users[i].replace(status.second, "");

            result[i] = new Pair<Pair, String>(status, nick);
        }

        ChannelUsers.addUsers(context, channelId, ircClient.getServerId(), result);
    }

    public static void processJOIN(Context context, IrcClient ircClient, String text) {
        String channel = text.split("JOIN :")[1];
        String nick = text.split(":")[1].split("!")[0];
        long channelId = Channel.getChannelId(context, ircClient.getServerId(), channel);

        ServerChannels.addChannel(context, ircClient.getServerId(), channelId, channel, true);

        if (!StringUtil.isEquals(ircClient.getNick(), nick)) {
            String body = text.split(":")[1];
            ChannelUsers.addUser(context, channelId, ircClient.getServerId(), new Pair<Pair<Integer, String>, String>(new Pair<Integer, String>(5, StringUtil.EMPTY), nick));
            Message.addMessage(context, StringUtil.EMPTY, StringUtil.EMPTY, channelId, body);
        }
    }

    public static void processPART(Context context, IrcClient ircClient, String text) {
        String channel = text.split(" PART ")[1].split(" :")[0];
        long channelId = Channel.getChannelId(context, ircClient.getServerId(), channel);

        if (channelId != 0L) {
            String nick = text.split(":")[1].split("!")[0];
            long userId = User.generateId(nick, ircClient.getServerId());

            ChannelUsers.deleteUserFromChannel(context, userId, channelId);

            String body = text.split(":")[1];

            Message.addMessage(context, StringUtil.EMPTY, StringUtil.EMPTY, channelId, body);
        }
    }

    public static void processPRIVMSG(Context context, IrcClient ircClient, String text) {
        String nick = text.split(":")[1].split("!")[0];
        String body = text.split(":")[2];

        String channel = text.split(Command.PRIVMSG + " ")[1].split(" :")[0];
        long channelId = Channel.getChannelId(context, ircClient.getServerId(), channel);

        if (channelId != 0L) {
            String status = User.getStatus(context, ircClient.getServerId(), channelId, nick);

            Message.addMessage(context, status, nick, channelId, body);
        }
    }

}
