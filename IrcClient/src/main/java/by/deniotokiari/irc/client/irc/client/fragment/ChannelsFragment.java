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
import android.widget.ListView;
import android.widget.TextView;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.activity.MainActivity;
import by.deniotokiari.irc.client.irc.client.adapter.CommonCursorAdapter;
import by.deniotokiari.irc.client.irc.client.cursor.ChannelsCursor;
import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.fragment.AbstractFragment;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;

public class ChannelsFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor>, SimpleCursorAdapter.ViewBinder {

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new CommonCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new String[]{Channel.TITLE},
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
        return R.layout.fragment_channels;
    }

    public Uri getUri() {
        return ModelContract.getSQLQueryUri(ChannelsCursor.SQL, ModelContract.getUri(Channel.class));
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
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if (CursorUtils.getBoolean(Channel.IS_FIRST_CHANNEL, cursor)) {
            TextView textView = (TextView) view;
            textView.setText(CursorUtils.getString(Server.HOST, cursor));
            return true;
        }

        return false;
    }

}
