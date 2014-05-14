package by.deniotokiari.irc.client.irc.client.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.deniotokiari.irc.client.irc.client.service.IrcService;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;

public class ServersFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mServers;
    private View mNoData;
    private View mProgress;

    private CursorAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_servers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();

        if (activity == null || view == null) {
            return;
        }

        View btnCancel = view.findViewById(R.id.cancel);
        View btnAdd = view.findViewById(R.id.add);

        mServers = (ListView) view.findViewById(android.R.id.list);
        mServers.setOnItemClickListener(this);
        mNoData = view.findViewById(R.id.no_data);
        mProgress = view.findViewById(android.R.id.progress);

        getDialog().setTitle("Servers");

        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        mAdapter = new SimpleCursorAdapter(activity, android.R.layout.simple_list_item_2, null, new String[]{Server.HOST, Server.PORT}, new int[]{android.R.id.text1, android.R.id.text2}, 2);
        mServers.setAdapter(mAdapter);

        setVisibility(mProgress, View.VISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(hashCode(), null, this);
    }

    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getDialog().cancel();
                break;
            case R.id.add:
                DialogFragment dialogFragment = new AddServerFragment();
                dialogFragment.show(getFragmentManager(), null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: open connect to this server

        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        long id = CursorUtils.getLong(Server._ID, cursor);

        Intent intent = new Intent(getActivity(), IrcService.class);
        intent.putExtra(IrcService.KEY_SERVER_ID, id);
        getActivity().startService(intent);

        getDialog().cancel();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO show dialog (delete, edit)
        return false;
    }

    public Uri getUri() {
        return ModelContract.getUri(Server.class);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getUri(), null, null, null, Server.HOST);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (CursorUtils.isEmpty(data)) {
            setVisibility(mNoData, View.VISIBLE);
        } else {
            setVisibility(mNoData, View.GONE);
        }

        mAdapter.swapCursor(data);
        setVisibility(mProgress, View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
