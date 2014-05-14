package by.deniotokiari.irc.client.irc.client.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.helper.PreferencesHelper;
import by.deniotokiari.irc.client.irc.client.model.Channel;
import by.deniotokiari.irc.client.irc.client.model.Server;
import by.istin.android.xcore.utils.ContentUtils;
import by.istin.android.xcore.utils.StringUtil;

@SuppressWarnings("ConstantConditions")
public class AddServerFragment extends DialogFragment implements View.OnClickListener {

    private EditText mServerName;
    private EditText mPort;
    private CheckBox mCheckBoxSettings;
    private EditText mNickName;
    private EditText mUserName;
    private EditText mRealName;
    private EditText mCommands;
    private EditText mChannels;
    private CheckBox mCheckBoxConnect;

    private View mProgress;
    private View mBlank;

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

        mProgress = view.findViewById(android.R.id.progress);
        mBlank = view.findViewById(R.id.blank);

        mServerName = (EditText) view.findViewById(R.id.server_name);
        mPort = (EditText) view.findViewById(R.id.port);
        mCheckBoxSettings = (CheckBox) view.findViewById(R.id.checkbox_settings);
        mNickName = (EditText) view.findViewById(R.id.nick_name);
        mUserName = (EditText) view.findViewById(R.id.user_name);
        mRealName = (EditText) view.findViewById(R.id.real_name);
        mCommands = (EditText) view.findViewById(R.id.commands);
        mChannels = (EditText) view.findViewById(R.id.channels);
        mCheckBoxConnect = (CheckBox) view.findViewById(R.id.checkbox_connect);

        mCheckBoxSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        if (!mCheckBoxSettings.isChecked()) {
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

    protected void addServer() {
        showProgress();

        final String serverName = mServerName.getText().toString();
        final String port = mPort.getText().toString();

        String nickName;
        String userName;
        String realName;

        final boolean connectOnStartUp = mCheckBoxConnect.isChecked();

        if (mCheckBoxSettings.isChecked()) {
            nickName = PreferencesHelper.getNickName();
            userName = PreferencesHelper.getUserName();
            realName = PreferencesHelper.getRealName();

            if (StringUtil.isEmpty(nickName)) {
                nickName = "Test";
            }
            if (StringUtil.isEmpty(userName)) {
                userName = "Test";
            }
            if (StringUtil.isEmpty(realName)) {
                realName = "Test";
            }
        } else {
            nickName = mNickName.getText().toString();
            userName = mUserName.getText().toString();
            realName = mRealName.getText().toString();
        }

        String commands = null;

        if (mCommands.getText() != null) {
            commands = mCommands.getText().toString();
        }

        String channels = null;

        if (mChannels.getText() != null) {
            channels = mChannels.getText().toString();
        }

        final String finalNickName = nickName;
        final String finalUserName = userName;
        final String finalRealName = realName;
        final String finalCommands = commands;
        final String finalChannels = channels;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Activity activity = getActivity();

                ContentValues server = new ContentValues();
                server.put(Server.HOST, serverName);
                server.put(Server.PORT, port);
                server.put(Server.COMMANDS, finalCommands);
                server.put(Server.CONNECT_ON_START_UP, connectOnStartUp);
                server.put(Server.NICK_NAME, finalNickName);
                server.put(Server.USER_NAME, finalUserName);
                server.put(Server.REAL_NAME, finalRealName);

                ContentUtils.putEntity(activity, Server.class, server);

                if (!StringUtil.isEmpty(finalChannels)) {
                    String[] channelsArray = finalChannels.split(",");

                    if (channelsArray != null && channelsArray.length > 0) {
                        long serverId = Server.generateId(server);

                        ContentValues[] channels = new ContentValues[channelsArray.length];
                        for (int i = 0; i < channels.length; i++) {
                            ContentValues item = new ContentValues();
                            item.put(Channel.TITLE, channelsArray[i]);
                            item.put(Channel.SERVER_ID, serverId);
                            item.put(Channel.IS_TEMPORARY, false);
                            item.put(Channel.IS_FIRST_CHANNEL, false);

                            channels[i] = item;
                        }

                        ContentUtils.putEntities(activity, Channel.class, channels);
                    }
                }

                if (activity != null) {
                    new Handler(activity.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Dialog dialog = getDialog();
                            if (dialog != null) {
                                Toast.makeText(activity, "Server added!", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    protected void showProgress() {
        if (getActivity() != null && mBlank != null && mProgress != null) {
            mBlank.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgress() {
        if (getActivity() != null && mBlank != null && mProgress != null) {
            mBlank.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getDialog().cancel();
                break;
            case R.id.ok:
                if (isValuesValid()) {
                    addServer();
                } else {
                    Toast.makeText(getActivity(), "Values not valid!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
