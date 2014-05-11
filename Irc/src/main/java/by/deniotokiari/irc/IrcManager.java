package by.deniotokiari.irc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IrcManager {

    private Map<String, IrcClient> mStorage;

    public IrcManager() {
        mStorage = new ConcurrentHashMap<String, IrcClient>();
    }

}
