package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.utils.ContentUtils;

public class Server implements BaseColumns {

    @dbLong
    public static final String ID = _ID;

    @dbString
    public static final String HOST = "host";

    @dbInteger
    public static final String PORT = "port";

    @dbString
    public static final String COMMANDS = "commands";

    @dbBoolean
    public static final String CONNECT_ON_START_UP = "connect_on_start_up";

    @dbString
    public static final String NICK_NAME = "nick_name";

    @dbString
    public static final String USER_NAME = "user_name";

    @dbString
    public static final String REAL_NAME = "real_name";

    @dbBoolean
    public static final String IS_CONNECTED = "is_connected";

    private static final String TEMPORARY_MESSAGES = new StringBuilder()
            .append("SELECT cm." + ChannelMessages.MESSAGE_ID + " AS " + ChannelMessages.MESSAGE_ID)

            .append(" FROM " + DBHelper.getTableName(ChannelMessages.class) + " AS cm")
            .append(" LEFT JOIN " + DBHelper.getTableName(ServerChannels.class) + " AS sc")
            .append(" ON cm." + ChannelMessages.CHANNEL_ID + " = sc." + ServerChannels.CHANNEL_ID)

            .append(" WHERE sc." + ServerChannels.IS_TEMPORARY + " = 1 AND sc." + ServerChannels.SERVER_ID + " = ?")

            .toString();

    private static final String TEMPORARY_CHANNELS = new StringBuilder()
            .append("SELECT " + ServerChannels.CHANNEL_ID)

            .append(" FROM " + DBHelper.getTableName(ServerChannels.class))
            .append(" WHERE " + ServerChannels.IS_TEMPORARY + " = 1 OR " + ServerChannels.IS_TEMPORARY + " NULL AND " + ServerChannels.SERVER_ID + " = ?")

            .toString();

    public static void setIsConnected(Context context, long serverId, boolean flag) {
        ContentValues values = new ContentValues();
        values.put(ID, serverId);
        values.put(IS_CONNECTED, flag);

        ContentUtils.putEntity(context, Server.class, values);

        if (!flag) {
            ContentUtils.removeEntities(context, Message.class, Message.ID + " IN(" + TEMPORARY_MESSAGES + ")", String.valueOf(serverId));
            ContentUtils.removeEntities(context, ChannelMessages.class, ChannelMessages.CHANNEL_ID + " IN(" + TEMPORARY_CHANNELS + ")", String.valueOf(serverId));
            ContentUtils.removeEntities(context, Channel.class, Channel.ID + " IN(" + TEMPORARY_CHANNELS + ")", String.valueOf(serverId));
            ContentUtils.removeEntities(context, ServerChannels.class, ServerChannels.SERVER_ID + " = ?", String.valueOf(serverId));
        }
    }

}
