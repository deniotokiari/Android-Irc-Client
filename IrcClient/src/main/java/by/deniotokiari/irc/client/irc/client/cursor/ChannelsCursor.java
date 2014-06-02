package by.deniotokiari.irc.client.irc.client.cursor;

import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.deniotokiari.irc.client.irc.client.model.ServerChannels;
import by.istin.android.xcore.db.impl.DBHelper;

public class ChannelsCursor {

    public static final String SQL = new StringBuilder()
            .append("SELECT ")

            .append("c." + Channel.NAME + " AS " + Channel.NAME + ",")
            .append("sc." + ServerChannels.SERVER_ID + " AS " + ServerChannels.SERVER_ID + ",")
            .append("sc." + ServerChannels.CHANNEL_ID + " AS " + ServerChannels.CHANNEL_ID + ",")
            .append("s." + Server.HOST + " AS " + Server.HOST + ",")
            .append("c." + Channel.ID)

            .append(" FROM " + DBHelper.getTableName(ServerChannels.class) + " AS sc")
            .append(" LEFT JOIN " + DBHelper.getTableName(Channel.class) + " AS c")
            .append(" ON sc." + ServerChannels.CHANNEL_ID + " = c." + Channel.ID)
            .append(" LEFT JOIN " + DBHelper.getTableName(Server.class) + " AS s")
            .append(" ON s." + Server.ID + " = sc." + ServerChannels.SERVER_ID)

            .append(" WHERE sc." + ServerChannels.IS_JOIN + " = 1")
            .append(" AND s." + Server.IS_CONNECTED + " = 1")

            .append(" ORDER BY s." + Server.ID + ", c." + Channel.NAME)

            .toString();

}
