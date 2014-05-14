package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;

public class Server implements BaseColumns, IGenerateID {

    @dbString
    public static final String HOST = "name";

    @dbInteger
    public static final String PORT = "port";

    @dbString
    public static final String COMMANDS = "commands";

    @dbBoolean
    public static final String CONNECT_ON_START_UP = "connect_in_start_up";

    @dbString
    public static final String NICK_NAME = "nick_name";

    @dbString
    public static final String USER_NAME = "user_name";

    @dbString
    public static final String REAL_NAME = "real_name";

    @dbBoolean
    public static final String IS_ONLINE = "is_online";

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return generateId(contentValues);
    }

    public static long generateId(ContentValues contentValues) {
        return HashUtils.generateId(
                contentValues.getAsString(HOST),
                contentValues.getAsInteger(PORT),
                contentValues.getAsBoolean(CONNECT_ON_START_UP),
                contentValues.getAsString(COMMANDS),
                contentValues.getAsString(NICK_NAME),
                contentValues.getAsString(USER_NAME),
                contentValues.getAsString(REAL_NAME)
        );
    }

    public static void setOnline(Context context, ContentValues values, boolean flag) {
        //noinspection ConstantConditions
        long serverId = values.getAsLong(_ID);
        if (!flag) {
            Channel.removeTemporaryChannels(context, serverId);
        } else {
            Channel.addServerChannel(context, serverId);
        }

        values.put(Server.IS_ONLINE, flag);

        ContentUtils.putEntity(context, Server.class, values);
        context.getContentResolver().notifyChange(ModelContract.getUri(Channel.class), null);
    }

}
