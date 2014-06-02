package by.deniotokiari.irc.client.irc.client.helper;

import android.content.Context;

import by.deniotokiari.irc.client.irc.client.irc.Command;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;
import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Message;
import by.deniotokiari.irc.client.irc.client.model.User;

public class OnSendHelper {

    public static void processPRIVMSG(Context context, IrcClient ircClient, String text) {
        String nick = ircClient.getNick();
        String body = text.split(" ")[2];

        String channel = text.split(Command.PRIVMSG + " ")[1].split(" ")[0];
        long channelId = Channel.getChannelId(context, ircClient.getServerId(), channel);

        if (channelId != 0L) {
            String status = User.getStatus(context, ircClient.getServerId(), channelId, nick);

            Message.addMessage(context, status, nick, channelId, body);
        }
    }

}
