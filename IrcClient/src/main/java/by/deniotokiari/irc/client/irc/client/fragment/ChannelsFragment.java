package by.deniotokiari.irc.client.irc.client.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.activity.MainActivity;
import by.deniotokiari.irc.client.irc.client.adapter.CommonCursorAdapter;
import by.deniotokiari.irc.client.irc.client.cursor.ChannelsCursor;
import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.deniotokiari.irc.client.irc.client.model.ServerChannels;
import by.istin.android.xcore.fragment.AbstractFragment;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;

public class ChannelsFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor>, SimpleCursorAdapter.ViewBinder, AdapterView.OnItemClickListener {

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;

    private boolean mChannelOpened;

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mAdapter = new CommonCursorAdapter(
                getActivity(),
                R.layout.adapter_drawer,
                new String[]{Channel.NAME},
                new int[]{android.R.id.text1},
                this
        );

        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(hashCode(), null, this);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_list;
    }

    public Uri getUri() {
        return ModelContract.getSQLQueryUri(ChannelsCursor.SQL, ModelContract.getUri(ServerChannels.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getUri(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        if (!CursorUtils.isEmpty(data)) {
            ((MainActivity) getActivity()).enableDrawer(Gravity.LEFT);

            if (!mChannelOpened) {
                mChannelOpened = true;

                final long channelId = CursorUtils.getLong(ServerChannels.CHANNEL_ID, data);
                final long serverId = CursorUtils.getLong(ServerChannels.SERVER_ID, data);
                final String channelName = CursorUtils.getString(Channel.NAME, data);


                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openChannel(channelId, serverId, channelName);
                    }
                }, 500L);
            }
        } else {
            ((MainActivity) getActivity()).disableDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if (cursor.isFirst()) {
            TextView header = (TextView) view.findViewById(R.id.header);

            if (header != null) {
                header.setText(CursorUtils.getString(Server.HOST, cursor));

                header.setVisibility(View.VISIBLE);
            }
        }

        return false;
    }

    public void openChannel(long channelId, long serverId, String channelName) {
        MainActivity activity = (MainActivity) getActivity();

        activity.closeDrawer(Gravity.RIGHT);
        ((MainActivity) getActivity()).enableDrawer(Gravity.RIGHT);
        activity.showFragmentInRoot(MessagesFragment.newInstance(serverId, channelId, channelName));
        activity.closeDrawer(Gravity.LEFT);
        activity.updateUsersFragment(channelId, serverId);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        long channelId = CursorUtils.getLong(ServerChannels.CHANNEL_ID, cursor);
        long serverId = CursorUtils.getLong(ServerChannels.SERVER_ID, cursor);
        String channelName = CursorUtils.getString(Channel.NAME, cursor);

        openChannel(channelId, serverId, channelName);
    }

}
