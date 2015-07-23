package com.wonyoung.lightcontrol;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class LightController {
    private Context mContext;
    private LightService mService;

    public LightController(Context context, LightService service) {
        mContext = context;
        mService = service;
    }

    public void color(int i) {
        byte c[] = {'z', (byte)(i >> 16), (byte)(i >> 8),(byte)(i)};
//        Toast.makeText(mContext, String.format("COLOR : %x / %x / %x", c[1], c[2], c[3]), Toast.LENGTH_SHORT).show();
        if (mService != null) {
            mService.send(c);
        }
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.accumulate("red", Color.red(i));
            jsonObj.accumulate("green", Color.green(i));
            jsonObj.accumulate("blue", Color.blue(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(mContext,jsonObj.toString(), Toast.LENGTH_SHORT).show();
        mService.send(jsonObj.toString());

    }



    public void setService(LightService service) {
        mService = service;
    }
}
