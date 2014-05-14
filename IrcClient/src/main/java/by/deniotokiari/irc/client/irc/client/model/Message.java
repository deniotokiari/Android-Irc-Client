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
    public static final String CHANNEL_ID = DBHelper.getForeignKey(Channel.class);

    @dbString
    public static final String BODY = "body";

    @dbLong
    public static final String DATE = "date";

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return HashUtils.generateId(
                contentValues.getAsLong(CHANNEL_ID),
                contentValues.getAsString(BODY),
                contentValues.getAsLong(DATE)
        );
    }

    public static void removeMessages(Context context, long channelId) {
        ContentUtils.removeEntities(context, Channel.class, CHANNEL_ID + " = ?", String.valueOf(channelId));
    }

}
