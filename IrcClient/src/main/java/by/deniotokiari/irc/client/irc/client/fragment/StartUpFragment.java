package by.deniotokiari.irc.client.irc.client.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.deniotokiari.irc.client.irc.client.R;

public class StartUpFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnAddServer = view.findViewById(R.id.btn_servers);
        View btnSettings = view.findViewById(R.id.btn_settings);

        btnAddServer.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_servers:
                DialogFragment dialogFragment = new ServersFragment();
                dialogFragment.show(getFragmentManager(), null);
                break;
            case R.id.btn_settings:
                break;
        }
    }

}
