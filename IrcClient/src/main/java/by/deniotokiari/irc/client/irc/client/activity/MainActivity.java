package by.deniotokiari.irc.client.irc.client.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import by.deniotokiari.irc.client.irc.client.R;
import by.deniotokiari.irc.client.irc.client.fragment.ServersFragment;
import by.deniotokiari.irc.client.irc.client.interfaces.IUsersFragment;
import by.deniotokiari.irc.client.irc.client.fragment.StartUpFragment;
import by.deniotokiari.irc.client.irc.client.helper.ActionBarHelper;
import by.deniotokiari.irc.client.irc.client.service.IrcService;
import by.istin.android.xcore.utils.StringUtil;

public class MainActivity extends ActionBarActivity {

    private ActionBar mActionBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private ServiceConnection mServiceConnection;
    private IrcService mIrcService;

    private boolean mIsConnectedToServer;

    private String mCurrentActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mActionBar = getSupportActionBar();
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {

                if (!StringUtil.isEmpty(mCurrentActionBarTitle)) {
                    ActionBarHelper.setText(MainActivity.this, mCurrentActionBarTitle);
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                int id = drawerView.getId();

                mCurrentActionBarTitle = MainActivity.this.getSupportActionBar().getTitle().toString();

                switch (id) {
                    case R.id.left_drawer:
                        getSupportActionBar().setTitle("Channels");
                        break;
                    case R.id.right_drawer:
                        getSupportActionBar().setTitle("Users");
                        break;
                }

                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        disableDrawer(Gravity.LEFT);
        disableDrawer(Gravity.RIGHT);

        if (savedInstanceState == null) {
            showFragmentInRoot(new StartUpFragment());
        }

        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mIrcService = ((IrcService.IrcServiceBinder) iBinder).getService();
                    mIsConnectedToServer = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mIsConnectedToServer = false;
                }
            };
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_servers) {
            DialogFragment dialogFragment = new ServersFragment();
            dialogFragment.show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void closeDrawer(int gravity) {
        mDrawerLayout.closeDrawer(gravity);
    }

    public void enableDrawer(int gravity) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, gravity);
    }

    public void disableDrawer(int gravity) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, gravity);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
    }

    public void showFragmentInRoot(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    public void updateUsersFragment(long channelId, long serverId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.right_drawer);

        if (fragment != null && fragment instanceof IUsersFragment) {
            ((IUsersFragment) fragment).onChannelChange(channelId, serverId);
        }
    }

    public void setActionBarTitle(String title) {
        mCurrentActionBarTitle = title;

        ActionBarHelper.setText(this, title);
    }

    public IrcService getIrcService() {
        return mIrcService;
    }

    @Override
    protected void onResume() {
        super.onResume();

        bindService(new Intent(this, IrcService.class), mServiceConnection, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(mServiceConnection);
    }

}
