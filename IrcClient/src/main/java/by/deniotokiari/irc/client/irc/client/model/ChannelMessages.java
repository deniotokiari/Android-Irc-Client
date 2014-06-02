package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;

public class ChannelMessages implements BaseColumns, IGenerateID {

    @dbLong
    public static final String ID = _ID;

    @dbLong
    public static final String CHANNEL_ID = DBHelper.getForeignKey(Channel.class);

    @dbLong
    public static final String MESSAGE_ID = DBHelper.getForeignKey(Message.class);

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return HashUtils.generateId(
                contentValues.getAsLong(CHANNEL_ID),
                contentValues.getAsLong(MESSAGE_ID)
        );
    }

    public static void addMessage(Context context, long channelId, long messageId) {
        ContentValues values = new ContentValues();

        values.put(CHANNEL_ID, channelId);
        values.put(MESSAGE_ID, messageId);

        ContentUtils.putEntity(context, ChannelMessages.class, values);
    }

}
