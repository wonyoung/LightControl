package com.wonyoung.lightcontrol;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class LightController {

    private static final byte CHANGE_COLOR = 11;
    private static final byte CHANGE_BRIGHTNESS = 12;
    private static final byte PRESET_RAINBOW = 21;
    private static final byte PRESET_RAINBOW_SLOW = 22;

    private Context mContext;
    private LightService mService = null;

    public LightController(Context context) {
        mContext = context;
    }

    public void setService(LightService service) {
        mService = service;
    }

    public void color(int i) {
        byte[] msg = withChecksum(colorCommand(Color.red(i), Color.green(i), Color.blue(i)));
        if (mService != null) {
            mService.send(msg);
        }
    }

    private byte[] colorCommand(int red, int green, int blue) {
        return new byte[]{CHANGE_COLOR, (byte) red, (byte) green, (byte) blue};
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

    /*
    #define CMD_LENGTH 5
    int index = 0;
    char buf[CMD_LENGTH]
    boolean verifyChecksum(char* buf)
    {
      int cs = buf[CMD_LENGTH-2];
      int sum = 0;
      for(int i = 0; i < CMD_LENGTH-1; i++)
      {
        sum += buf[i];
      }
      if (sum == cs) return true;
      return false;
    }

    void handleCommand(char* buf)
    {
      for(int i = 0; i < CMD_LENGTH; i++)
      {
        Serial.println(buf[i]);
      }

      if(verifyChecksum(buf)) {
        BTSerial.println("NG");
        return;
      }
      char cmd = buf[0];
      switch(cmd)
      {
      }
    }

    void loop()
    {
      while(BTSerial.available())
      {
        c = BTSerial.read();
        buf[i] = c;
        i++;
        if (i == CMD_LENGTH)
        {
          handleCommand(buf);
          i = 0;
        }
      }
    }
     */
}
