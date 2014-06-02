package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;

public class Channel implements BaseColumns {

    @dbLong
    public static final String ID = _ID;

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String TOPIC = "topic";

    private static final String CHANNEL_ID_SQL = new StringBuilder()
            .append("SELECT ")

            .append("c." + Channel.ID + " AS " + Channel.ID)

            .append(" FROM " + DBHelper.getTableName(ServerChannels.class) + " AS sc")
            .append(" LEFT JOIN " + DBHelper.getTableName(Channel.class) + " AS c")
            .append(" ON sc." + ServerChannels.CHANNEL_ID + " = c." + Channel.ID)

            .append(" WHERE sc." + ServerChannels.SERVER_ID + " = ? AND c." + Channel.NAME + " = ?")

            .toString();

    public static void addChannel(Context context, String name, long serverId) {
        ContentValues values = new ContentValues();

        values.put(NAME, name);
        values.put(ID, HashUtils.generateId(name, serverId));

        ContentUtils.putEntity(context, Channel.class, values);
    }

    @SuppressWarnings("ConstantConditions")
    public static long getChannelId(Context context, long serverId, String channelName) {
        ContentValues values = ContentUtils.getEntity(
                context,
                ModelContract.getSQLQueryUri(
                        CHANNEL_ID_SQL,
                        ModelContract.getUri(Channel.class)),
                null, null, new String[]{String.valueOf(serverId),
                        channelName}
        );

        return values == null ? 0L : values.getAsLong(Channel.ID);
    }

}
