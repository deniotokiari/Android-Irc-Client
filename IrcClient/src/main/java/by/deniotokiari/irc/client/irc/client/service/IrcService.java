package by.deniotokiari.irc.client.irc.client.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.irc.Command;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.utils.ContentUtils;

public class IrcService extends Service implements IrcClient.EventListener {

    public static final String KEY_SERVER_ID = "key:server_id";
    public static final int NOTIFICATION_ID = 42248;

    @SuppressLint("UseSparseArrays")
    private Map<Integer, IrcClient> mIrcClients = Collections.synchronizedMap(new HashMap<Integer, IrcClient>());

    private void handleCommand(Intent intent) {
        long id = intent.getLongExtra(KEY_SERVER_ID, 0L);
        ContentValues values = ContentUtils.getEntity(this, Server.class, id);

        IrcClient ircClient = new IrcClient(values, this);
        ircClient.start();

        mIrcClients.put(ircClient.hashCode(), ircClient);

        if (mIrcClients.size() == 1) {
            showNotification();
        }
    }

    public void showNotification() {
        String title = getString(R.string.app_name);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setTicker(title)
                .setContentText(title)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setOngoing(true);
        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

    public void exitIfZero() {
        if (mIrcClients.size() == 0) {
            stopSelf();
        }
    }

    public boolean removeIrcClient(IrcClient ircClient) {
        boolean result = mIrcClients.remove(ircClient.hashCode()) != null;
        if (result) {
            Server.setOnline(this, ircClient.getContentValues(), false);
        }
        return result;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(IrcClient ircClient, String host, int port) {
        Server.setOnline(this, ircClient.getContentValues(), true);
    }

    @Override
    public void onDisconnected(IrcClient ircClient, String host, int port) {
        if (removeIrcClient(ircClient)) {
            exitIfZero();
        }
    }

    @Override
    public void onError(IrcClient ircClient, Exception e) {
        if (removeIrcClient(ircClient)) {
            exitIfZero();
        }
    }

    @Override
    public void onSend(IrcClient ircClient, String text) {

    }

    @Override
    public void onReceive(IrcClient ircClient, String text) {
        Log.d("LOG", "onReceive: " + text);

        if (text.contains("!join")) {
            ircClient.send(Command.JOIN + " #wwips");
        } else if (text.contains("!q")) {
            ircClient.disconnect();
        }
    }

}
