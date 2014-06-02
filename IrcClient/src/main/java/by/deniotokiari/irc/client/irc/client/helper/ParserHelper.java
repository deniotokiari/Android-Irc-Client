package by.deniotokiari.irc.client.irc.client.helper;

import android.content.Context;
import android.util.Log;

import by.deniotokiari.irc.client.irc.client.irc.Command;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;

public class ParserHelper {

    public static void processOnSend(Context context, IrcClient ircClient, String text) {
        Log.d("LOG", "onSend: " + text);

        switch (recognize(ircClient, text)) {
            case PRIVMSG:
                OnSendHelper.processPRIVMSG(context, ircClient, text);
                break;
        }
    }

    public static void processOnReceive(Context context, IrcClient ircClient, String text) {
        Log.d("LOG", "onReceive: " + text);

        switch (recognize(ircClient, text)) {
            case PRIVMSG:
                OnReceiveHelper.processPRIVMSG(context, ircClient, text);
                break;
            case JOIN:
                OnReceiveHelper.processJOIN(context, ircClient, text);
                break;
            case PART:
                OnReceiveHelper.processPART(context, ircClient, text);
                break;
            case NAMES:
                OnReceiveHelper.processNAMES(context, ircClient, text);
                break;
            case NICK:
                OnReceiveHelper.processNICK(context, ircClient, text);
                break;
        }
    }

    private static Command recognize(IrcClient ircClient, String text) {
        if (text.contains(Command.PRIVMSG.name() + " ")) {
            return Command.PRIVMSG;
        } else if (text.contains(Command.JOIN.name())) {
            return Command.JOIN;
        } else if (text.contains(Command.PART.name())) {
            return Command.PART;
        } else if (text.contains(" 353 ")) {
            return Command.NAMES;
        } else if (text.contains(Command.NICK + " :")) {
            return Command.NICK;
        }

        return Command.NONE;
    }

}
