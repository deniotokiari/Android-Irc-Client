package by.deniotokiari.irc.client.irc.client.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import by.istin.android.xcore.fragment.CursorLoaderFragment;
import by.istin.android.xcore.fragment.CursorLoaderFragmentHelper;
import by.istin.android.xcore.provider.ModelContract;

public class ServersFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mServers;
    private View mNoData;
    private View mProgress;

    private CursorAdapter mAdapter;
    private CursorLoaderFragment mLoaderFragment = new CursorLoaderFragment() {
        @Override
        protected int getViewLayout() {
            return 0;
        }

        @Override
        public Uri getUri() {
            return ModelContract.getUri(Server.class);
        }

        @Override
        public int getLoaderId() {
            return ServersFragment.this.hashCode();
        }

        @Override
        public void showProgress() {
            setVisibility(mProgress, View.VISIBLE);
        }

        @Override
        public void hideProgress() {
            setVisibility(mProgress, View.GONE);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    };

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

        mNoData = view.findViewById(R.id.no_data);
        mProgress = view.findViewById(android.R.id.progress);

        getDialog().setTitle("Servers");

        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        mAdapter = new SimpleCursorAdapter(activity, android.R.layout.simple_list_item_2, null, new String[]{Server.NAME, Server.PORT}, new int[]{android.R.id.text1, android.R.id.text2}, 2);
        mServers.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CursorLoaderFragmentHelper.onActivityCreated(mLoaderFragment, null);
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
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO show dialog (delete, edit)
        return false;
    }

}
