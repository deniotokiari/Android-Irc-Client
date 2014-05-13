package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.HashUtils;

public class Server implements BaseColumns, IGenerateID {

    @dbString
    public static final String NAME = "name";

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

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return generateId(contentValues);
    }

    public static long generateId(ContentValues contentValues) {
        return HashUtils.generateId(
                contentValues.getAsString(NAME),
                contentValues.getAsInteger(PORT),
                contentValues.getAsBoolean(CONNECT_ON_START_UP),
                contentValues.getAsString(COMMANDS),
                contentValues.getAsString(NICK_NAME),
                contentValues.getAsString(USER_NAME),
                contentValues.getAsString(REAL_NAME)
        );
    }

}
