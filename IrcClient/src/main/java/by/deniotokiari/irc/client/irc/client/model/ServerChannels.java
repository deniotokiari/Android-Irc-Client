package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import java.util.List;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;
import by.istin.android.xcore.utils.StringUtil;

public class ServerChannels implements BaseColumns, IGenerateID {

    @dbLong
    public static final String ID = _ID;

    @dbLong
    public static final String SERVER_ID = DBHelper.getForeignKey(Server.class);

    @dbLong
    public static final String CHANNEL_ID = DBHelper.getForeignKey(Channel.class);

    @dbBoolean
    public static final String IS_JOIN = "is_join";

    @dbBoolean
    public static final String IS_TEMPORARY = "is_temporary";

    @dbLong
    public static final String JOIN_DATE = "join_date";

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return HashUtils.generateId(
                contentValues.getAsLong(SERVER_ID),
                contentValues.getAsLong(CHANNEL_ID)
        );
    }

    public static void addChannel(Context context, long serverId, long channelId, String name, boolean isJoin) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(SERVER_ID, serverId);
        contentValues.put(CHANNEL_ID, channelId);
        contentValues.put(IS_JOIN, isJoin);
        //contentValues.put(IS_TEMPORARY, isTemporary);
        contentValues.put(JOIN_DATE, System.currentTimeMillis());

        Channel.addChannel(context, name, serverId);
        ContentUtils.putEntity(context, ServerChannels.class, contentValues);
    }

    private static final String SQL_SERVER_CHANNELS = new StringBuilder()
            .append("SELECT ")
            .append("c." + Channel.NAME + " AS " + Channel.NAME + ",")
            .append("c." + Channel.ID)

            .append(" FROM " + DBHelper.getTableName(ServerChannels.class) + " AS sc")
            .append(" LEFT JOIN " + DBHelper.getTableName(Channel.class) + " AS c")
            .append(" ON sc." + ServerChannels.CHANNEL_ID + " = c." + Channel.ID)

            .append(" WHERE sc." + ServerChannels.IS_TEMPORARY + " = 0 AND sc." + ServerChannels.SERVER_ID + " = ?")

            .toString();

    public static String getJoinedServerChannels(Context context, long serverId) {
        List<ContentValues> list = ContentUtils.getEntities(context, ModelContract.getSQLQueryUri(SQL_SERVER_CHANNELS, ModelContract.getUri(ServerChannels.class)), null, null, new String[]{String.valueOf(serverId)});

        if (list != null && !list.isEmpty()) {
            String result = "";

            for (int i = 0; i < list.size(); i++) {
                result += list.get(i).getAsString(Channel.NAME);

                if (i < list.size() - 1) {
                    result += ",";
                }
            }

            return result;
        }

        return StringUtil.EMPTY;
    }

}
