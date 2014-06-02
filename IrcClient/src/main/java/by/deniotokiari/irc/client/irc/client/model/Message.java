package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;

public class Message implements BaseColumns, IGenerateID {

    @dbLong
    public static final String ID = _ID;

    @dbString
    public static final String BODY = "body";

    @dbString
    public static final String NICK = "nick";

    @dbLong
    public static final String DATE = "date";

    @dbString
    public static final String STATUS = "status";

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return generateId(contentValues);
    }

    public static long generateId(ContentValues contentValues) {
        return HashUtils.generateId(
                contentValues.getAsString(BODY),
                contentValues.getAsString(NICK),
                contentValues.getAsLong(DATE)
        );
    }

    public static void addMessage(Context context, String status, String nick, long channelId, String body) {
        ContentValues values = new ContentValues();

        values.put(NICK, nick);
        values.put(STATUS, status);
        values.put(DATE, System.currentTimeMillis());
        values.put(BODY, body);

        ContentUtils.putEntity(context, Message.class, values);
        ChannelMessages.addMessage(context, channelId, generateId(values));
    }

}
