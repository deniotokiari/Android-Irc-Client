package by.deniotokiari.irc.client.irc.client.helper;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

public class ActionBarHelper {

    public static void setText(ActionBarActivity activity, String text) {
        activity.getSupportActionBar().setTitle(text);
    }

    public static void setChannelTitle(Activity activity, String nick, String server, String channelName, String topic) {
        setText((ActionBarActivity) activity, nick + " @ " + server + " / " + channelName + " " + topic);
    }

}
