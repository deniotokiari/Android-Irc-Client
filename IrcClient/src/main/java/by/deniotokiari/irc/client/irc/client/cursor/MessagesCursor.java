package by.deniotokiari.irc.client.irc.client.cursor;

import android.database.Cursor;

import by.deniotokiari.irc.client.irc.client.model.ChannelMessages;
import by.deniotokiari.irc.client.irc.client.model.Message;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.CursorModel;

public class MessagesCursor extends CursorModel {

    public static final String SQL = new StringBuilder()
            .append("SELECT ")

            .append("m." + Message.ID + " AS " + Message.ID + ",")
            .append("m." + Message.BODY + " AS " + Message.BODY + ",")
            .append("m." + Message.DATE + " AS " + Message.DATE + ",")
            .append("m." + Message.STATUS + " || m." + Message.NICK + " AS " + Message.NICK)

            .append(" FROM " + DBHelper.getTableName(ChannelMessages.class) + " AS cm")
            .append(" LEFT JOIN " + DBHelper.getTableName(Message.class) + " AS m")
            .append(" ON cm." + ChannelMessages.MESSAGE_ID + " = m." + Message.ID)

            .append(" WHERE cm." + ChannelMessages.CHANNEL_ID + " = ?")

            .append(" ORDER BY m." + Message.DATE)

            .toString();

    public static final CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new MessagesCursor(cursor);
        }
    };

    public MessagesCursor(Cursor cursor) {
        super(cursor);
    }

}
