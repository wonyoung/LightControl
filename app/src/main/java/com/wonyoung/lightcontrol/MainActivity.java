package com.wonyoung.lightcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String ARG_SECTION_NUMBER = "section_number";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static final int REQUEST_DEVICE_LIST = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private LightController mLightController = new LightController(this);
    private LightService mLightService = null;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = setUpDrawerFragment();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
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
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mLightService == null) {
            setupLight();
        }
    }

    private void setupLight() {
        mLightService = new LightService(this);
        mLightController.setService(mLightService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLightService != null) {
            if (mLightService.getState() == LightService.STATE_NONE) {
                mLightService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLightService != null) {
            mLightService.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupLight();
                } else {
                    Toast.makeText(this, "To start, Bluetooth should be enabled.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_DEVICE_LIST:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
        }
    }
    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mLightService.connect(device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                return ColorPickerFragment.newInstance(mLightController);
        }

        return SettingsFragment.newInstance();
    }

    @Override
    public void onOptionItemSelected() {
        Intent intent = new Intent(this, DeviceListActivity.class);

        startActivityForResult(intent, REQUEST_DEVICE_LIST);
        return;
    }

    public void onSectionAttached(int number) {

    }

}
