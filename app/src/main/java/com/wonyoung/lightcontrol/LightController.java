package com.wonyoung.lightcontrol;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class LightController {

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

    private Context mContext;
    private LightService mService = null;

    public LightController(Context context) {
        mContext = context;
    }

    public void setService(LightService service) {
        mService = service;
    }

    public void color(int i) {
        send(new byte[] {CHANGE_COLOR, (byte) Color.red(i), (byte) Color.green(i), (byte) Color.blue(i)});
    }

    public void brightness(int b) {
        send(new byte[] {CHANGE_BRIGHTNESS, (byte) b, 0, 0 });
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

    public void stop() {
        send(new byte[] {PRESET_STOP, 0, 0, 0 });
    }

    public void blink() {
        send(new byte[] {PRESET_BLINK, 0, 0, 0 });
    }

    private void send(byte[] msg) {
        if (mService != null) {
            mService.send(addDummy(withChecksum(addStartByte(msg))));
        }
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

}
