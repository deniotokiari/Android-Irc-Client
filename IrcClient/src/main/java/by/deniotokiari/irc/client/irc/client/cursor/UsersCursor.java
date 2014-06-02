package by.deniotokiari.irc.client.irc.client.cursor;

import android.database.Cursor;

import by.deniotokiari.irc.client.irc.client.model.ChannelUsers;
import by.deniotokiari.irc.client.irc.client.model.User;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.CursorModel;

public class UsersCursor extends CursorModel {

    public static final String SQL = new StringBuilder()
            .append("SELECT ")

            .append("u." + User.ID + " AS " + User.ID + ",")
            .append("cu." + ChannelUsers.STATUS + " || u." + User.NICK + " AS " + User.NICK)

            .append(" FROM " + DBHelper.getTableName(ChannelUsers.class) + " AS cu")
            .append(" LEFT JOIN " + DBHelper.getTableName(User.class) + " AS u")
            .append(" ON cu." + ChannelUsers.USER_ID + " = u." + User.ID)

            .append(" WHERE cu." + ChannelUsers.CHANNEL_ID + " = ? AND u." + User.SERVER_ID + " = ?")

            .append("ORDER BY cu." + ChannelUsers.STATUS_NUM + ", u." + User.NICK)

            .toString();

    public static final CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new UsersCursor(cursor);
        }
    };

    public UsersCursor(Cursor cursor) {
        super(cursor);
    }

}
