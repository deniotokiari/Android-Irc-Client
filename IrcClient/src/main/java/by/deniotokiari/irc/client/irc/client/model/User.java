package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;

public class User implements BaseColumns, IGenerateID {

    @dbLong
    public static final String ID = _ID;

    @dbString
    public static final String NICK = "nick";

    @dbLong
    public static final String SERVER_ID = DBHelper.getForeignKey(Server.class);

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return generateId(
                contentValues.getAsString(NICK),
                contentValues.getAsLong(SERVER_ID)
        );
    }

    public static long generateId(String nick, long serverId) {
        return HashUtils.generateId(nick, serverId);
    }

    private static final String USER_STATUS_SQL = new StringBuilder()
            .append("SELECT ")

            .append("u." + User.ID + " AS " + User.ID + ",")
            .append("cu." + ChannelUsers.STATUS + " AS " + ChannelUsers.STATUS)

            .append(" FROM " + DBHelper.getTableName(ChannelUsers.class) + " AS cu")
            .append(" LEFT JOIN " + DBHelper.getTableName(User.class) + " u")
            .append(" ON cu." + ChannelUsers.USER_ID + " = u." + User.ID)

            .append(" WHERE cu." + ChannelUsers.CHANNEL_ID + " = ? AND u." + User.NICK + " = ? AND u." + User.SERVER_ID + " = ?")

            .toString();

    public static void updateUser(Context context, long id, String newNick, long serverId) {
        ContentValues values = new ContentValues();

        values.put(User.ID, id);
        values.put(User.NICK, newNick);
        values.put(User.SERVER_ID, serverId);

        ContentUtils.putEntity(context, User.class, values);
    }

    public static String getStatus(Context context, long serverId, long channelId, String nick) {
        ContentValues values = ContentUtils.getEntity(
                context,
                ModelContract.getSQLQueryUri(USER_STATUS_SQL, ModelContract.getUri(User.class)),
                null, null,
                new String[]{String.valueOf(channelId), nick, String.valueOf(serverId)});

        return values == null ? "" : values.getAsString(ChannelUsers.STATUS);
    }

    public static void addUsers(Context context, String[] users, long serverId) {
        ContentValues[] values = new ContentValues[users.length];

        for (int i = 0; i < users.length; i++) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(NICK, users[i]);
            contentValues.put(SERVER_ID, serverId);

            values[i] = contentValues;
        }

        ContentUtils.putEntities(context, User.class, values);
    }


}
