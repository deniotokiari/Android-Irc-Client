package by.deniotokiari.irc.client.irc.client.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import by.deniotokiari.irc.client.irc.client.R;
import by.istin.android.xcore.utils.StringUtil;

public class AddServerFragment extends DialogFragment implements View.OnClickListener {

    private EditText mServerName;
    private EditText mPort;
    private CheckBox mCheckBox_settings;
    private EditText mNickName;
    private EditText mUserName;
    private EditText mRealName;
    private EditText mCommands;
    private EditText mChannels;
    private CheckBox mCheckBox_connect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_server, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("Add Server");

        View btnCancel = view.findViewById(R.id.cancel);
        View btnOk = view.findViewById(R.id.ok);

        mServerName = (EditText) view.findViewById(R.id.server_name);
        mPort = (EditText) view.findViewById(R.id.port);
        mCheckBox_settings = (CheckBox) view.findViewById(R.id.checkbox_settings);
        mNickName = (EditText) view.findViewById(R.id.nick_name);
        mUserName = (EditText) view.findViewById(R.id.user_name);
        mRealName = (EditText) view.findViewById(R.id.real_name);
        mCommands = (EditText) view.findViewById(R.id.commands);
        mChannels = (EditText) view.findViewById(R.id.channels);
        mCheckBox_connect = (CheckBox) view.findViewById(R.id.checkbox_connect);

        mCheckBox_settings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value) {
                    mNickName.setEnabled(false);
                    mUserName.setEnabled(false);
                    mRealName.setEnabled(false);
                } else {
                    mNickName.setEnabled(true);
                    mUserName.setEnabled(true);
                    mRealName.setEnabled(true);
                }
            }
        });
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private boolean isValuesValid() {
        if (StringUtil.isEmpty(mServerName.getText().toString())) {
            return false;
        } else if (StringUtil.isEmpty(mPort.getText().toString())) {
            return false;
        }
        if (!mCheckBox_settings.isChecked()) {
            if (StringUtil.isEmpty(mNickName.getText().toString())) {
                return false;
            } else if (StringUtil.isEmpty(mUserName.getText().toString())) {
                return false;
            } else if (StringUtil.isEmpty(mRealName.getText().toString())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getDialog().cancel();
                break;
            case R.id.ok:
                if (isValuesValid()) {

                } else {
                    Toast.makeText(getActivity(), "Values not valid!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
