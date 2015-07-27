package com.wonyoung.lightcontrol.control;

import android.graphics.Color;
import android.os.Handler;

import com.wonyoung.lightcontrol.device.LightDevice;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class ArduinoLightController implements LightController {

    private static final byte START_BYTE = 77;

    private static final byte CHANGE_COLOR = 11;
    private static final byte CHANGE_BRIGHTNESS = 12;
    private static final byte CHANGE_PERIOD = 13;

    private static final byte PRESET_RAINBOW = 21;
    private static final byte PRESET_GRADIENT = 22;
    private static final byte PRESET_BLACK = 23;
    private static final byte PRESET_WHITE = 24;
    private static final byte PRESET_STOP = 25;
    private static final byte PRESET_BLINK = 26;

    private Delayer delayer = new Delayer();

    private LightDevice mLight;

    public ArduinoLightController(LightDevice device) {
        mLight = device;
    }

    public void color(int i) {
        send(new byte[] {CHANGE_COLOR, (byte) Color.red(i), (byte) Color.green(i), (byte) Color.blue(i)});
    }

    public void brightness(int b) {
        send(new byte[] {CHANGE_BRIGHTNESS, (byte) b, 0, 0 });
    }

    @Override
    public void resume() {
        mLight.resume();
    }

    @Override
    public void stop() {
        mLight.stop();
    }

    @Override
    public void connect(String address) {
        mLight.connect(address);
    }

    @Override
    public boolean isConnected() {
        return mLight.isConnected();
    }

    public void period(int b) {
        send(new byte[] {CHANGE_PERIOD, (byte) b, 0, 0 });
    }

    public void rainbow() {
        send(new byte[] {PRESET_RAINBOW, 0, 0, 0 });
    }

    public void gradient() {
        send(new byte[] {PRESET_GRADIENT, 0, 0, 0 });
    }

    public void black() {
        send(new byte[] {PRESET_BLACK, 0, 0, 0 });
    }

    public void white() {
        send(new byte[] {PRESET_WHITE, 0, 0, 0 });
    }

    public void pause() {
        send(new byte[] {PRESET_STOP, 0, 0, 0 });
    }

    public void blink() {
        send(new byte[] {PRESET_BLINK, 0, 0, 0 });
    }

    private void send(final byte[] msg) {
        delayer.run(new Runnable() {
            @Override
            public void run() {
                mLight.send(addDummy(withChecksum(addStartByte(msg))));
            }
        });
    }

    private byte[] addDummy(byte[] msg) {
        byte[] bytes = new byte[msg.length+2];
        for (int i = 0; i < msg.length; i++) {
            bytes[i] = msg[i];
        }
        return bytes;
    }

    private byte[] addStartByte(byte[] msg) {
        byte[] bytes = new byte[msg.length+1];
        bytes[0] = START_BYTE;
        for (int i = 0; i < msg.length; i++) {
            bytes[i+1] = msg[i];
        }
        return bytes;
    }

    private byte[] withChecksum(byte[] cmd) {
        byte[] withcs = new byte[cmd.length+1];
        byte sum = 0;
        for (int i = 0; i < cmd.length; i++) {
            withcs[i] = cmd[i];
            sum += cmd[i];
        }
        withcs[withcs.length - 1] = sum;
        return withcs;
    }


    private class Delayer {
        private Handler handler = new Handler();

        public void run(Runnable runnable) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnable, 100);
        }
    }

}
