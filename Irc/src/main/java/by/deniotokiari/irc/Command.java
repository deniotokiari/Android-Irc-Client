package by.deniotokiari.irc;

public enum Command {

    /**
     * ADMIN [&lt;servers&gt;]
     */
    ADMIN,

    /**
     * AWAY [&lt;massage&gt;]
     */
    AWAY,

    /**
     * CNOTICE &lt;nickname&gt; &lt;channel&gt; :&lt;message&gt;
     */
    CNOTICE,

    /**
     * CPRIVMSG &lt;nickname&gt; &lt;channel&gt; :&lt;message&gt;
     */
    CPRIVMSG,

    /**
     * CONNECT &lt;target server&gt [&lt;port&gt [&lt;remote server&gt]] <b>RFC 1459</b><br/>
     * CONNECT &lt;target server&gt &lt;port&gt [&lt;remote server&gt] <b>RFC 2812</b>
     */
    CONNECT,

    /**
     * DIE
     */
    DIE,

    /**
     * :&lt;source&gt ENCAP &lt;destination&gt &lt;subcommand&gt &lt;parameters&gt
     */
    ENCAP,

    /**
     * ERROR &lt;error message&gt
     */
    ERROR,

    /**
     * HELP [&lt;target&gt]
     */
    HELP,

    /**
     * INFO [&lt;target&gt]
     */
    INFO,

    /**
     * INVITE &lt;nickname&gt &lt;channel&gt
     */
    INVITE,

    /**
     * ISON &lt;nickname&gt
     */
    ISON,

    /**
     * JOIN &lt;channel&gt [&lt;keys&gt]
     */
    JOIN,

    /**
     * KICK &lt;channel&gt &lt;client&gt [&lt;message&gt]
     */
    KICK,

    /**
     * KNOCK &lt;channel&gt [&lt;message&gt]
     */
    KNOCK,

    /**
     * LINKS [&lt;remote server&gt [&lt;server mask&gt]]
     */
    LINKS,

    /**
     * LIST [&lt;channels&gt [&lt;server&gt]]
     */
    LIST,

    /**
     * LUSERS [&lt;mask&gt [&lt;server&gt]]
     */
    LUSERS,

    /**
     * MODE &lt;nickname&gt &lt;flags&gt (user)
     * MODE &lt;channel&gt &lt;flags&gt [&lt;args&gt] (channel)
     */

    /**
     * MOTD [&lt;server&gt]
     */
    MOTD,

    /**
     * NAMES [&lt;channels&gt] &lt;b&gtRFC 1459&lt;/b&gt
     * NAMES [&lt;channels&gt [&lt;server&gt]] &lt;b&gtRC 2812&lt;/b&gt
     */
    NAMES,

    /**
     * PROTOCTL NAMESX
     */
    NAMESX,

    /**
     * NICK &lt;nickname&gt [&lt;hopcount&gt] &lt;b&gtRFC 1459&lt;/b&gt &lt;br&gt
     */
    NICK,


    /**
     * NOTICE &lt;msgtarget&gt &lt;message&gt
     */
    NOTICE,

    /**
     * OPER &lt;username&gt &lt;password&gt
     */
    OPER,

    /**
     * PART &lt;channels&gt [&lt;message&gt]
     */
    PART,

    /**
     * PASS &lt;password&gt
     */
    PASS,

    /**
     * PING &lt;server1&gt [&lt;server2&gt]
     */
    PING,

    /**
     * PONG &lt;server1&gt [&lt;server2&gt]
     */
    PONG,

    /**
     * PRIVMSG &lt;msgtarget&gt &lt;message&gt
     */
    PRIVMSG,

    /**
     * QUIT [&lt;message&gt]
     */
    QUIT,

    /**
     * REHASH
     */
    REHASH,

    /**
     * RESTART
     */
    RESTART,

    /**
     * RULES
     */
    RULES,

    /**
     * SERVER &lt;servername&gt &lt;hopcount&gt &lt;info&gt
     */
    SERVER,

    /**
     * SERVICE &lt;nickname&gt &lt;reserved&gt &lt;distribution&gt &lt;type&gt &lt;reserved&gt &lt;info&gt
     */
    SERVICE,

    /**
     * SERVLIST [&lt;mask&gt [&lt;type&gt]]
     */
    SERVLIST,

    /**
     * SQUERY &lt;servicename&gt &lt;text&gt
     */
    SQUERY,

    /**
     * SQUIT &lt;server&gt &lt;comment&gt
     */
    SQUIT,

    /**
     * SETNAME &lt;new real name&gt
     */
    SETNAME,

    /**
     * SILENCE [+/-&lt;hostmask&gt]
     */
    SILENCE,

    /**
     * STATS &lt;query&gt [&lt;server&gt]
     */
    STATS,

    /**
     * SUMMON &lt;user&gt [&lt;server&gt] &lt;b&gt&lt;/b&gtRFC 1459&lt;/b&gt
     * SUMMON &lt;user&gt [&lt;server&gt [&lt;channel&gt]] &lt;b&gtRFC 2812&lt;/b&gt
     */
    SUMMON,

    /**
     * TIME [&lt;server&gt]
     */
    TIME,

    /**
     * TOPIC &lt;channel&gt [&lt;topic&gt]
     */
    TOPIC,

    /**
     * TRACE [&lt;target&gt]
     */
    TRACE,

    /**
     * PROTOCTL UHNAMES
     */
    UHNAMES,

    /**
     * USER &lt;username&gt &lt;hostname&gt &lt;servername&gt &lt;realname&gt &lt;b&gtRFC 1459&lt;/b&gt
     * USER &lt;user&gt &lt;mode&gt &lt;unused&gt &lt;realname&gt &lt;b&gtRFC 2812&lt;/b&gt
     */
    USER,

    /**
     * USERHOST &lt;nickname&gt [&lt;nickname&gt &lt;nickname&gt ...]
     */
    USERHOST,

    /**
     * USERIP &lt;nickname&gt
     */
    USERIP,

    /**
     * USERS [&lt;server&gt]
     */
    USERS,

    /**
     * VERSION [&lt;server&gt]
     */
    VERSION,

    /**
     * WALLOPS &lt;message&gt
     */
    WALLOPS,

    /**
     * WATCH [+/-&lt;nicknames&gt]
     */
    WATCH,

    /**
     * WHO [&lt;name&gt ["o"]]
     */
    WHO,

    /**
     * WHOIS [&lt;server&gt] &lt;nicknames&gt
     */
    WHOIS,

    /**
     * WHOWAS &lt;nickname&gt [&lt;count&gt [&lt;server&gt]]
     */
    WHOWAS

}
