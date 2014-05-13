package by.deniotokiari.irc.client.irc.client.service;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.irc.Command;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.utils.ContentUtils;

public class IrcService extends Service implements IrcClient.EventListener {

    public static final String KEY_SERVER_ID = "key:server_id";

    public AtomicInteger mServersCount = new AtomicInteger();

    private void handleCommand(Intent intent) {
        long id = intent.getLongExtra(KEY_SERVER_ID, 0L);
        ContentValues values = ContentUtils.getEntity(this, Server.class, id);

        IrcClient ircClient = new IrcClient(values, values.getAsString(Server.NAME), values.getAsInteger(Server.PORT), this);
        ircClient.start();

        mServersCount.incrementAndGet();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setTicker("SERVICE")
                .setContentText("SERVICE")
                .setWhen(System.currentTimeMillis())
                .setContentTitle(
                        "SERVICE")
                .setOngoing(true);
        Notification notification = builder.build();
        startForeground(124, notification);
    }

    public void exitIfZero() {
        if (mServersCount.get() <= 0) {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
        String nick = ircClient.getContentValues().getAsString(Server.NICK_NAME);
        ircClient.send(Command.USER.name() + " " + nick + " host servname : d14");
        ircClient.send(Command.NICK.name() + " " + nick);
    }

    @Override
    public void onDisconnected(IrcClient ircClient, String host, int port) {

    }

    @Override
    public void onError(IrcClient ircClient, Exception e) {

    }

    @Override
    public void onSend(IrcClient ircClient, String text) {

    }

    @Override
    public void onReceive(IrcClient ircClient, String text) {
        Log.d("LOG", "onReceive: " + text);

        if (text.contains("!join")) {
            ircClient.send(Command.JOIN.name() + " #wwips");
        } else if (text.contains("!q")) {
            ircClient.disconnect();

            mServersCount.decrementAndGet();
            exitIfZero();
        }
    }

}
