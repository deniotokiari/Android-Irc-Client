package by.deniotokiari.java.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrcClient extends Thread {

    private EventListener mEventListener;

    private String mHost;
    private int mPort;

    private Socket mSocket;

    private PrintStream mOut;
    private BufferedReader mIn;

    private AtomicBoolean mIsRunning;

    public IrcClient(String host, int port, EventListener eventListener) {
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
            e.printStackTrace();
        }
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
            interrupt();

            mIsRunning.set(false);

            mSocket.close();

            mOut.close();
            mIn.close();

            sendEvent(EventListener.EVENT.DISCONNECTED, mHost, mPort);
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
        if (mEventListener != null && mIsRunning.get()) {
            switch (event) {
                case CONNECTED:
                    mEventListener.onConnected((String) args[0], (Integer) args[1]);
                    break;
                case DISCONNECTED:
                    mEventListener.onDisconnected((String) args[0], (Integer) args[1]);
                    break;
                case ERROR:
                    try {
                        mSocket.close();
                        mIn.close();
                        mOut.close();
                    } catch (Exception e) {
                        mEventListener.onError(e);
                    }

                    mEventListener.onError((Exception) args[0]);
                    break;
                case SEND:
                    mEventListener.onSend((String) args[0]);
                    break;
                case RECEIVE:
                    mEventListener.onReceive((String) args[0]);
                    break;
            }
        }
    }

    public static interface EventListener {

        public static enum EVENT {

            CONNECTED, DISCONNECTED, ERROR, SEND, RECEIVE

        }

        public void onConnected(String host, int port);

        public void onDisconnected(String host, int port);

        public void onError(Exception e);

        public void onSend(String text);

        public void onReceive(String text);

    }

}
