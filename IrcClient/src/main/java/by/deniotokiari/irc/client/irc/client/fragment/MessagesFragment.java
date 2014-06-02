package by.deniotokiari.irc.client.irc.client.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.activity.MainActivity;
import by.deniotokiari.irc.client.irc.client.adapter.CommonCursorAdapter;
import by.deniotokiari.irc.client.irc.client.cursor.MessagesCursor;
import by.deniotokiari.irc.client.irc.client.irc.Command;
import by.deniotokiari.irc.client.irc.client.irc.IrcClient;
import by.deniotokiari.irc.client.irc.client.model.ChannelMessages;
import by.deniotokiari.irc.client.irc.client.model.Message;
import by.deniotokiari.irc.client.irc.client.service.IrcService;
import by.istin.android.xcore.fragment.AbstractFragment;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;
import by.istin.android.xcore.utils.StringUtil;

public class MessagesFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor>, SimpleCursorAdapter.ViewBinder, AdapterView.OnItemClickListener {

    public static final String KEY_CHANNEL_ID = "key:channel_id";
    public static final String KEY_SERVER_ID = "key:server_id";
    public static final String KEY_CHANNEL_NAME = "key:channel_name";

    private long mChannelId;
    private long mServerId;

    private String mChannelName;

    private EditText mEditText;
    private View mButton;
    private IrcClient mIrcClient;
    private SimpleDateFormat mSimpleDateFormat;

    public static MessagesFragment newInstance(long serverId, long channelId, String channelName) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_SERVER_ID, serverId);
        bundle.putLong(KEY_CHANNEL_ID, channelId);
        bundle.putString(KEY_CHANNEL_NAME, channelName);

        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private ListView mListView;
    private CommonCursorAdapter mAdapter;

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);

        mSimpleDateFormat = new SimpleDateFormat("dd MMM, HH:mm:ss");

        Bundle bundle = getArguments();

        if (bundle != null) {
            mChannelId = bundle.getLong(KEY_CHANNEL_ID, 0L);
            mServerId = bundle.getLong(KEY_SERVER_ID, 0L);
            mChannelName = bundle.getString(KEY_CHANNEL_NAME);
        }

        IrcService ircService = ((MainActivity) getActivity()).getIrcService();

        if (ircService != null) {
            mIrcClient = ircService.getIrcClient(mServerId);
        }

        mEditText = (EditText) view.findViewById(R.id.edit_text_message);
        mButton = view.findViewById(R.id.btn_send);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable editable = mEditText.getText();

                if (editable != null && !StringUtil.isEmpty(editable.toString())) {
                    String body = editable.toString();
                    String msg = "";

                    if (body.length() > 1 && body.startsWith("/") && !body.startsWith("/", 1)) {
                        msg = body.replaceFirst("/", "");
                    } else {
                        msg = Command.PRIVMSG.name() + " " + mChannelName + " " + body;
                    }

                    if (mIrcClient != null) {
                        mIrcClient.send(msg);
                    }

                    mEditText.setText("");
                }
            }
        });

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mAdapter = new CommonCursorAdapter(
                getActivity(),
                R.layout.adapter_message,
                new String[]{Message.DATE, Message.NICK, Message.BODY},
                new int[]{R.id.date, R.id.nick, R.id.body},
                this
        );

        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(hashCode(), null, this);

        MainActivity activity = (MainActivity) getActivity();

        if (activity != null && mIrcClient != null) {
            activity.setActionBarTitle(mIrcClient.getNick() + " @ " + mIrcClient.getHost() + " / " + mChannelName);
        }
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_messages;
    }

    public Uri getUri() {
        return ModelContract.getSQLQueryUri(MessagesCursor.SQL, ModelContract.getUri(ChannelMessages.class));
    }

    public String[] getSelectionArgs() {
        return new String[]{String.valueOf(mChannelId)};
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getUri(), null, null, getSelectionArgs(), null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        if (data.getCount() > 0) {
            mListView.smoothScrollToPosition(data.getCount() - 1);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if (columnIndex == cursor.getColumnIndex(Message.DATE)) {
            TextView textView = (TextView) view;

            textView.setText(mSimpleDateFormat.format(CursorUtils.getLong(Message.DATE, cursor)));

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
