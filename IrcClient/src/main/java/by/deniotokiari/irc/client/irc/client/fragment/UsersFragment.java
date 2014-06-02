package by.deniotokiari.irc.client.irc.client.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.adapter.CommonCursorAdapter;
import by.deniotokiari.irc.client.irc.client.cursor.UsersCursor;
import by.deniotokiari.irc.client.irc.client.interfaces.IUsersFragment;
import by.deniotokiari.irc.client.irc.client.model.ChannelUsers;
import by.deniotokiari.irc.client.irc.client.model.User;
import by.istin.android.xcore.fragment.AbstractFragment;
import by.istin.android.xcore.provider.ModelContract;

public class UsersFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor>, IUsersFragment {

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;

    private long mChannelId;
    private long mServerId;

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new CommonCursorAdapter(
                getActivity(),
                R.layout.adapter_drawer,
                new String[]{User.NICK},
                new int[]{android.R.id.text1},
                null
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getUri(), null, null, getSelectionArgs(), null);
    }

    public Uri getUri() {
        return ModelContract.getSQLQueryUri(UsersCursor.SQL, ModelContract.getUri(ChannelUsers.class));
    }

    public String[] getSelectionArgs() {
        return new String[]{String.valueOf(mChannelId), String.valueOf(mServerId)};
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onChannelChange(long channelId, long serverId) {
        mChannelId = channelId;
        mServerId = serverId;

        getLoaderManager().restartLoader(hashCode(), null, this);
    }

}
