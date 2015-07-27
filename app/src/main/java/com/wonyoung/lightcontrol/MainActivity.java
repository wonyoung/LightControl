package com.wonyoung.lightcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String ARG_SECTION_NUMBER = "section_number";

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static final int REQUEST_SELECT_BT_DEVICE = 2;

    private LightController mLightController = new LightController(this);
    private LightDevice mLightDevice = null;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = setUpDrawerFragment();
        mTitle = getTitle();
    }

    private NavigationDrawerFragment setUpDrawerFragment() {
        NavigationDrawerFragment fragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        fragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        return fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLightDevice == null) {
            setupLight();
        }
    }

    private void setupLight() {
        mLightDevice = new BluetoothLightDevice(new LightDevice.DeviceListener() {
            @Override
            public void onConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connected.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onConnectionFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Toast.makeText(MainActivity.this, "Connection Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mLightController.setService(mLightDevice);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLightDevice != null) {
            mLightDevice.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLightDevice != null) {
            mLightDevice.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_SELECT_BT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
        }
    }

    private void connectDevice(Intent data) {
        String address = data.getExtras()
                .getString(EXTRA_DEVICE_ADDRESS);

        mLightDevice.connect(address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getPlaceholderFragment(position + 1))
                .commit();
    }

    private Fragment getPlaceholderFragment(int sectionNumber) {
        switch(sectionNumber) {
            case 1:
                return PresetColorFragment.newInstance(mLightController, sectionNumber);
            case 2:
                return ColorPickerFragment.newInstance(mLightController, sectionNumber);
        }

        return SettingsFragment.newInstance(sectionNumber);
    }

    @Override
    public void onOptionItemSelected() {
        Intent intent = new Intent(this, SelectBluetoothDeviceActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_BT_DEVICE);
        return;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_preset);
                break;
            case 2:
                mTitle = getString(R.string.title_simple_color);
                break;
            case 3:
                mTitle = getString(R.string.title_settings);
                break;
        }
        restoreActionBar();
    }

}
