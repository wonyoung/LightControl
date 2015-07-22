package com.wonyoung.lightcontrol;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class LightController {
    private Context mContext;

    public LightController(Context context) {
        mContext = context;
    }

    public void color(int i) {
        Toast.makeText(mContext, "COLOR : " + i, Toast.LENGTH_SHORT).show();
    }
}
