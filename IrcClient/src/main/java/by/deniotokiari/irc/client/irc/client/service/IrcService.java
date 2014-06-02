package by.deniotokiari.irc.client.irc.client.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.helper.ParserHelper;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.utils.ContentUtils;

public class IrcService extends Service implements IrcClient.EventListener {

    public static final String KEY_SERVER_ID = "key:server_id";
    public static final int NOTIFICATION_ID = 42248;

    private Binder mBinder = new IrcServiceBinder();

    @SuppressLint("UseSparseArrays")
    private Map<Long, IrcClient> mIrcClients = Collections.synchronizedMap(new HashMap<Long, IrcClient>());

    private void handleCommand(Intent intent) {
        long id = intent.getLongExtra(KEY_SERVER_ID, 0L);
        ContentValues values = ContentUtils.getEntity(this, Server.class, id);

        IrcClient ircClient = new IrcClient(values, this);
        ircClient.start();

        mIrcClients.put(id, ircClient);

        if (mIrcClients.size() == 1) {
            showNotification();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mIrcClients != null && !mIrcClients.isEmpty()) {
            for (Long key : mIrcClients.keySet()) {
                removeIrcClient(mIrcClients.get(key));
            }
        }
    }

    public void showNotification() {
        String title = getString(R.string.app_name);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
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
            //stopSelf();
            stopForeground(true);
        }
    }

    public boolean removeIrcClient(IrcClient ircClient) {
        boolean result = mIrcClients.remove(ircClient.getServerId()) != null;
        if (result) {
            Server.setIsConnected(this, ircClient.getServerId(), false);
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
        return mBinder;
    }

    @Override
    public void onConnected(IrcClient ircClient, String host, int port) {
        Server.setIsConnected(this, ircClient.getServerId(), true);
    }

    @Override
    public void onDisconnected(IrcClient ircClient, String host, int port) {
        if (removeIrcClient(ircClient)) {
            exitIfZero();
        }
    }

    @Override
    public void onError(IrcClient ircClient, Exception e) {
        /*if (removeIrcClient(ircClient)) {
            exitIfZero();
        }*/
    }

    @Override
    public void onSend(IrcClient ircClient, String text) {
        ParserHelper.processOnSend(this, ircClient, text);
    }

    @Override
    public void onReceive(IrcClient ircClient, String text) {
        ParserHelper.processOnReceive(this, ircClient, text);
    }

    public IrcClient getIrcClient(long serverId) {
        return mIrcClients.get(serverId);
    }

    public class IrcServiceBinder extends Binder {
        public IrcService getService() {
            return IrcService.this;
        }
    }

}
