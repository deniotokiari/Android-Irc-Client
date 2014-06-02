package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;
import android.util.Pair;

import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;

public class ChannelUsers implements BaseColumns, IGenerateID {

    @dbLong
    public static final String ID = _ID;

    @dbLong
    public static final String CHANNEL_ID = DBHelper.getForeignKey(Channel.class);

    @dbLong
    public static final String USER_ID = DBHelper.getForeignKey(User.class);

    @dbString
    public static final String STATUS = "status";

    @dbInteger
    public static final String STATUS_NUM = "status_num";

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return generateId(
                contentValues.getAsLong(CHANNEL_ID),
                contentValues.getAsLong(USER_ID)
        );
    }

    public static long generateId(long channelId, long userId) {
        return HashUtils.generateId(
                channelId,
                userId
        );
    }

    public static void addUsers(Context context, long channelId, long serverId, Pair<Pair<Integer, String>, String>[] users) {
        String[] usersNicks = new String[users.length];
        ContentValues[] values = new ContentValues[users.length];

        for (int i = 0; i < values.length; i++) {
            usersNicks[i] = users[i].second;

            ContentValues contentValues = new ContentValues();

            contentValues.put(CHANNEL_ID, channelId);
            contentValues.put(USER_ID, User.generateId(users[i].second, serverId));
            contentValues.put(STATUS, users[i].first.second);
            contentValues.put(STATUS_NUM, users[i].first.first);

            values[i] = contentValues;
        }

        User.addUsers(context, usersNicks, serverId);

        ContentUtils.putEntities(context, ChannelUsers.class, values);
    }

    public static void addUser(Context context, long channelId, long serverId, Pair<Pair<Integer, String>, String> user) {
        addUsers(context, channelId, serverId, new Pair[]{user});
    }

    public static void deleteUserFromChannel(Context context, long userId, long channelId) {
        ContentUtils.removeEntity(context, ChannelUsers.class, generateId(channelId, userId));
    }

}
