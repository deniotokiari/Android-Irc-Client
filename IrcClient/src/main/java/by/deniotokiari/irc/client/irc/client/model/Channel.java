package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;

import java.util.List;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.HashUtils;
import by.istin.android.xcore.utils.StringUtil;

public class Channel implements BaseColumns, IGenerateID {

    @dbString
    public static final String TITLE = "title";

    @dbLong
    public static final String SERVER_ID = DBHelper.getForeignKey(Server.class);

    @dbBoolean
    public static final String IS_TEMPORARY = "is_temporary";

    @dbBoolean
    public static final String IS_FIRST_CHANNEL = "is_first_channel";

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return HashUtils.generateId(contentValues.getAsString(TITLE), contentValues.getAsLong(SERVER_ID));
    }

    public static void removeTemporaryChannels(Context context, long serverId) {
        List<ContentValues> list = ContentUtils.getEntities(
                context, Channel.class, Channel.SERVER_ID + " = ? AND " + Channel.IS_TEMPORARY + " = ?",
                String.valueOf(serverId), String.valueOf(1));
        if (list != null && !list.isEmpty()) {
            String ids = "";
            for (int i = 0; i < list.size(); i++) {
                ids += list.get(i).getAsString(_ID);

                if (i < list.size() - 1) {
                    ids += ",";
                }
            }

            if (!StringUtil.isEmpty(ids)) {
                ContentUtils.removeEntities(context, Message.class, Message.CHANNEL_ID + " IN(?)", ids);
            }
        }

        ContentUtils.removeEntities(
                context,
                Channel.class, Channel.SERVER_ID + " = ? AND " + Channel.IS_TEMPORARY + " = ?",
                String.valueOf(serverId), String.valueOf(1));
    }

    public static void addServerChannel(Context context, long serverId) {
        ContentValues values = new ContentValues();

        values.put(SERVER_ID, serverId);
        values.put(IS_TEMPORARY, true);
        values.put(IS_FIRST_CHANNEL, true);

        ContentUtils.putEntity(context, Channel.class, values);
    }

}
