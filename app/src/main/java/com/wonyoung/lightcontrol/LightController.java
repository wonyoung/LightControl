package com.wonyoung.lightcontrol;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class LightController {
    private Context mContext;
    private LightService mService;

    public LightController(Context context, LightController mLightController) {
        mContext = context;

    }

    public void color(int i) {
        byte c[] = {'z', (byte)(i >> 16), (byte)(i >> 8),(byte)(i)};
        Toast.makeText(mContext, String.format("COLOR : %x / %x / %x", c[1], c[2], c[3]), Toast.LENGTH_SHORT).show();
        if (mService != null) {
            mService.write(c);
        }
    }



    public void setService(LightService service) {
        mService = service;
    }
}
