package by.deniotokiari.irc.client.irc.client.irc;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import by.deniotokiari.irc.client.irc.client.model.Server;
import by.deniotokiari.irc.client.irc.client.model.ServerChannels;
import by.istin.android.xcore.ContextHolder;

public class IrcClient extends Thread {

    private ContentValues mContentValues;

    private EventListener mEventListener;

    private String mHost;
    private int mPort;

    private Socket mSocket;

    private PrintStream mOut;
    private BufferedReader mIn;

    private AtomicBoolean mIsRunning;

    String serverId;
    String nick;
    String userName;
    String realName;
    String channels;

    public IrcClient(ContentValues values, EventListener eventListener) {
        mContentValues = values;

        serverId = mContentValues.getAsString(Server.ID);
        nick = mContentValues.getAsString(Server.NICK_NAME);
        userName = mContentValues.getAsString(Server.USER_NAME);
        realName = mContentValues.getAsString(Server.REAL_NAME);
        channels = "";

        mHost = mContentValues.getAsString(Server.HOST);
        //noinspection ConstantConditions
        mPort = mContentValues.getAsInteger(Server.PORT);

        mEventListener = eventListener;

        mIsRunning = new AtomicBoolean(true);
    }

    public long getServerId() {
        return Long.parseLong(serverId);
    }

    public String getNick() {
        return nick;
    }

    public String getHost() {
        return mHost;
    }

    @Override
    public void run() {
        super.run();

        connect();

        String msg;

        try {
            while (mIsRunning.get() && (msg = mIn.readLine()) != null) {
                // send PONG if get PING from server
                if (msg.contains(Command.PING.name())) {
                    send(Command.PONG + " " + msg.split(":")[1]);
                } else if (msg.contains("001 " + nick + " :")) {
                    send(Command.JOIN + " " + channels);
                } else if (msg.contains("ERROR :Closing Link:")) {
                    //disconnect();
                    break;
                }

                sendEvent(EventListener.EVENT.RECEIVE, msg);
            }
        } catch (IOException e) {
            sendEvent(EventListener.EVENT.ERROR, new Exception("error"));
        } finally {
            disconnect();
        }
    }

    public ContentValues getContentValues() {
        return mContentValues;
    }

    public synchronized void connect() {
        try {
            channels = ServerChannels.getJoinedServerChannels(ContextHolder.getInstance().getContext(), Long.parseLong(serverId));

            mSocket = new Socket(mHost, mPort);

            mOut = new PrintStream(mSocket.getOutputStream());
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

            sendEvent(EventListener.EVENT.CONNECTED, mHost, mPort);

            send(Command.USER + " " + userName + " host servname : " + realName);
            send(Command.NICK + " " + nick);
        } catch (Exception e) {
            sendEvent(EventListener.EVENT.ERROR, new Exception("error"));//TODO: imp
        }
    }

    public synchronized void disconnect() {
        try {
            mIsRunning.set(false);

            mOut.close();
            mIn.close();

            mSocket.close();

            sendEvent(EventListener.EVENT.DISCONNECTED, mHost, mPort);

            interrupt();
        } catch (Exception e) {
            sendEvent(EventListener.EVENT.ERROR, new Exception("error"));//TODO: imp
        }
    }

    public synchronized void send(String text) {
        try {
            mOut.println(text);

            sendEvent(EventListener.EVENT.SEND, text);
        } catch (Exception e) {
            sendEvent(EventListener.EVENT.ERROR, new Exception("error"));//TODO: imp
        }
    }

    private synchronized void sendEvent(EventListener.EVENT event, Object... args) {
        if (mEventListener != null) {
            switch (event) {
                case CONNECTED:
                    mEventListener.onConnected(this, (String) args[0], (Integer) args[1]);
                    break;
                case DISCONNECTED:
                    mEventListener.onDisconnected(this, (String) args[0], (Integer) args[1]);
                    break;
                case ERROR:
                    try {
                        mSocket.close();
                        mIn.close();
                        mOut.close();
                    } catch (Exception e) {
                        mEventListener.onError(this, e);
                    }

                    mEventListener.onError(this, (Exception) args[0]);
                    break;
                case SEND:
                    mEventListener.onSend(this, (String) args[0]);
                    break;
                case RECEIVE:
                    mEventListener.onReceive(this, (String) args[0]);
                    break;
            }
        }
    }

    public static interface EventListener {

        public static enum EVENT {

            CONNECTED, DISCONNECTED, ERROR, SEND, RECEIVE

        }

        public void onConnected(IrcClient ircClient, String host, int port);

        public void onDisconnected(IrcClient ircClient, String host, int port);

        public void onError(IrcClient ircClient, Exception e);

        public void onSend(IrcClient ircClient, String text);

        public void onReceive(IrcClient ircClient, String text);

    }

}
