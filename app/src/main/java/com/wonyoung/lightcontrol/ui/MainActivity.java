package com.wonyoung.lightcontrol.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wonyoung.lightcontrol.R;
import com.wonyoung.lightcontrol.control.ArduinoLightController;
import com.wonyoung.lightcontrol.control.LightController;
import com.wonyoung.lightcontrol.device.BluetoothLightDevice;
import com.wonyoung.lightcontrol.device.LightDevice;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LightController {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String EXTRA_DEVICE_ADDRESS = "device_address";

    private static final int REQUEST_SELECT_BT_DEVICE = 2;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MenuItem mMenuItemConnect;
    private CharSequence mTitle;

    private final LightDevice nullDevice = new LightDevice() {

        @Override
        public void stop() {

        }

        @Override
        public void start() {

        }

        @Override
        public void connect(String address) {

        }

        @Override
        public void resume() {

        }

        @Override
        public void send(byte[] msg) {

        }

        @Override
        public boolean isConnected() {
            return false;
        }
    };
    private LightController mLightController = new ArduinoLightController(nullDevice);

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
    protected void onResume() {
        super.onResume();
        mLightController.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLightController.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_SELECT_BT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    mLightController = makeBluetoothController();
                    connectDevice(data);
                }
                break;
        }
    }

    private LightController makeBluetoothController() {
        return new ArduinoLightController(new BluetoothLightDevice(
                new LightDevice.DeviceListener() {
                    @Override
                    public void onConnected() {
                        toast("Connected.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMenuItemConnect.setTitle(R.string.action_disconnect);
                            }
                        });
                    }

                    @Override
                    public void onConnectionFailed() {
                        toast("Connection Failed.");
                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            mMenuItemConnect = (MenuItem) menu.findItem(R.id.action_connect);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
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
                return PresetColorFragment.newInstance(sectionNumber);
            case 2:
                return ColorPickerFragment.newInstance(sectionNumber);
        }

        return SettingsFragment.newInstance(sectionNumber);
    }

    @Override
    public void onOptionItemSelected() {
        if (mLightController.isConnected()) {
            mLightController.stop();
            mMenuItemConnect.setTitle(R.string.action_connect);
        }
        else {
            Intent intent = new Intent(this, SelectBluetoothDeviceActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_BT_DEVICE);
        }
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

    private void connectDevice(Intent data) {
        String address = data.getExtras()
                .getString(EXTRA_DEVICE_ADDRESS);

        mLightController.connect(address);
    }

    private void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final LightDevice debugDevice = new LightDevice() {
        public boolean mConnect = false;

        @Override
        public void stop() {
            toast("Dummy: stop");
            mConnect = false;
        }

        @Override
        public void start() {
            toast("Dummy: start");
        }

        @Override
        public void connect(String address) {
            toast("Dummy: connect");
            mConnect = true;
        }

        @Override
        public void resume() {
            toast("Dummy: resume");
        }

        @Override
        public void send(byte[] msg) {
            toast("Dummy: send " + Arrays.toString(msg));
        }

        @Override
        public boolean isConnected() {
            return mConnect;
        }
    };

    @Override
    public void color(int i) {
        mLightController.color(i);
    }

    @Override
    public void black() {
        mLightController.black();
    }

    @Override
    public void white() {
        mLightController.white();
    }

    @Override
    public void rainbow() {
        mLightController.rainbow();
    }

    @Override
    public void gradient() {
        mLightController.gradient();
    }

    @Override
    public void pause() {
        mLightController.pause();
    }

    @Override
    public void blink() {
        mLightController.blink();
    }

    @Override
    public void period(int p) {
        mLightController.period(p);
    }

    @Override
    public void brightness(int brightness) {
        mLightController.brightness(brightness);
    }

    @Override
    public void resume() {
        mLightController.resume();
    }

    @Override
    public void stop() {
        mLightController.stop();
    }

    @Override
    public void connect(String address) {
        mLightController.connect(address);
    }

    @Override
    public boolean isConnected() {
        return mLightController.isConnected();
    }
}
