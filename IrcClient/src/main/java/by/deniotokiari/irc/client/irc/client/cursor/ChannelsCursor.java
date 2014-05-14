package by.deniotokiari.irc.client.irc.client.cursor;

import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.db.impl.DBHelper;

public class ChannelsCursor {

    public static final String SQL = new StringBuilder()
            .append("SELECT")

            .append(" c." + Channel._ID + " AS " + Channel._ID + ",")
            .append(" c." + Channel.TITLE + " AS " + Channel.TITLE + ",")
            .append(" c." + Channel.IS_FIRST_CHANNEL + " AS " + Channel.IS_FIRST_CHANNEL + ",")
            .append(" s." + Server.HOST + " AS " + Server.HOST)

            .append(" FROM " + DBHelper.getTableName(Channel.class) + " AS c")
            .append(" LEFT JOIN " + DBHelper.getTableName(Server.class) + " AS s")
            .append(" ON c." + Channel.SERVER_ID + " = s." + Server._ID)

            .append(" WHERE ")
            .append("s." + Server.IS_ONLINE + " = 1")

            .append(" ORDER BY s." + Server.HOST + ",")
            .append("c." + Channel.IS_FIRST_CHANNEL + " DESC,")
            .append("c." + Channel.TITLE)
            .toString();

}
