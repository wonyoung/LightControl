package com.wonyoung.lightcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by wonyoung.jang on 15. 7. 23.
 */
public class SettingsFragment extends Fragment {
    private static final int REQUEST_DEVICE_LIST = 2;
    private static LightService mLightService;

    public static SettingsFragment newInstance(LightService service) {
        SettingsFragment.mLightService = service;
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Button connectButton = (Button) rootView.findViewById(R.id.button_connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), DeviceListActivity.class);

                startActivityForResult(intent, REQUEST_DEVICE_LIST);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
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
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        // Attempt to connect to the device
        mLightService.connect(device);
    }
}
