package by.deniotokiari.irc.client.irc.client.model;

import android.content.ContentValues;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IGenerateID;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.HashUtils;

public class Channel implements IGenerateID {

    @dbString
    public static final String TITLE = "title";

    @dbLong
    public static final String SERVER = DBHelper.getForeignKey(Server.class);

    @Override
    public long generateId(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, ContentValues contentValues) {
        return HashUtils.generateId(contentValues.getAsInteger(TITLE), contentValues.getAsLong(SERVER));
    }

}
