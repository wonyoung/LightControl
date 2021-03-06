package com.wonyoung.lightcontrol.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Created by wonyoung.jang on 15. 7. 27..
 */
public interface LightDevice {

    void stop();

    void start();

    void connect(String address);

    void resume();

    void send(byte[] msg);

    boolean isConnected();

    public interface DeviceListener {
        void onConnected();

        void onConnectionFailed();
    }
}
