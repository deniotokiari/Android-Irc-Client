package by.deniotokiari.java.irc;

public class Main implements IrcClient.EventListener {

    public static void main(String[] args) {
        new Main().run();
    }

    private IrcClient ircClient;

    public void run() {
        ircClient = new IrcClient("irc.rizon.net", 6667, this);
        ircClient.start();
    }

    @Override
    public void onConnected(String host, int port) {
        System.out.println("Connected: " + host + ":" + port);
        ircClient.send("USER denio host servname : denio");
        ircClient.send("NICK deniotokiari");
    }

    @Override
    public void onDisconnected(String host, int port) {
        System.out.println("Disconnected: " + host + ":" + port);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error: " + e.getMessage());
    }

    @Override
    public void onSend(String text) {
        System.out.println("Send: " + text);
    }

    @Override
    public void onReceive(String text) {
        System.out.println("Receive: " + text);
    }

}
