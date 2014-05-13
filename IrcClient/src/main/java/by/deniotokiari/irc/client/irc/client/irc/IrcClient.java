package by.deniotokiari.irc.client.irc.client.irc;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrcClient extends Thread {

    private ContentValues mContentValues;

    private EventListener mEventListener;

    private String mHost;
    private int mPort;

    private Socket mSocket;

    private PrintStream mOut;
    private BufferedReader mIn;

    private AtomicBoolean mIsRunning;

    public IrcClient(ContentValues values, String host, int port, EventListener eventListener) {
        mContentValues = values;

        mHost = host;
        mPort = port;

        mEventListener = eventListener;

        mIsRunning = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        super.run();

        connect();

        String msg;

        try {
            while ((msg = mIn.readLine()) != null && mIsRunning.get()) {
                // send PONG if get PING from server
                if (msg.contains(Command.PING.name())) {
                    send(Command.PONG + " " + msg.split(":")[1]);
                }

                sendEvent(EventListener.EVENT.RECEIVE, msg);
            }
        } catch (IOException e) {
            sendEvent(EventListener.EVENT.ERROR, new Exception("error"));
        }
    }

    public ContentValues getContentValues() {
        return mContentValues;
    }

    public synchronized void connect() {
        try {
            mSocket = new Socket(mHost, mPort);

            mOut = new PrintStream(mSocket.getOutputStream());
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

            sendEvent(EventListener.EVENT.CONNECTED, mHost, mPort);

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
